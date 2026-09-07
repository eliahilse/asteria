import { test, expect } from '@playwright/test';
import ExcelJS from 'exceljs';

test('research overview, filters, evidence record and exact prompt comparison', async ({ page }) => {
  const errors: string[] = [];
  page.on('pageerror', e => errors.push(e.message));
  await page.goto('/');
  await expect(page.getByRole('heading', { name: 'From context to evidence.' })).toBeVisible();
  await expect(page.locator('.metric').nth(0)).toContainText('16');
  await expect(page.locator('.metric').nth(1)).toContainText('4/16');
  await expect(page.locator('.metric').nth(2)).toContainText('1/16');
  await expect(page.locator('.metric').nth(3)).toContainText('3/4');
  await page.screenshot({ path: 'test-results/overview-desktop.png', fullPage: true });
  await page.getByRole('button', { name: 'Run explorer', exact: true }).click();
  await page.getByLabel('Model filter').selectOption('gpt-5.4-mini');
  await expect(page.getByRole('heading', { name: '8 observations' })).toBeVisible();
  await page.getByRole('button', { name: 'Inspect gpt-5.4-mini_none__generation_s__r1', exact: true }).click();
  await expect(page.getByRole('dialog')).toBeVisible();
  await expect(page.getByRole('dialog')).toContainText('Environment error');
  await page.getByRole('button', { name: 'findings', exact: true }).click();
  await page.getByRole('button', { name: 'Inspect cited code · lines 130, 131' }).click();
  await expect(page.locator('.highlight-line').first()).toContainText('int size = data.readInt()');
  await page.keyboard.press('Escape');
  await expect(page.getByRole('dialog')).not.toBeVisible();
  await page.getByRole('button', { name: 'Compare evidence', exact: true }).click();
  await expect(page.locator('.diff-added')).toContainText('BEGIN SECURITY FINDINGS CONTEXT');
  await page.getByRole('button', { name: 'Highscore code', exact: true }).click();
  await expect(page.locator('.diff-code')).toContainText('MAX_ENTRIES');
  expect(errors).toEqual([]);
});

test('filtered XLSX export contains individual checks and exact evidence', async ({ page }) => {
  await page.goto('/');
  await page.getByLabel('Model filter').selectOption('gpt-5.4-mini');
  const downloaded = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export XLSX' }).click();
  const download = await downloaded;
  expect(download.suggestedFilename()).toBe('asteria-highscore-evidence.xlsx');
  const workbook = new ExcelJS.Workbook();
  await workbook.xlsx.readFile((await download.path())!);
  expect(workbook.getWorksheet('Runs')!.rowCount).toBe(9);
  expect(workbook.getWorksheet('Historical checks')!.rowCount).toBe(177);
  expect(workbook.getWorksheet('Evidence text')!.rowCount).toBeGreaterThan(20);
});

test('cohorts remain separate and narrow viewports remain navigable', async ({ page }) => {
  await page.setViewportSize({ width: 390, height: 844 });
  await page.goto('/');
  await page.getByLabel('Evidence cohort').selectOption('published_selected');
  await expect(page.locator('.metric').first()).toContainText('6');
  await expect(page.locator('.notice').first()).toContainText('cannot describe all 80');
  await page.getByRole('button', { name: 'Context catalogue' }).click();
  await expect(page.getByRole('heading', { name: 'Context catalogue' })).toBeVisible();
  await expect(page.locator('.fact-card')).toHaveCount(5);
  await page.getByLabel('Extracted context type').selectOption('C4');
  await expect(page.locator('.extracted-fact')).toHaveCount(6);
  const overflow = await page.evaluate(() => document.documentElement.scrollWidth > innerWidth);
  expect(overflow).toBe(false);
  await page.screenshot({ path: 'test-results/contexts-mobile.png', fullPage: true });
});

test('context provenance and planned prompt differences remain separate from outcomes', async ({ page }) => {
  await page.goto('/?view=contexts');
  await page.getByLabel('Extracted context type').selectOption('C2');
  await expect(page.locator('.extracted-fact').first()).toContainText('syntactic flow candidate');
  await expect(page.locator('.extracted-fact').first()).toContainText('input.readLine');
  await page.getByRole('button', { name: 'Luna experiment plan' }).click();
  await expect(page.getByRole('heading', { name: 'Luna experiment plan' })).toBeVisible();
  await expect(page.getByText('Planned · no observations published')).toBeVisible();
  await page.getByRole('button', { name: 'Inspect planned generation_security_c4', exact: true }).click();
  await expect(page.locator('.diff-added')).toContainText('Do not deserialize arbitrary Java objects');
  await page.getByRole('button', { name: 'Inspect complete frozen prompt' }).click();
  await expect(page.locator('.source-code')).toContainText('BEGIN ATTACHED TARGET SOURCE');
  await page.screenshot({ path: 'test-results/luna-plan-desktop.png', fullPage: true });
});
