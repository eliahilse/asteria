import ExcelJS from 'exceljs';
import type { MatrixData } from './experiment-types';
import { combinationRows } from './combination-stats';
import { allRunPassRate, compileRate, compiledEntries, fullRate, functionalCounts, functionalSuites,
  mostCommonFailure, paperContexts, ratio, reportGroups, suiteOutcome,
  type FunctionalSuite, type ReportEntry, type ReportGroup } from './report-data';

type Value = string | number | null;
const blue = 'FF1F4E79', purple = 'FF6B4585';
const border: ExcelJS.Border = { style: 'thin', color: { argb: 'FFD9D9D9' } };
const identities = ['Study', 'Task', 'Method', 'Security strategy'];
const identity = (g: ReportGroup): Value[] => [g.study.plan.id, g.task, g.method, g.security];
const tierNames = { unit: 'Unit', invoked: 'Invoked (coupling)', autonomous: 'Autonomous (wiring)' };
const contextEntries = (g: ReportGroup, context: string) => g.entries.filter(e => e.context === context);
const difference = (a: number | null, b: number | null) => a === null || b === null ? null : a - b;

export function reportTable(sheet: ExcelJS.Worksheet, headers: string[], rows: Value[][], percentages: number[] = [], start = 1, lowerIsBetter: number[] = []) {
  const header = sheet.getRow(start);
  header.values = headers;
  header.height = 32;
  headers.forEach((label, i) => {
    const cell = header.getCell(i + 1);
    cell.font = { name: 'Arial', size: 11, bold: true, color: { argb: 'FFFFFFFF' } };
    cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: label.includes('Security') ? purple : blue } };
    cell.alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
    cell.border = { top: border, bottom: border, left: border, right: border };
    if (!sheet.getColumn(i + 1).width) sheet.getColumn(i + 1).width = label === 'Study' ? 34 : /Test$|failure|Meaning|ID|SHA/.test(label) ? 45 : label.includes('Security') ? 23 : 14;
  });
  rows.forEach((values, i) => {
    const row = sheet.getRow(start + i + 1);
    row.values = values;
    headers.forEach((_, j) => {
      const cell = row.getCell(j + 1);
      cell.font = { name: 'Arial', size: 10 };
      cell.border = { top: border, bottom: border, left: border, right: border };
      cell.alignment = { vertical: 'top', horizontal: typeof cell.value === 'number' ? 'center' : 'left', wrapText: true };
      if (i % 2 === 0) cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF2F2F2' } };
      if (percentages.includes(j + 1)) cell.numFmt = '0.0%';
      if (typeof cell.value === 'string' && cell.value.length > 30000) {
        cell.value = cell.value.slice(0, 29800) + '\n[Excerpt; full diagnostic remains in the detailed export.]';
      }
    });
  });
  for (const column of percentages) if (rows.length) sheet.addConditionalFormatting({
    ref: `${sheet.getCell(start + 1, column).address}:${sheet.getCell(start + rows.length, column).address}`,
    rules: [{ type: 'colorScale', priority: start * headers.length + column,
      cfvo: [{ type: 'num', value: 0 }, { type: 'num', value: 0.5 }, { type: 'num', value: 1 }],
      color: (lowerIsBetter.includes(column) ? ['FF63BE7B', 'FFFFEB84', 'FFF8696B'] : ['FFF8696B', 'FFFFEB84', 'FF63BE7B']).map(argb => ({ argb })) }],
  });
  if (start === 1) {
    sheet.views = [{ state: 'frozen', xSplit: headers[0] === 'Study' ? 4 : 0, ySplit: 1 }];
    sheet.autoFilter = { from: { row: 1, column: 1 }, to: { row: Math.max(1, rows.length + 1), column: headers.length } };
  }
  sheet.pageSetup = { orientation: 'landscape', fitToPage: true, fitToWidth: 1, fitToHeight: 0 };
  return start + rows.length + 1;
}

function title(sheet: ExcelJS.Worksheet, row: number, text: string) {
  sheet.getCell(row, 1).value = text;
  sheet.getCell(row, 1).font = { name: 'Arial', size: 13, bold: true, color: { argb: blue } };
  sheet.getRow(row).height = 24;
}

function pivot(sheet: ExcelJS.Worksheet, groups: ReportGroup[], metric: (entries: ReportEntry[]) => number | null, start = 1) {
  const rows = groups.map(g => [...identity(g), ...paperContexts.map(c => metric(contextEntries(g, c))), metric(g.entries)]);
  return reportTable(sheet, [...identities, ...paperContexts, 'Overall'], rows, Array.from({ length: 9 }, (_, i) => i + 5), start);
}

function catalog(group: ReportGroup) {
  return [...new Map(group.study.summary.conditions.flatMap(c => c.checks.filter(t => Object.hasOwn(functionalSuites, t.suite))).map(t => [t.id, t])).values()];
}

/** The supplied report's eleven-sheet layout, populated only from current observations. */
export function reportWorkbook(data: MatrixData) {
  const wb = new ExcelJS.Workbook();
  wb.creator = 'Asteria research export';
  const groups = reportGroups(data);
  const overview = wb.addWorksheet('Overview');
  overview.getColumn(1).width = 36;
  overview.getColumn(2).width = 65;
  title(overview, 1, 'ApoGames - LLM Context Study');
  let row = 3;
  for (const study of data.studies) {
    const entries = groups.filter(g => g.study === study).flatMap(g => g.entries);
    const repetitions = [...new Set(study.plan.conditions.map(c => c.repetitions))];
    const plan = study.plan as typeof study.plan & { settings?: { temperature?: number }; temperature?: number };
    row = reportTable(overview, ['Experiment Summary', study.plan.id], [
      ['Total runs', entries.length], ['Prompt variants', study.plan.conditions.length],
      ['Runs per prompt (planned)', repetitions.length === 1 ? repetitions[0] : 'Varies'],
      ['Model', study.plan.model], ['Temperature (requested)', plan.settings?.temperature ?? plan.temperature ?? null],
      ['Submission cap', study.plan.maxSubmissions ?? 1], ['Collection complete', study.summary.complete ? 'Yes' : 'No'],
      ['Overall compile rate', compileRate(entries)], ['Overall test pass rate (measured)', functionalCounts(entries).rate],
      ['Fully functional / all runs', fullRate(entries)], ['Unresolved functional checks on compiled runs', functionalCounts(entries).unresolved],
      ['Observation unit', study.plan.observationUnit ?? 'attempt'],
    ], [], row);
    for (let r = row - 5; r <= row - 3; r++) overview.getCell(r, 2).numFmt = '0.0%';
    row += 2;
    title(overview, row++, 'Test Pass Rate by Test Type');
    row = reportTable(overview, ['Test Type', 'Pass Rate', 'Passed', 'Evaluated', 'Unresolved'],
      Object.entries(tierNames).map(([suite, name]) => {
        const c = functionalCounts(entries, suite as FunctionalSuite);
        return [name, c.rate, c.passed, c.evaluated, c.unresolved];
      }), [2], row) + 2;
  }
  title(overview, row++, 'Per-Task / Method / Security Summary');
  reportTable(overview, [...identities, 'Compile', 'Test Pass', 'Compile Fails', 'n'], groups.map(g => [
    ...identity(g), compileRate(g.entries), functionalCounts(g.entries).rate,
    g.entries.filter(e => e.run.mainCompilation === 'fail').length, g.entries.length,
  ]), [5, 6], row);

  pivot(wb.addWorksheet('Compile Rate'), groups, compileRate);
  pivot(wb.addWorksheet('Pass Rate'), groups, entries => functionalCounts(entries).rate);

  const methods = wb.addWorksheet('Reuse vs Generation');
  title(methods, 1, 'Combined functional pass rate (measured checks on compiled runs)');
  row = pivot(methods, groups, entries => functionalCounts(entries).rate, 3) + 2;
  title(methods, row++, 'Compile rate by context (all recorded runs)');
  pivot(methods, groups, compileRate, row);

  const failures = wb.addWorksheet('Compile Failure Analysis');
  reportTable(failures, [...identities, 'Primary error type', '# failed runs', 'Compile fail / all runs', 'Meaning'],
    groups.filter(g => g.entries.some(e => e.run.mainCompilation === 'fail')).map(g => {
      const n = g.entries.filter(e => e.run.mainCompilation === 'fail').length;
      return [...identity(g), 'Unclassified', n, ratio(n, g.entries.length), 'Compiler classification is not provided by the compact dataset; see archived compiler logs.'];
    }), [7], 1, [7]);

  const testFailures: Value[][] = [];
  for (const g of groups) for (const test of catalog(g)) {
    const c = functionalCounts(g.entries, test.suite as FunctionalSuite, test.name);
    if (c.failed) testFailures.push([...identity(g), tierNames[test.suite as FunctionalSuite], test.name, c.failed,
      c.evaluated, compiledEntries(g.entries).length, c.unresolved, ratio(c.failed, c.evaluated), mostCommonFailure(g.entries, test.suite, test.name)]);
  }
  testFailures.sort((a, b) => Number(b[6]) - Number(a[6]));
  reportTable(wb.addWorksheet('Test Failure Analysis'), [...identities, 'Tier', 'Test', '# fails', '# tested', '# compiled', '# unresolved', 'Fail rate', 'Most common failure'], testFailures, [11], 1, [11]);

  const perTest = wb.addWorksheet('Per-Test Breakdown');
  const perTestRows: Value[][] = [];
  const perTestNotes: string[][] = [];
  for (const g of groups) for (const test of catalog(g)) {
    const counts = [...paperContexts.map(c => contextEntries(g, c)), g.entries].map(entries => functionalCounts(entries, test.suite as FunctionalSuite, test.name));
    perTestRows.push([...identity(g), tierNames[test.suite as FunctionalSuite], test.name, ...counts.map(c => c.rate)]);
    perTestNotes.push(counts.map(c => `${c.passed} passed / ${c.evaluated} evaluated; ${c.failed} failed; ${c.unresolved} unresolved on compiled runs.`));
  }
  reportTable(perTest, [...identities, 'Tier', 'Test', ...paperContexts, 'Overall'], perTestRows, Array.from({ length: 9 }, (_, i) => i + 7));
  perTestNotes.forEach((notes, i) => notes.forEach((note, j) => { perTest.getCell(i + 2, j + 7).note = note; }));

  const tiers = wb.addWorksheet('Tier Breakdown');
  row = 1;
  for (const [suite, name] of Object.entries(tierNames)) {
    title(tiers, row++, name);
    row = pivot(tiers, groups, entries => functionalCounts(entries, suite as FunctionalSuite).rate, row) + 2;
  }

  const effects = wb.addWorksheet('Context Effect');
  title(effects, 1, 'Descriptive presence/absence comparison; selected contexts may be unbalanced.');
  row = reportTable(effects, [...identities, 'Context type', 'n present', 'n absent', 'Compile% present', 'Compile% absent', 'Delta compile', 'Pass% present', 'Pass% absent', 'Delta pass'],
    groups.flatMap(g => ['S', 'F', 'B'].map(context => {
      const present = g.entries.filter(e => e.context.split('+').includes(context));
      const absent = g.entries.filter(e => !e.context.split('+').includes(context));
      const cp = compileRate(present), ca = compileRate(absent), pp = functionalCounts(present).rate, pa = functionalCounts(absent).rate;
      return [...identity(g), context, present.length, absent.length, cp, ca, difference(cp, ca), pp, pa, difference(pp, pa)];
    })), [8, 9, 10, 11, 12, 13], 3) + 2;
  title(effects, row++, 'Contexts ranked within each study / method / security strategy');
  reportTable(effects, [...identities, 'Context', 'n', 'Compile%', 'Pass% (measured)', 'Pass% (all runs)'],
    groups.flatMap(g => paperContexts.map(context => {
      const entries = contextEntries(g, context);
      return [...identity(g), context, entries.length, compileRate(entries), functionalCounts(entries).rate, allRunPassRate(entries)];
    }).sort((a, b) => (b[8] === null ? -1 : Number(b[8])) - (a[8] === null ? -1 : Number(a[8])))), [7, 8, 9], row);

  const delivery = wb.addWorksheet('Delivery & Errors');
  title(delivery, 1, 'File and compiler-error counts are unavailable in the compact dataset; blank means unavailable.');
  reportTable(delivery, [...identities, 'Avg files delivered', 'n runs', 'Avg compile errors (failed runs)', 'n failed', 'Avg model submissions'], groups.map(g => [
    ...identity(g), null, g.entries.length, null, g.entries.filter(e => e.run.mainCompilation === 'fail').length,
    g.entries.length && g.entries.every(e => typeof e.run.submissions === 'number') ? g.entries.reduce((n, e) => n + e.run.submissions!, 0) / g.entries.length : null,
  ]), [], 3);

  reportTable(wb.addWorksheet('Raw Data'), ['Run', 'Prompt/ctx', 'Task', 'Method', 'Context', 'Compiled', 'Compile Err Type', '# Err', '# Files',
    'Unit P', 'Unit T', 'Invoked P', 'Invoked T', 'Auton P', 'Auton T', 'Combined P', 'Combined T', 'Combined %', 'Invoked Outcome', 'Autonomous Outcome',
    'Study', 'Security strategy', 'Run ID', 'Submissions', 'Fully functional', 'Functional unresolved (compiled)'], groups.flatMap(g => g.entries.map(e => {
      const counts = (Object.keys(functionalSuites) as FunctionalSuite[]).map(s => functionalCounts([e], s));
      const all = functionalCounts([e]);
      return [e.run.repetition, e.context, e.task, e.method, e.context,
        e.run.mainCompilation === 'pass' ? 'Yes' : e.run.mainCompilation === 'fail' ? 'No' : null,
        e.run.mainCompilation === 'fail' ? 'Unclassified' : null, e.run.mainCompilation === 'pass' ? 0 : null, null,
        ...counts.flatMap(c => [c.evaluated ? c.passed : null, c.evaluated || null]), all.evaluated ? all.passed : null, all.evaluated || null, all.rate,
        suiteOutcome(e, 'invoked'), suiteOutcome(e, 'autonomous'), e.study, e.security, e.run.runId, e.run.submissions ?? null,
        e.run.functionalSuccess === null ? null : e.run.functionalSuccess ? 'Yes' : 'No', e.run.mainCompilation === 'pass' ? all.unresolved : null];
    })), [18]);
  addSecurityAndProvenance(wb, data);
  return wb;
}

function addSecurityAndProvenance(wb: ExcelJS.Workbook, data: MatrixData) {
  reportTable(wb.addWorksheet('Security Issues'), ['Study', 'Task', 'Method', 'Security strategy', 'Paper context', 'Condition', 'N',
    'Issue failures', 'Evaluated issue checks', 'Unresolved issue checks', 'Expected issue checks', 'Delta (equal coverage)',
    'Delta lower bound', 'Delta upper bound'], combinationRows(data).map(r => [r.study, 'Highscore', r.method, r.security,
      r.paper.join('+') || 'None', r.condition, r.values.attempts, r.issues.detected, r.issues.evaluated,
      r.issues.expected - r.issues.evaluated, r.issues.expected, r.issues.delta, r.issues.deltaBounds?.[0] ?? null, r.issues.deltaBounds?.[1] ?? null]));

  const securityRows: Value[][] = [];
  for (const study of data.studies) for (const condition of study.plan.conditions) {
    const summary = study.summary.conditions.find(c => c.id === condition.id)!;
    for (const check of summary.checks.filter(c => c.suite === 'security_v1')) securityRows.push([
      study.plan.id, 'Highscore', condition.strategy, condition.securityStrategy, condition.baseContext, condition.id, check.name,
      check.name === 'validRecordRoundTrip' ? 'No (positive persistence)' : 'Yes', summary.attempts,
      check.pass, check.fail, check.executed, summary.attempts - check.executed, ratio(check.fail, check.executed),
      check.not_run, check.unknown, check.compile_error, check.infrastructure_error,
    ]);
  }
  reportTable(wb.addWorksheet('Security Checks'), ['Study', 'Task', 'Method', 'Security strategy', 'Paper context', 'Condition', 'Test',
    'Counts as issue check', 'N', 'Passed', 'Failed', 'Evaluated', 'Unresolved', 'Fail rate', 'Not run', 'Unknown', 'Compile error', 'Infrastructure error'], securityRows, [14], 1, [14]);

  const definitions: Value[][] = [
    ['All', 'Report layout', 'Based on experiment_results_report.xlsx. Each study, method and security strategy is kept separate. Only recorded Highscore observations are exported.'],
    ['All', 'N', 'Recorded code-generation attempts or trajectories, including failed and interrupted attempts. Unstarted planned runs are not observations.'],
    ['All', 'Compile rate', 'Successful whole-game compilations / all recorded runs. A missing compilation is not counted as a compiler diagnostic failure.'],
    ['All', 'Pass rate', 'Passed / evaluated functional checks on compiled runs, pooled from counts. Only pass and fail are evaluated. Unknown and unexecuted checks are excluded, with coverage recorded.'],
    ['All', 'All-run pass rate', 'Passed functional checks / (16 × all recorded runs). This differs from the measured-check rate and preserves the cost of incomplete delivery.'],
    ['All', 'Test tiers', '7 unit, 4 invoked integration (coupling), and 5 autonomous integration (wiring) checks. Full functionality requires all 16.'],
    ['All', 'Empty cells', 'No applicable observations or unavailable metadata. Empty rates are not zero; unresolved outcomes do not establish passes.'],
    ['All', 'Missing metadata', 'Delivered-file counts, compiler diagnostic counts and compiler error classifications are not included in the compact dataset. Blank fields and Unclassified preserve this limitation.'],
    ['All', 'Context effect', 'Descriptive present/absent comparisons within each study, method and security strategy. Selected contexts may be unbalanced; these differences are not isolated causal effects.'],
    ['All', 'Reuse vs Generation', 'Available context combinations can differ between methods. Overall method rates are descriptive, not matched estimates of a reuse effect.'],
    ['All', 'Security issues', 'Failed observations of ten fixed issue contracts, not distinct vulnerabilities or CVEs. The positive validRecordRoundTrip check is excluded.'],
    ['All', 'Security differences', 'Treatment minus its fresh control at equal N. Bounds allow every unresolved check to pass or fail; they are not confidence intervals. The equal-coverage delta is blank when per-check coverage differs.'],
    ['All', 'Measurement qualification', 'The exported snapshot determines qualification. Unsupported large-record outcomes remain unknown where qualified data is supplied; original reports are retained separately.'],
    ['All', 'Repetitions', 'Code repetitions may share one acquired context. Repair submissions and checks within an artifact are not independent context samples.'],
    ['All', 'Numeric precision', 'Rates are numeric fractions formatted as percentages, without rounding stored values. Delta percentages represent percentage-point differences when multiplied by 100.'],
    ['All', 'Diagnostics', 'Most common failure is an observed diagnostic, not an independently validated root cause. Messages exceeding Excel cell limits are marked excerpts; full text remains in the detailed export.'],
  ];
  for (const study of data.studies) {
    const id = study.plan.id, qualification = study.measurementQualification;
    definitions.push([id, 'Manifest SHA-256', study.plan.fingerprint], [id, 'Requested model', study.plan.model],
      [id, 'Requested reasoning', study.plan.reasoning], [id, 'Collection complete', study.summary.complete ? 'Yes' : 'No'],
      [id, 'Observation unit', study.plan.observationUnit ?? 'attempt'], [id, 'Submission cap', study.plan.maxSubmissions ?? 1],
      [id, 'Runs with unattested settings', study.summary.conditions.reduce((n, c) => n + c.unverifiedSettings, 0)],
      [id, 'Qualification protocol', qualification?.protocol ?? 'No qualification metadata in this snapshot'],
      [id, 'Qualification audit SHA-256', qualification?.auditSha256 ?? null],
      [id, 'Qualifier SHA-256', qualification?.qualifierSha256 ?? null],
      [id, 'Changed measurements', qualification?.changes.length ?? null]);
    for (const acquisition of study.plan.acquisitions ?? []) definitions.push([id, `Context ${acquisition.id}`,
      `${acquisition.method} / ${acquisition.strategy}; ${acquisition.items} items; repository snapshot ${acquisition.snapshotFingerprint}; task ${acquisition.taskSha256}`]);
    for (const note of study.plan.deviations) definitions.push([id, 'Protocol note', note]);
  }
  const provenance = wb.addWorksheet('Provenance');
  reportTable(provenance, ['Study', 'Field', 'Meaning / value'], definitions);
  provenance.views = [{ state: 'frozen', ySplit: 1 }];
  provenance.getColumn(2).width = 36;
  provenance.getColumn(3).width = 110;
}
