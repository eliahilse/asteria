import { expect, it } from 'vitest';
import { readFile } from 'node:fs/promises';
import ExcelJS from 'exceljs';
import { reportWorkbook } from './report-workbook';
import type { MatrixData } from './experiment-types';

it('recreates the report sheets with numeric values, context columns, and blank untested cells', async () => {
  const data: MatrixData = JSON.parse(await readFile('../research/iterations/i07-operational-replication/qualified-results.json', 'utf8'));
  const loaded = new ExcelJS.Workbook();
  await loaded.xlsx.load(await reportWorkbook(data).xlsx.writeBuffer());
  expect(loaded.worksheets.map(s => s.name)).toEqual(['Overview', 'Compile Rate', 'Pass Rate', 'Reuse vs Generation', 'Compile Failure Analysis',
    'Test Failure Analysis', 'Per-Test Breakdown', 'Tier Breakdown', 'Context Effect', 'Delivery & Errors', 'Raw Data']);
  const compile = loaded.getWorksheet('Compile Rate')!;
  expect(compile.getRow(1).values).toEqual([undefined, 'Study', 'Task', 'Method', 'Security strategy', 'None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B', 'Overall']);
  const generation = compile.getRows(2, compile.rowCount - 1)!.find(r => r.getCell(3).value === 'Generation' && r.getCell(4).value === 'none')!;
  expect(generation.getCell(5).value).toBeNull();
  expect(generation.getCell(6).value).toBe(1);
  expect(generation.getCell(6).numFmt).toBe('0.0%');
  expect(compile.views[0].state).toBe('frozen');
  expect(loaded.getWorksheet('Raw Data')!.rowCount).toBe(81);
  expect(loaded.getWorksheet('Per-Test Breakdown')!.rowCount).toBe(129);
  const raw = loaded.getWorksheet('Raw Data')!;
  const failed = raw.getRows(2, raw.rowCount - 1)!.find(r => r.getCell(6).value === 'No')!;
  expect(failed.getCell(9).value).toBeNull();
  expect(failed.getCell(18).value).toBeNull();
  expect(failed.getCell(19).value).toBe('game compile-fail');
});

it('leaves planned-but-unobserved report rates blank', async () => {
  const data: MatrixData = JSON.parse(await readFile('public/data/original-matrix.json', 'utf8'));
  const wb = reportWorkbook(data);
  expect(wb.getWorksheet('Raw Data')!.rowCount).toBe(1);
  const compile = wb.getWorksheet('Compile Rate')!;
  compile.eachRow((row, i) => { if (i > 1) for (let column = 5; column <= 13; column++) expect(row.getCell(column).value).toBeNull(); });
});
