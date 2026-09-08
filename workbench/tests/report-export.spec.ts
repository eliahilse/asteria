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
  expect(workbook.getWorksheet('Per-Test Breakdown')).toBeDefined();
  expect(workbook.getWorksheet('Raw Data')!.rowCount).toBe(81);
  expect(workbook.getWorksheet('Security Issues')!.rowCount).toBe(17);
  expect(workbook.getWorksheet('Security Checks')!.rowCount).toBe(177);
  expect(workbook.getWorksheet('Provenance')).toBeDefined();
  await page.setViewportSize({ width: 390, height: 844 });
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
});
