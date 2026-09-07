import { test, expect } from '@playwright/test';
import ExcelJS from 'exceljs';

test('opens the test table, removes old runs and exposes exact test contracts', async ({ page }) => {
  const errors: string[] = []; page.on('pageerror', e => errors.push(e.message));
  await page.goto('/');
  await expect(page.getByRole('heading', { name: 'Highscore', exact: true })).toBeVisible();
  await expect(page.locator('.results-table tbody tr')).toHaveCount(11);
  await expect(page.getByText('No new evaluations imported. Rates are unreported until tests run.')).toBeVisible();
  await page.getByRole('button', { name: 'Bound physical-line processing', exact: true }).click();
  await expect(page.getByRole('dialog')).toContainText('64 MiB');
  await expect(page.getByRole('dialog')).toContainText('Environment error');
  await page.getByText('Test source', { exact: true }).click();
  await expect(page.getByRole('dialog').locator('pre')).toContainText('oversizedPhysicalLine');
  await page.keyboard.press('Escape');
  await page.getByLabel('Tests', { exact: true }).selectOption('functional');
  await expect(page.locator('.results-table tbody tr')).toHaveCount(16);
  await page.getByRole('button', { name: 'Runs', exact: true }).click();
  await expect(page.getByText('No new runs imported.')).toBeVisible();
  expect(errors).toEqual([]);
});

test('context filtering, source details and actual coverage gaps', async ({ page }) => {
  await page.goto('/?view=contexts');
  await page.getByLabel('Context type', { exact: true }).selectOption('C2');
  await expect(page.locator('.context-table tbody tr')).toHaveCount(3);
  await page.locator('.context-table summary').first().click();
  await expect(page.locator('.context-table')).toContainText('syntactic flow candidate');
  await expect(page.locator('.context-table')).toContainText('input.readLine');
  await page.getByText('Context types and test coverage', { exact: true }).click();
  await expect(page.getByText('None — coverage gap').first()).toBeVisible();
});

test('planned prompt diffs and XLSX carry details without invented results', async ({ page }) => {
  await page.goto('/?view=conditions');
  await page.getByRole('button', { name: 'generation_security_c4', exact: true }).click();
  await expect(page.locator('.diff-added')).toContainText('Do not deserialize arbitrary Java objects');
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export XLSX', exact: true }).click();
  const wb = new ExcelJS.Workbook(); await wb.xlsx.readFile((await (await download).path())!);
  expect(wb.getWorksheet('Runs')!.rowCount).toBe(1);
  expect(wb.getWorksheet('Test comparisons')!.rowCount).toBe(12);
  expect(wb.getWorksheet('Test definitions')!.rowCount).toBe(28);
});

test('plain navigation remains usable on narrow screens', async ({ page }) => {
  await page.setViewportSize({ width: 390, height: 844 }); await page.goto('/');
  await page.getByRole('button', { name: 'Contexts', exact: true }).click();
  await page.getByLabel('Context type').selectOption('C4');
  await expect(page.locator('.context-table tbody tr')).toHaveCount(6);
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
  await page.screenshot({ path: 'test-results/simple-mobile.png' });
});

test('test-only observations expose conditional-rate differences and missing denominators', async ({ page }) => {
  // Synthetic transport data exists only in this browser test, never in the study assets.
  await page.route('**/data/study.json', async route => {
    const response = await route.fetch(), data = await response.json();
    const definition = data.tests[0];
    data.runs = ['pass', 'not_run', 'pass', 'fail'].map((status, index) => ({
      id: `test-fixture-${index}`, planId: data.experimentPlans[0].id,
      condition: index < 2 ? 'generation_security_none' : 'generation_security_all',
      model: data.experimentPlans[0].model, repetition: index + 1,
      evaluationSignature: 'same-test-environment',
      checks: [{ id: definition.id, status, detail: 'Synthetic browser-test fixture' }],
    }));
    await route.fulfill({ response, json: data });
  });
  await page.goto('/');
  const row = page.locator('.results-table tbody tr').first();
  await expect(row).toContainText('100.0%');
  await expect(row).toContainText('50.0%');
  await expect(row.locator('td').last()).toHaveText('-50.0');
  await row.getByRole('button').click();
  const dialog = page.getByRole('dialog');
  await expect(dialog).toContainText('Pass / all attempts');
  await expect(dialog).toContainText('Synthetic browser-test fixture');
  await expect(dialog.getByText('50.0%', { exact: true })).toHaveCount(2);
});
