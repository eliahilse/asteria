import { expect, it } from 'vitest';
import { readFile } from 'node:fs/promises';
import ExcelJS from 'exceljs';
import { reportWorkbook } from './report-workbook';
import type { MatrixData } from './experiment-types';

it('recreates the report sheets with numeric values, context columns, and blank untested cells', async () => {
  const data: MatrixData = JSON.parse(await readFile('../research/iterations/i07-operational-replication/qualified-results.json', 'utf8'));
  const loaded = new ExcelJS.Workbook();
  await loaded.xlsx.load(await reportWorkbook(data, { cweMapping: { rejectsNullName: ['CWE-20', 'CWE-476'] } }).xlsx.writeBuffer());
  expect(loaded.worksheets.map(s => s.name)).toEqual(['Overview', 'Compile Rate', 'Pass Rate', 'Reuse vs Generation', 'Compile Failure Analysis',
    'Test Failure Analysis', 'Per-Test Breakdown', 'Tier Breakdown', 'Context Effect', 'Delivery & Errors', 'Raw Data', 'Security Issues', 'Security Checks', 'Issue Matrix', 'Provenance']);
  // Invoked-integration checks count toward Full and stay per-test, but are no longer a headline tier.
  const tierTitles = (name: string) => { const titles: string[] = []; loaded.getWorksheet(name)!.eachRow(r => { const v = r.getCell(1).value; if (typeof v === 'string' && /^(Unit|Invoked|Autonomous)/.test(v)) titles.push(v); }); return titles; };
  expect(tierTitles('Overview')).toEqual(['Unit', 'Autonomous (wiring)']);
  expect(tierTitles('Tier Breakdown')).toEqual(['Unit', 'Autonomous (wiring)']);
  const perTest = loaded.getWorksheet('Per-Test Breakdown')!;
  expect(perTest.getRows(2, perTest.rowCount - 1)!.some(r => r.getCell(5).value === 'Invoked (coupling)')).toBe(true);
  expect(loaded.getWorksheet('Raw Data')!.getRow(1).values).toContain('Invoked P');
  expect(loaded.getWorksheet('Raw Data')!.getRow(1).values).toContain('Combined (fraction)');
  // Counts first (docs/REPORTING.md): every rate is a k/N text cell with its numeric fraction beside it.
  const compile = loaded.getWorksheet('Compile Rate')!;
  const contexts = ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B', 'Overall'];
  expect(compile.getRow(1).values).toEqual([undefined, 'Study', 'Task', 'Method', 'Security strategy', ...contexts.flatMap(c => [c, `${c} (fraction)`])]);
  const generation = compile.getRows(2, compile.rowCount - 1)!.find(r => r.getCell(3).value === 'Generation' && r.getCell(4).value === 'none')!;
  expect(generation.getCell(5).value).toBeNull();
  expect(generation.getCell(6).value).toBeNull();
  expect(generation.getCell(7).value).toBe('5/5');
  expect(generation.getCell(8).value).toBe(1);
  // N = 5 is below the percentage threshold: the fraction stays a plain number.
  expect(generation.getCell(8).numFmt).not.toBe('0.0%');
  expect(generation.getCell(21).value).toBe('10/10');
  expect(generation.getCell(22).value).toBe(1);
  expect(compile.views[0].state).toBe('frozen');
  expect(compile.getCell(compile.rowCount, 1).value).toMatch(/^Observation unit: trajectory/);
  const overview = loaded.getWorksheet('Overview')!;
  const summary = (label: string) => { let found: ExcelJS.Row | undefined; overview.eachRow(r => { if (r.getCell(1).value === label) found = r; }); return found!; };
  expect(summary('Overall compile').getCell(2).value).toBe('78/80');
  expect(summary('Overall compile').getCell(3).value).toBeCloseTo(0.975);
  expect(summary('Overall compile').getCell(3).numFmt).toBe('0.0%');
  expect(summary('Fully functional / all runs').getCell(2).value).toBe('74/80');
  expect(summary('Unit').getCell(2).value).toBe('546/546');
  expect(summary('Unit').getCell(3).numFmt).toBe('0.0%');
  expect(summary('Unit').getCell(4).value).toBe(0);
  // 78/80 evaluated checks rest on five trajectories: counts, no percentage format.
  const pass = loaded.getWorksheet('Pass Rate')!;
  const reuseBoundaries = pass.getRows(2, pass.rowCount - 1)!.find(r => r.getCell(3).value === 'Reuse' && r.getCell(4).value === 'boundaries')!;
  expect(reuseBoundaries.getCell(11).value).toBe('78/80');
  expect(reuseBoundaries.getCell(12).value).toBeCloseTo(0.975);
  expect(reuseBoundaries.getCell(12).numFmt).not.toBe('0.0%');
  expect(overview.getRow(20).values).toEqual([undefined, 'Test Type', 'Pass rate', 'Pass rate (fraction)', 'Unresolved']);
  expect(perTest.getCell(1, 7).note).toMatch(/^Observation unit: check/);
  expect(perTest.getCell(2, 13).value).toBe('5/5');
  expect(perTest.getCell(2, 13).note).toBe('5 passed / 5 evaluated; 0 failed; 0 unresolved on compiled runs.');
  const effects = loaded.getWorksheet('Context Effect')!;
  expect(effects.getRow(3).values).toContain('Delta compile (fraction difference)');
  expect(effects.getRow(3).values).not.toContain('Compile% present');
  expect(loaded.getWorksheet('Raw Data')!.rowCount).toBe(81);
  expect(loaded.getWorksheet('Per-Test Breakdown')!.rowCount).toBe(129);
  const raw = loaded.getWorksheet('Raw Data')!;
  const failed = raw.getRows(2, raw.rowCount - 1)!.find(r => r.getCell(6).value === 'No')!;
  expect(failed.getCell(9).value).toBeNull();
  expect(failed.getCell(18).value).toBeNull();
  expect(failed.getCell(19).value).toBe('game compile-fail');
  const issues = loaded.getWorksheet('Security Issues')!;
  const requirements = issues.getRows(2, issues.rowCount - 1)!.find(r => r.getCell(6).value === 'reuse_sb__requirements')!;
  expect(requirements.getCell(8).value).toBe(0);
  expect(requirements.getCell(9).value).toBe(45);
  expect(requirements.getCell(10).value).toBe(5);
  expect(requirements.getCell(11).value).toBe(50);
  expect(requirements.getCell(12).value).toBeNull();
  expect(requirements.getCell(13).value).toBe(-33);
  expect(requirements.getCell(14).value).toBe(-27);
  expect(loaded.getWorksheet('Security Checks')!.rowCount).toBe(177);
  const provenance = loaded.getWorksheet('Provenance')!;
  const qualification = provenance.getRows(2, provenance.rowCount - 1)!.find(r => r.getCell(2).value === 'Changed measurements')!;
  expect(qualification.getCell(3).value).toBe(16);
  expect(provenance.getRows(2, provenance.rowCount - 1)!.find(r => r.getCell(2).value === 'Test tiers')!.getCell(3).value).toMatch(/not reported as a separate tier/);
  expect(provenance.getRows(2, provenance.rowCount - 1)!.find(r => r.getCell(2).value === 'Reporting standard')!.getCell(3).value).toMatch(/docs\/REPORTING\.md/);
  const checks = loaded.getWorksheet('Security Checks')!;
  expect(checks.getRow(1).values).toContain('Fail rate (fraction)');
  expect(checks.getRow(2).getCell(14).value).toBe('0/5');
  expect(checks.getRow(2).getCell(15).value).toBe(0);
});

it('reports every issue check against the fixed denominator with unresolved reasons', async () => {
  const data: MatrixData = JSON.parse(await readFile('../research/iterations/i07-operational-replication/qualified-results.json', 'utf8'));
  const loaded = new ExcelJS.Workbook();
  await loaded.xlsx.load(await reportWorkbook(data, { cweMapping: { rejectsNullName: ['CWE-20', 'CWE-476'] } }).xlsx.writeBuffer());
  const sheet = loaded.getWorksheet('Issue Matrix')!;
  const conditions = data.studies[0].plan.conditions.map(c => c.id);
  expect(sheet.getRow(1).values).toEqual([undefined, 'Study', 'Test', 'CWE', ...conditions.flatMap(c => [`${c} failed`, `${c} unresolved`, `${c} passed`])]);
  expect(sheet.views[0]).toMatchObject({ state: 'frozen', xSplit: 3, ySplit: 1 });
  const column = (condition: string, part: 'failed' | 'unresolved' | 'passed') => (sheet.getRow(1).values as string[]).indexOf(`${condition} ${part}`);
  const cells = (row: number, condition: string) => (['failed', 'unresolved', 'passed'] as const).map(part => sheet.getRow(row).getCell(column(condition, part)).value);
  expect(sheet.getRow(2).getCell(2).value).toBe('N per check');
  expect(cells(2, 'generation_s__none')).toEqual([5, 5, 5]);
  expect(sheet.getRows(3, 10)!.map(r => r.getCell(2).value)).toEqual(['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName',
    'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet']);
  expect(sheet.getRow(5).getCell(3).value).toBe('CWE-20, CWE-476');
  expect(sheet.getRow(3).getCell(3).value ?? '').toBe('');
  // Two unknown large-record outcomes are neither failures nor passes; the cell still sums to N.
  expect(cells(12, 'generation_s__none')).toEqual([3, 2, 0]);
  expect(cells(12, 'reuse_b__operations')).toEqual([0, 5, 0]);
  expect(sheet.getRow(13).getCell(2).value).toBe('Total (10 issue checks)');
  expect(cells(13, 'generation_s__none')).toEqual([38, 2, 10]);
  expect(cells(13, 'generation_s__operations')).toEqual([18, 4, 28]);
  expect(cells(13, 'reuse_sb__requirements')).toEqual([0, 5, 45]);
  for (const c of conditions) expect(cells(13, c).reduce((a, b) => Number(a) + Number(b), 0)).toBe(50);
  expect(sheet.getRow(14).getCell(2).value).toBeNull();
  expect(sheet.getRow(15).getCell(2).value).toBe('validRecordRoundTrip (positive persistence check, not an issue)');
  expect(cells(15, 'reuse_b__none')).toEqual([0, 1, 4]);
  expect(sheet.getCell(1, 4).note).toMatch(/N = 5 per check; 10×N = 50/);
  expect(sheet.getRow(19).values).toEqual([undefined, 'Study', 'Condition', 'Test', 'Unresolved', 'not_run', 'unknown', 'compile_error', 'infrastructure_error', 'Meaning']);
  const reasons = sheet.getRows(20, sheet.rowCount - 19)!.map(r => r.values as (string | number)[]);
  expect(reasons).toHaveLength(35);
  expect(reasons.every(r => Number(r[4]) === Number(r[5]) + Number(r[6]) + Number(r[7]) + Number(r[8]))).toBe(true);
  const compileError = reasons.find(r => r[2] === 'reuse_b__none' && r[3] === 'rejectsNullName')!;
  expect(compileError.slice(4, 9)).toEqual([1, 0, 0, 1, 0]);
  expect(compileError[9]).toMatch(/security-suite compilation/);
  const unknown = reasons.find(r => r[2] === 'reuse_sb__operations' && r[3] === 'largePersistedRecordSet')!;
  expect(unknown.slice(4, 9)).toEqual([5, 0, 5, 0, 0]);
  expect(unknown[9]).toMatch(/qualification audit/);
  expect(sheet.rowCount).toBe(54);
});

it('leaves planned-but-unobserved report rates blank', async () => {
  const data: MatrixData = JSON.parse(await readFile('public/data/original-matrix.json', 'utf8'));
  const wb = reportWorkbook(data);
  expect(wb.getWorksheet('Raw Data')!.rowCount).toBe(1);
  const compile = wb.getWorksheet('Compile Rate')!;
  compile.eachRow((row, i) => { if (i > 1 && typeof row.getCell(1).value !== 'string') for (let column = 5; column <= 22; column++) expect(row.getCell(column).value).toBeNull(); });
  const matrix = wb.getWorksheet('Issue Matrix')!;
  expect(matrix.getRow(13).getCell(2).value).toBe('Total (10 issue checks)');
  for (let column = 4; column <= matrix.columnCount; column++) expect(matrix.getRow(13).getCell(column).value).toBeNull();
  expect(matrix.getRow(18).getCell(1).value).toMatch(/^Unresolved reasons/);
  expect(matrix.rowCount).toBe(19);
});
