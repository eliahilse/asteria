import { test, expect } from '@playwright/test';
import { readFile } from 'node:fs/promises';
import ExcelJS from 'exceljs';

test('downloads the report layout separately from the detailed XLSX', async ({ page }) => {
  const data = JSON.parse(await readFile('../research/iterations/i07-operational-replication/qualified-results.json', 'utf8'));
  await page.route('**/api/experiment', route => route.fulfill({ json: data }));
  await page.goto('/?view=experiment');
  await expect(page.getByRole('button', { name: 'Export XLSX', exact: true })).toBeVisible();
  await expect(page.getByRole('button', { name: 'Export JSON', exact: true })).toBeVisible();
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export report XLSX', exact: true }).click();
  const file = await download;
  expect(file.suggestedFilename()).toBe('experiment_results_report.xlsx');
  const workbook = new ExcelJS.Workbook();
  await workbook.xlsx.readFile((await file.path())!);
  expect(workbook.getWorksheet('Compile Rate')).toBeDefined();
  // Counts first (docs/REPORTING.md): k/N text beside the numeric fraction; N = 5 cells carry no percentage format.
  expect(workbook.getWorksheet('Compile Rate')!.getRow(1).values).toContain('S (fraction)');
  const reuseB = workbook.getWorksheet('Compile Rate')!.getRows(2, 9)!.find(r => r.getCell(3).value === 'Reuse' && r.getCell(4).value === 'none')!;
  expect(reuseB.getCell(11).value).toBe('4/5');
  expect(reuseB.getCell(12).value).toBe(0.8);
  expect(reuseB.getCell(12).numFmt).not.toBe('0.0%');
  const overview = workbook.getWorksheet('Overview')!;
  expect(overview.getRow(11).values).toEqual([undefined, 'Overall compile', '78/80', 0.975]);
  expect(overview.getRow(11).getCell(3).numFmt).toBe('0.0%');
  expect(workbook.getWorksheet('Per-Test Breakdown')).toBeDefined();
  expect(workbook.getWorksheet('Raw Data')!.rowCount).toBe(81);
  expect(workbook.getWorksheet('Security Issues')!.rowCount).toBe(17);
  expect(workbook.getWorksheet('Security Checks')!.rowCount).toBe(177);
  const names = workbook.worksheets.map(s => s.name);
  expect(names).toHaveLength(15);
  expect(names.slice(-3)).toEqual(['Security Checks', 'Issue Matrix', 'Provenance']);
  expect(workbook.getWorksheet('Issue Matrix')!.rowCount).toBe(54);
  expect(workbook.getWorksheet('Issue Matrix')!.getRow(13).getCell(2).value).toBe('Total (10 issue checks)');
  expect(workbook.getWorksheet('Provenance')).toBeDefined();
  await page.setViewportSize({ width: 390, height: 844 });
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
});
