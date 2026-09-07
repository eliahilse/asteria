import { test, expect } from '@playwright/test';
import ExcelJS from 'exceljs';

test('paper matrix starts with 16 empty cells and exports missing rates as blanks', async ({ page }) => {
  await page.route('**/api/experiment', async route => {
    const response = await page.request.get('/data/matrix.json');
    await route.fulfill({ json: await response.json() });
  });
  await page.goto('/');
  await expect(page.locator('.experiment-matrix tbody tr')).toHaveCount(16);
  await expect(page.locator('.experiment-matrix tbody button')).toHaveCount(16);
  await expect(page.getByText('After selection', { exact: true })).toHaveCount(48);
  await page.getByRole('button', { name: 'Generation None No security context', exact: true }).click();
  await expect(page.locator('.matrix-checks tbody tr')).toHaveCount(16);
  await expect(page.locator('.matrix-checks tbody tr').first()).toContainText('—');
  await page.getByLabel('Test suite', { exact: true }).selectOption('security');
  await expect(page.locator('.matrix-checks tbody tr')).toHaveCount(11);
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export results XLSX', exact: true }).click();
  const wb = new ExcelJS.Workbook(); await wb.xlsx.readFile((await (await download).path())!);
  expect(wb.getWorksheet('Matrix')!.rowCount).toBe(17);
  expect(wb.getWorksheet('Test rates')!.rowCount).toBe(433);
  expect(wb.getWorksheet('Attempts')!.rowCount).toBe(1);
  const rates = wb.getWorksheet('Test rates')!;
  const columns = rates.getRow(1).values as string[];
  expect(rates.getRow(2).getCell(columns.indexOf('passRate')).value).toBeNull();
});

test('matrix distinguishes tested rates from all attempts and opens raw diagnostics', async ({ page }) => {
  await page.route('**/api/experiment', async route => {
    const data = await (await page.request.get('/data/matrix.json')).json();
    data.local = true;
    const study = data.studies[0], condition = study.summary.conditions[0];
    Object.assign(condition, { attempts: 2, compiled: 1, fullFunctional: 1, unverifiedSettings: 2 });
    Object.assign(condition.checks[0], { pass: 1, not_run: 1, attempts: 2, executed: 1, passRate: 1, allAttemptRate: 0.5 });
    study.runs = [{ runId: 'browser-test-only', condition: condition.id, repetition: 1, status: 'settings_unverified', evaluationStatus: 'evaluated', mainCompilation: 'pass', checks: [] }];
    await route.fulfill({ json: data });
  });
  await page.route('**/api/experiment/*/run/browser-test-only', route => route.fulfill({ json: {
    observation: { runId: 'browser-test-only', request: {}, response: { output_text: 'Browser test fixture, not a model result' } },
    evaluation: { processes: [{ command: ['fixture-compiler'], exitCode: 1, stdout: '', stderr: 'Fixture compiler output' }] },
  } }));
  await page.goto('/');
  await page.getByLabel('Cell measure', { exact: true }).selectOption('full');
  await page.getByRole('button', { name: 'Reuse None No security context', exact: true }).click();
  const row = page.locator('.matrix-checks tbody tr').first();
  await expect(row).toContainText('100.0%');
  await expect(row).toContainText('50.0%');
  await page.getByRole('button', { name: 'Repetition 1', exact: true }).click();
  await expect(page.getByText('Fixture compiler output', { exact: true })).toBeVisible();
  await page.getByText('Original model response', { exact: true }).click();
  await expect(page.getByText('Browser test fixture, not a model result', { exact: true })).toBeVisible();
});
