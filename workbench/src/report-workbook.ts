import ExcelJS from 'exceljs';
import type { MatrixData } from './experiment-types';
import { PERCENT_MIN_N, combinationRows, fraction, fractionText, fractionValue, issueTestsFor, percentAllowed, positiveTestFor, type Fraction } from './combination-stats';
import { allRunPassCounts, compileCounts, compiledEntries, fullCounts, functionalCounts, functionalSuites,
  mostCommonFailure, paperContexts, passCounts, reportGroups, suiteOutcome,
  type FunctionalSuite, type ReportEntry, type ReportGroup } from './report-data';

type Value = string | number | null;
/** A table cell: a plain value, or a k/N Fraction that expands into a text cell and an adjacent numeric fraction. */
type Cell = Value | Fraction;
const isFraction = (cell: Cell): cell is Fraction => typeof cell === 'object' && cell !== null;
/** Header pair for a counted rate: the `k/N` text column and its numeric fraction (docs/REPORTING.md). */
export const counted = (label: string) => [label, `${label} (fraction)`];
/** Optional inputs that are not part of the observation dataset. */
export type ReportOptions = { cweMapping?: Record<string, string[]> };
const blue = 'FF1F4E79', purple = 'FF6B4585';
const border: ExcelJS.Border = { style: 'thin', color: { argb: 'FFD9D9D9' } };
const identities = ['Study', 'Task', 'Method', 'Security strategy'];
const identity = (g: ReportGroup): Value[] => [g.study.plan.id, g.task, g.method, g.security];
const tierNames = { unit: 'Unit', invoked: 'Invoked (coupling)', autonomous: 'Autonomous (wiring)' };
// Headline tiers: the four invoked-integration checks count toward Full (all 16) and stay in the
// per-test and raw sheets, but are no longer summarised as a separate tier.
const headlineTiers = { unit: tierNames.unit, autonomous: tierNames.autonomous };
const unresolvedReasons = {
  not_run: 'check not executed',
  unknown: 'precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown',
  compile_error: 'final artifact failed the security-suite compilation',
  infrastructure_error: 'harness failure',
} as const;
const contextEntries = (g: ReportGroup, context: string) => g.entries.filter(e => e.context === context);
const difference = (a: number | null, b: number | null) => a === null || b === null ? null : a - b;
const trajectoryUnit = 'Observation unit: trajectory. Each cell is compiled / N recorded trajectories in that cell (N is the denominator shown). The numeric fraction beside it is formatted as a percentage only when N is at least ' + PERCENT_MIN_N + '; below that the counts are the result (docs/REPORTING.md).';
const checkUnit = 'Observation unit: check on a compiled run. Each cell is passed / evaluated checks, with unresolved checks listed beside the count (they are not passes). The numeric fraction is formatted as a percentage only when the cell rests on at least ' + PERCENT_MIN_N + ' trajectories; checks on one artifact are correlated and do not count as independent observations (docs/REPORTING.md).';

/**
 * Writes one formatted table. A Fraction cell becomes two columns, `k/N` text then the numeric
 * fraction, so counts are never replaced by a percentage; the header list must include both columns
 * (see `counted`). The 0.0% number format applies only where the independent observations behind the
 * count (trajectories, not correlated checks) reach PERCENT_MIN_N.
 */
export function reportTable(sheet: ExcelJS.Worksheet, headers: string[], rows: Cell[][], start = 1, lowerIsBetter: string[] = []) {
  const expanded = rows.map(row => row.flatMap(cell => isFraction(cell) ? [fractionText(cell), fractionValue(cell)] : [cell]));
  const mismatch = expanded.find(row => row.length !== headers.length);
  if (mismatch) throw new Error(`Row has ${mismatch.length} cells for ${headers.length} headers`);
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
  const fractionColumns = new Set<number>();
  rows.forEach((source, i) => {
    const row = sheet.getRow(start + i + 1);
    row.values = expanded[i];
    headers.forEach((_, j) => {
      const cell = row.getCell(j + 1);
      cell.font = { name: 'Arial', size: 10 };
      cell.border = { top: border, bottom: border, left: border, right: border };
      cell.alignment = { vertical: 'top', horizontal: typeof cell.value === 'number' ? 'center' : 'left', wrapText: true };
      if (i % 2 === 0) cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF2F2F2' } };
      if (typeof cell.value === 'string' && cell.value.length > 30000) {
        cell.value = cell.value.slice(0, 29800) + '\n[Excerpt; full diagnostic remains in the detailed export.]';
      }
    });
    let column = 1;
    for (const cell of source) {
      if (isFraction(cell)) {
        row.getCell(column).alignment = { vertical: 'top', horizontal: 'center', wrapText: true };
        column += 1;
        fractionColumns.add(column);
        if (percentAllowed(cell)) row.getCell(column).numFmt = '0.0%';
      }
      column += 1;
    }
  });
  for (const column of fractionColumns) sheet.addConditionalFormatting({
    ref: `${sheet.getCell(start + 1, column).address}:${sheet.getCell(start + rows.length, column).address}`,
    rules: [{ type: 'colorScale', priority: start * headers.length + column,
      cfvo: [{ type: 'num', value: 0 }, { type: 'num', value: 0.5 }, { type: 'num', value: 1 }],
      color: (lowerIsBetter.some(label => headers[column - 1] === `${label} (fraction)`) ? ['FF63BE7B', 'FFFFEB84', 'FFF8696B'] : ['FFF8696B', 'FFFFEB84', 'FF63BE7B']).map(argb => ({ argb })) }],
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

/** A caption stating the table's observation unit and denominator (docs/REPORTING.md). */
function caption(sheet: ExcelJS.Worksheet, row: number, text: string) {
  sheet.getCell(row, 1).value = text;
  sheet.getCell(row, 1).font = { name: 'Arial', size: 9, italic: true, color: { argb: 'FF595959' } };
  sheet.getCell(row, 1).alignment = { vertical: 'top', horizontal: 'left' };
  return row + 1;
}

function pivot(sheet: ExcelJS.Worksheet, groups: ReportGroup[], metric: (entries: ReportEntry[]) => Fraction, start = 1) {
  const rows = groups.map(g => [...identity(g), ...paperContexts.map(c => metric(contextEntries(g, c))), metric(g.entries)]);
  return reportTable(sheet, [...identities, ...paperContexts.flatMap(counted), ...counted('Overall')], rows, start);
}

function catalog(group: ReportGroup) {
  return [...new Map(group.study.summary.conditions.flatMap(c => c.checks.filter(t => Object.hasOwn(functionalSuites, t.suite))).map(t => [t.id, t])).values()];
}

/** The supplied report's eleven-sheet layout plus security sheets, populated only from current observations. */
export function reportWorkbook(data: MatrixData, options: ReportOptions = {}) {
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
    // Counts first: the summary rows carry k/N text, with the numeric fraction in the third column.
    row = reportTable(overview, ['Experiment Summary', study.plan.id, 'Fraction'], [
      ['Total runs', entries.length, null], ['Prompt variants', study.plan.conditions.length, null],
      ['Runs per prompt (planned)', repetitions.length === 1 ? repetitions[0] : 'Varies', null],
      ['Model', study.plan.model, null], ['Temperature (requested)', plan.settings?.temperature ?? plan.temperature ?? null, null],
      ['Submission cap', study.plan.maxSubmissions ?? 1, null], ['Collection complete', study.summary.complete ? 'Yes' : 'No', null],
      ['Overall compile', compileCounts(entries)], ['Overall test pass (measured checks on compiled runs)', passCounts(entries)],
      ['Fully functional / all runs', fullCounts(entries)], ['Unresolved functional checks on compiled runs', functionalCounts(entries).unresolved, null],
      ['Observation unit', study.plan.observationUnit ?? 'attempt', null],
    ], row);
    row += 2;
    title(overview, row++, 'Test Pass Rate by Test Type');
    row = caption(overview, row, checkUnit);
    row = reportTable(overview, ['Test Type', ...counted('Pass rate'), 'Unresolved'],
      Object.entries(headlineTiers).map(([suite, name]) => [name, passCounts(entries, suite as FunctionalSuite), functionalCounts(entries, suite as FunctionalSuite).unresolved]), row) + 2;
  }
  title(overview, row++, 'Per-Task / Method / Security Summary');
  row = caption(overview, row, 'Observation unit: trajectory for Compile (compiled / n), check for Test Pass (passed / evaluated on compiled runs). n = recorded trajectories in the group.');
  reportTable(overview, [...identities, ...counted('Compile'), ...counted('Test Pass'), 'Compile Fails', 'n'], groups.map(g => [
    ...identity(g), compileCounts(g.entries), passCounts(g.entries),
    g.entries.filter(e => e.run.mainCompilation === 'fail').length, g.entries.length,
  ]), row);

  const compileSheet = wb.addWorksheet('Compile Rate');
  caption(compileSheet, pivot(compileSheet, groups, compileCounts) + 1, trajectoryUnit);
  const passSheet = wb.addWorksheet('Pass Rate');
  caption(passSheet, pivot(passSheet, groups, entries => passCounts(entries)) + 1, checkUnit);

  const methods = wb.addWorksheet('Reuse vs Generation');
  title(methods, 1, 'Combined functional pass count (passed / evaluated checks on compiled runs)');
  row = pivot(methods, groups, entries => passCounts(entries), caption(methods, 2, checkUnit)) + 2;
  title(methods, row++, 'Compiled / all recorded runs by context');
  pivot(methods, groups, compileCounts, caption(methods, row, trajectoryUnit));

  const failures = wb.addWorksheet('Compile Failure Analysis');
  reportTable(failures, [...identities, 'Primary error type', '# failed runs', ...counted('Compile fail / all runs'), 'Meaning'],
    groups.filter(g => g.entries.some(e => e.run.mainCompilation === 'fail')).map(g => {
      const n = g.entries.filter(e => e.run.mainCompilation === 'fail').length;
      return [...identity(g), 'Unclassified', n, fraction(n, g.entries.length), 'Compiler classification is not provided by the compact dataset; see archived compiler logs.'];
    }), 1, ['Compile fail / all runs']);

  const testFailures: Cell[][] = [];
  for (const g of groups) for (const test of catalog(g)) {
    const c = functionalCounts(g.entries, test.suite as FunctionalSuite, test.name);
    if (c.failed) testFailures.push([...identity(g), tierNames[test.suite as FunctionalSuite], test.name, c.failed,
      c.evaluated, compiledEntries(g.entries).length, c.unresolved, fraction(c.failed, c.evaluated), mostCommonFailure(g.entries, test.suite, test.name)]);
  }
  testFailures.sort((a, b) => Number(b[6]) - Number(a[6]));
  reportTable(wb.addWorksheet('Test Failure Analysis'), [...identities, 'Tier', 'Test', '# fails', '# tested', '# compiled', '# unresolved', ...counted('Fail rate'), 'Most common failure'], testFailures, 1, ['Fail rate']);

  const perTest = wb.addWorksheet('Per-Test Breakdown');
  const perTestRows: Cell[][] = [];
  const perTestNotes: string[][] = [];
  for (const g of groups) for (const test of catalog(g)) {
    const cells = [...paperContexts.map(c => contextEntries(g, c)), g.entries];
    perTestRows.push([...identity(g), tierNames[test.suite as FunctionalSuite], test.name, ...cells.map(entries => passCounts(entries, test.suite as FunctionalSuite, test.name))]);
    perTestNotes.push(cells.map(entries => { const c = functionalCounts(entries, test.suite as FunctionalSuite, test.name); return `${c.passed} passed / ${c.evaluated} evaluated; ${c.failed} failed; ${c.unresolved} unresolved on compiled runs.`; }));
  }
  reportTable(perTest, [...identities, 'Tier', 'Test', ...paperContexts.flatMap(counted), ...counted('Overall')], perTestRows);
  // The unit statement sits on the k/N header cells (a caption below 128 rows would go unread), and every
  // k/N cell keeps its full count breakdown as a note; the pair for context j starts at column 7 + 2j.
  for (let j = 0; j <= paperContexts.length; j++) perTest.getCell(1, 7 + 2 * j).note = checkUnit;
  perTestNotes.forEach((notes, i) => notes.forEach((note, j) => { perTest.getCell(i + 2, 7 + 2 * j).note = note; }));

  const tiers = wb.addWorksheet('Tier Breakdown');
  row = 1;
  for (const [suite, name] of Object.entries(headlineTiers)) {
    title(tiers, row++, name);
    row = pivot(tiers, groups, entries => passCounts(entries, suite as FunctionalSuite), caption(tiers, row, checkUnit)) + 2;
  }

  const effects = wb.addWorksheet('Context Effect');
  title(effects, 1, 'Descriptive presence/absence comparison; selected contexts may be unbalanced.');
  row = caption(effects, 2, 'Observation unit: trajectory for compile counts, check for pass counts. Delta columns are fraction differences (present minus absent), not counts, because n present and n absent differ.');
  row = reportTable(effects, [...identities, 'Context type', 'n present', 'n absent', ...counted('Compile present'), ...counted('Compile absent'), 'Delta compile (fraction difference)',
    ...counted('Pass present'), ...counted('Pass absent'), 'Delta pass (fraction difference)'],
    groups.flatMap(g => ['S', 'F', 'B'].map(context => {
      const present = g.entries.filter(e => e.context.split('+').includes(context));
      const absent = g.entries.filter(e => !e.context.split('+').includes(context));
      const cp = compileCounts(present), ca = compileCounts(absent), pp = passCounts(present), pa = passCounts(absent);
      return [...identity(g), context, present.length, absent.length, cp, ca, difference(fractionValue(cp), fractionValue(ca)), pp, pa, difference(fractionValue(pp), fractionValue(pa))];
    })), row) + 2;
  title(effects, row++, 'Contexts ranked within each study / method / security strategy');
  row = caption(effects, row, 'Observation unit: trajectory (n, Compile), check (Pass (measured): passed / evaluated on compiled runs; Pass (all runs): passed / 16 × n). Ranked by Pass (all runs).');
  reportTable(effects, [...identities, 'Context', 'n', ...counted('Compile'), ...counted('Pass (measured)'), ...counted('Pass (all runs)')],
    groups.flatMap(g => paperContexts.map(context => {
      const entries = contextEntries(g, context);
      return [...identity(g), context, entries.length, compileCounts(entries), passCounts(entries), allRunPassCounts(entries)];
    }).sort((a, b) => (fractionValue(b[8] as Fraction) ?? -1) - (fractionValue(a[8] as Fraction) ?? -1))), row);

  const delivery = wb.addWorksheet('Delivery & Errors');
  title(delivery, 1, 'File and compiler-error counts are unavailable in the compact dataset; blank means unavailable.');
  reportTable(delivery, [...identities, 'Avg files delivered', 'n runs', 'Avg compile errors (failed runs)', 'n failed', 'Avg model submissions'], groups.map(g => [
    ...identity(g), null, g.entries.length, null, g.entries.filter(e => e.run.mainCompilation === 'fail').length,
    g.entries.length && g.entries.every(e => typeof e.run.submissions === 'number') ? g.entries.reduce((n, e) => n + e.run.submissions!, 0) / g.entries.length : null,
  ]), 3);

  // Per-run counts: the P / T columns are the numerator and denominator; the combined fraction is never a percentage (T ≤ 16).
  reportTable(wb.addWorksheet('Raw Data'), ['Run', 'Prompt/ctx', 'Task', 'Method', 'Context', 'Compiled', 'Compile Err Type', '# Err', '# Files',
    'Unit P', 'Unit T', 'Invoked P', 'Invoked T', 'Auton P', 'Auton T', 'Combined P', 'Combined T', 'Combined (fraction)', 'Invoked Outcome', 'Autonomous Outcome',
    'Study', 'Security strategy', 'Run ID', 'Submissions', 'Fully functional', 'Functional unresolved (compiled)'], groups.flatMap(g => g.entries.map(e => {
      const counts = (Object.keys(functionalSuites) as FunctionalSuite[]).map(s => functionalCounts([e], s));
      const all = functionalCounts([e]);
      return [e.run.repetition, e.context, e.task, e.method, e.context,
        e.run.mainCompilation === 'pass' ? 'Yes' : e.run.mainCompilation === 'fail' ? 'No' : null,
        e.run.mainCompilation === 'fail' ? 'Unclassified' : null, e.run.mainCompilation === 'pass' ? 0 : null, null,
        ...counts.flatMap(c => [c.evaluated ? c.passed : null, c.evaluated || null]), all.evaluated ? all.passed : null, all.evaluated || null, all.rate,
        suiteOutcome(e, 'invoked'), suiteOutcome(e, 'autonomous'), e.study, e.security, e.run.runId, e.run.submissions ?? null,
        e.run.functionalSuccess === null ? null : e.run.functionalSuccess ? 'Yes' : 'No', e.run.mainCompilation === 'pass' ? all.unresolved : null];
    })));
  addSecurityAndProvenance(wb, data, options);
  return wb;
}

/**
 * Every issue check against its fixed denominator: N per check and 10 × N for the total, split into
 * failed / unresolved / passed. Unresolved outcomes are not passes; a second table names their reason.
 */
function addIssueMatrix(wb: ExcelJS.Workbook, data: MatrixData, cweMapping: Record<string, string[]>) {
  const sheet = wb.addWorksheet('Issue Matrix');
  type Summary = MatrixData['studies'][number]['summary']['conditions'][number];
  const find = (summary: Summary, name: string) => summary.checks.find(c => c.suite === 'security_v1' && c.name === name);
  const cells = (summary: Summary, name: string): Value[] => {
    const check = find(summary, name);
    return check && summary.attempts ? [check.fail, summary.attempts - check.executed, check.pass] : [null, null, null];
  };
  const cwe = (name: string) => (cweMapping[name] ?? []).join(', ');
  const meaning = (check: NonNullable<ReturnType<typeof find>>, n: number) => (Object.keys(unresolvedReasons) as (keyof typeof unresolvedReasons)[])
    .filter(reason => check[reason] > 0).map(reason => `${reason} (${check[reason]}): ${unresolvedReasons[reason]}`).join('; ')
    + `. Unresolved outcomes are not passes; they remain in the fixed denominator N = ${n}.`;
  let row = 1;
  for (const study of data.studies) {
    const id = study.plan.id; const issueTests = issueTestsFor(study.plan); const positiveTest = positiveTestFor(study.plan);
    const conditions = study.plan.conditions.map(c => ({ id: c.id, summary: study.summary.conditions.find(s => s.id === c.id)! }));
    const headers = ['Study', 'Test', 'CWE', ...conditions.flatMap(c => [`${c.id} failed`, `${c.id} unresolved`, `${c.id} passed`])];
    const total = (summary: Summary): Value[] => {
      const parts = issueTests.map(name => cells(summary, name));
      return parts.some(p => p[0] === null) ? [null, null, null] : [0, 1, 2].map(i => parts.reduce((sum, p) => sum + Number(p[i]), 0));
    };
    const rows: Value[][] = [
      [id, 'N per check', '', ...conditions.flatMap(c => Array<Value>(3).fill(c.summary.attempts || null))],
      ...issueTests.map(name => [id, name, cwe(name), ...conditions.flatMap(c => cells(c.summary, name))]),
      [id, 'Total (10 issue checks)', '', ...conditions.flatMap(c => total(c.summary))],
      Array<Value>(headers.length).fill(null),
      [id, `${positiveTest} (positive persistence check, not an issue)`, cwe(positiveTest), ...conditions.flatMap(c => cells(c.summary, positiveTest))],
    ];
    const start = row;
    row = reportTable(sheet, headers, rows, start);
    sheet.getRow(start).height = 48;
    conditions.forEach((c, i) => { for (let k = 0; k < 3; k++) sheet.getCell(start, 4 + 3 * i + k).note =
      `${c.id}: N = ${c.summary.attempts} per check; 10×N = ${10 * c.summary.attempts} for the total row. failed + unresolved + passed = N; unresolved outcomes are not passes.`; });
    const totalRow = start + issueTests.length + 2, separator = totalRow + 1, positive = totalRow + 2;
    headers.forEach((_, j) => {
      sheet.getCell(totalRow, j + 1).font = { name: 'Arial', size: 10, bold: true };
      sheet.getCell(separator, j + 1).border = {};
      sheet.getCell(separator, j + 1).fill = { type: 'pattern', pattern: 'none' };
      sheet.getCell(positive, j + 1).font = { name: 'Arial', size: 10, italic: true };
      sheet.getCell(positive, j + 1).border = { top: { style: 'medium', color: { argb: 'FF7F7F7F' } }, bottom: border, left: border, right: border };
    });
    sheet.getRow(separator).height = 8;
    row += 2;
    title(sheet, row++, `Unresolved reasons (${id})`);
    const reasons: Value[][] = conditions.flatMap(c => [...issueTests, positiveTest].flatMap(name => {
      const check = find(c.summary, name), unresolved = check ? c.summary.attempts - check.executed : 0;
      return check && unresolved > 0 ? [[id, c.id, name, unresolved, check.not_run, check.unknown, check.compile_error, check.infrastructure_error, meaning(check, c.summary.attempts)]] : [];
    }));
    row = reportTable(sheet, ['Study', 'Condition', 'Test', 'Unresolved', 'not_run', 'unknown', 'compile_error', 'infrastructure_error', 'Meaning'], reasons, row) + 2;
  }
  sheet.views = [{ state: 'frozen', xSplit: 3, ySplit: 1 }];
  sheet.autoFilter = undefined as unknown as ExcelJS.Worksheet['autoFilter'];
  sheet.getColumn(3).width = 30;
  sheet.getColumn(9).width = 60;
}

function addSecurityAndProvenance(wb: ExcelJS.Workbook, data: MatrixData, options: ReportOptions) {
  reportTable(wb.addWorksheet('Security Issues'), ['Study', 'Task', 'Method', 'Security strategy', 'Paper context', 'Condition', 'N',
    'Issue failures', 'Evaluated issue checks', 'Unresolved issue checks', 'Expected issue checks', 'Delta (equal coverage)',
    'Delta lower bound', 'Delta upper bound'], combinationRows(data).map(r => [r.study, 'Highscore', r.method, r.security,
      r.paper.join('+') || 'None', r.condition, r.attempts, r.issues.detected, r.issues.evaluated,
      r.issues.expected - r.issues.evaluated, r.issues.expected, r.issues.delta, r.issues.deltaBounds?.[0] ?? null, r.issues.deltaBounds?.[1] ?? null]));

  const securityRows: Cell[][] = [];
  for (const study of data.studies) for (const condition of study.plan.conditions) {
    const summary = study.summary.conditions.find(c => c.id === condition.id)!;
    for (const check of summary.checks.filter(c => c.suite === 'security_v1')) securityRows.push([
      study.plan.id, 'Highscore', condition.strategy, condition.securityStrategy, condition.baseContext, condition.id, check.name,
      check.name === 'validRecordRoundTrip' ? 'No (positive persistence)' : 'Yes', summary.attempts,
      check.pass, check.fail, check.executed, summary.attempts - check.executed, fraction(check.fail, check.executed),
      check.not_run, check.unknown, check.compile_error, check.infrastructure_error,
    ]);
  }
  reportTable(wb.addWorksheet('Security Checks'), ['Study', 'Task', 'Method', 'Security strategy', 'Paper context', 'Condition', 'Test',
    'Counts as issue check', 'N', 'Passed', 'Failed', 'Evaluated', 'Unresolved', ...counted('Fail rate'), 'Not run', 'Unknown', 'Compile error', 'Infrastructure error'], securityRows, 1, ['Fail rate']);
  addIssueMatrix(wb, data, options.cweMapping ?? {});

  const definitions: Value[][] = [
    ['All', 'Report layout', 'Based on experiment_results_report.xlsx. Each study, method and security strategy is kept separate. Only recorded Highscore observations are exported.'],
    ['All', 'Reporting standard', `docs/REPORTING.md: counts first. Every rate is a k/N text cell with its numeric fraction in the adjacent "(fraction)" column; the fraction carries the 0.0% format only when the count rests on at least ${PERCENT_MIN_N} trajectories (checks on one artifact are not independent observations), and a percentage never replaces the counts. Each table states its observation unit in a caption or header.`],
    ['All', 'N', 'Recorded code-generation attempts or trajectories, including failed and interrupted attempts. Unstarted planned runs are not observations.'],
    ['All', 'Compile rate', 'Successful whole-game compilations / all recorded runs. A missing compilation is not counted as a compiler diagnostic failure.'],
    ['All', 'Pass rate', 'Passed / evaluated functional checks on compiled runs, pooled from counts. Only pass and fail are evaluated. Unknown and unexecuted checks are excluded from the denominator but listed beside every count as unresolved; they are never passes.'],
    ['All', 'All-run pass rate', 'Passed functional checks / (16 × all recorded runs). This differs from the measured-check rate and preserves the cost of incomplete delivery.'],
    ['All', 'Test tiers', '7 unit, 4 invoked integration (coupling), and 5 autonomous integration (wiring) checks. Full functionality requires all 16. The four invoked-integration checks count toward full functionality but are not reported as a separate tier; they remain in the Per-Test Breakdown, Test Failure Analysis and Raw Data sheets.'],
    ['All', 'Empty cells', 'No applicable observations or unavailable metadata. Empty rates are not zero; unresolved outcomes do not establish passes.'],
    ['All', 'Missing metadata', 'Delivered-file counts, compiler diagnostic counts and compiler error classifications are not included in the compact dataset. Blank fields and Unclassified preserve this limitation.'],
    ['All', 'Context effect', 'Descriptive present/absent comparisons within each study, method and security strategy. Selected contexts may be unbalanced; these differences are not isolated causal effects.'],
    ['All', 'Reuse vs Generation', 'Available context combinations can differ between methods. Overall method rates are descriptive, not matched estimates of a reuse effect.'],
    ['All', 'Security issues', 'Failed observations of ten fixed issue contracts, not distinct vulnerabilities or CVEs. The positive validRecordRoundTrip check is excluded.'],
    ['All', 'Issue matrix', 'Every issue check is reported against its fixed denominator: N per check and 10 × N for the total, split into failed / unresolved / passed. Unresolved = not run + unknown + compile error + infrastructure error; unresolved outcomes are not passes. The Unresolved reasons table names the reason for every unresolved cell. CWE cells are blank unless a mapping (research/security/cwe-mapping.json) is supplied at export time.'],
    ['All', 'Security differences', 'Treatment minus its fresh control at equal N. Bounds allow every unresolved check to pass or fail; they are not confidence intervals. The equal-coverage delta is blank when per-check coverage differs.'],
    ['All', 'Measurement qualification', 'The exported snapshot determines qualification. Unsupported large-record outcomes remain unknown where qualified data is supplied; original reports are retained separately.'],
    ['All', 'Repetitions', 'Code repetitions may share one acquired context. Repair submissions and checks within an artifact are not independent context samples.'],
    ['All', 'Numeric precision', 'Fractions are stored unrounded next to their k/N counts. Delta columns are fraction differences (percentage points when multiplied by 100) and carry no percentage format.'],
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
    for (const note of study.plan.deviations ?? []) definitions.push([id, 'Protocol note', note]);
  }
  const provenance = wb.addWorksheet('Provenance');
  reportTable(provenance, ['Study', 'Field', 'Meaning / value'], definitions);
  provenance.views = [{ state: 'frozen', ySplit: 1 }];
  provenance.getColumn(2).width = 36;
  provenance.getColumn(3).width = 110;
}
