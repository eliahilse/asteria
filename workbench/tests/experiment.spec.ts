import { test, expect } from '@playwright/test';
import ExcelJS from 'exceljs';

test('every combination is visible without selectors and empty rates export as blanks', async ({ page }) => {
  await page.route('**/api/experiment', async route => route.fulfill({ json: await (await page.request.get('/data/original-matrix.json')).json() }));
  await page.goto('/');
  await expect(page.locator('.combination-table tbody tr')).toHaveCount(16);
  await expect(page.locator('.combination-table thead th')).toHaveCount(11);
  await expect(page.locator('select')).toHaveCount(0);
  await expect(page.locator('.combination-table tbody button')).toHaveCount(0);
  const row = page.locator('[data-condition="generation_none"]');
  await expect(row.locator('[data-stat="attempts"]')).toHaveText('0');
  await expect(row.locator('[data-stat="compiled"]')).toHaveText('—');
  await expect(row.locator('[data-stat="securityIssues"]')).toHaveText('—');
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export XLSX', exact: true }).click();
  const wb = new ExcelJS.Workbook(); await wb.xlsx.readFile((await (await download).path())!);
  const sheet = wb.getWorksheet('Combinations')!, columns = sheet.getRow(1).values as string[];
  expect(sheet.rowCount).toBe(17);
  expect(sheet.getRow(2).getCell(columns.indexOf('Compile %')).value).toBeNull();
  expect(sheet.getRow(2).getCell(columns.indexOf('security_issues_detected')).value).toBeNull();
  expect(wb.getWorksheet('Test rates')!.rowCount).toBe(433);
  expect(wb.getWorksheet('Attempts')!.rowCount).toBe(1);
});

test('quality metrics and one security issue column fit on desktop, with coverage-aware changes', async ({ page }) => {
  const errors: string[] = []; page.on('pageerror', e => errors.push(e.message));
  await page.route('**/api/experiment', async route => {
    const data = await (await page.request.get('/data/original-matrix.json')).json(); data.local = true;
    const baseline = data.studies[0], selected = ['reuse_b', 'reuse_sb', 'generation_s', 'generation_sfb'];
    const followup = structuredClone(baseline);
    followup.plan.id = 'browser-followup-only'; followup.plan.phase = 'security_followup';
    followup.plan.conditions = baseline.plan.conditions.filter((c: { id: string }) => selected.includes(c.id)).flatMap((c: { id: string }) => ['none', 'overview', 'task', 'flows'].map(strategy => ({ ...c, id: `${c.id}__${strategy}`, parentCondition: c.id, securityStrategy: strategy, contextAcquisitionId: strategy === 'task' ? 'test-acquisition' : undefined })));
    followup.plan.acquisitions = [{ id: 'test-acquisition', citationChecks: { matched: 2, total: 2, uncitedItems: 1, itemsByKind: { security_property: 3, existing_risk: 0, change_risk: 2, unknown: 1 } } }];
    followup.summary.conditions = followup.plan.conditions.map((c: { id: string; parentCondition: string; securityStrategy: string }) => {
      const r = structuredClone(baseline.summary.conditions.find((r: { id: string }) => r.id === c.parentCondition));
      r.id = c.id; r.attempts = 5; r.planned = 5; r.compiled = 5;
      for (const check of r.checks) Object.assign(check, { pass: 5, fail: 0, attempts: 5, executed: 5 });
      const check = r.checks.find((t: { name: string }) => t.name === 'oversizedPhysicalLine');
      const fail = c.securityStrategy === 'task' ? 2 : c.securityStrategy === 'flows' ? 1 : 5;
      const executed = c.securityStrategy === 'flows' ? 4 : 5;
      Object.assign(check, { pass: executed - fail, fail, unknown: 5 - executed, executed });
      if (c.securityStrategy === 'overview') Object.assign(r.checks.find((t: { name: string }) => t.name === 'rejectsNullName'), { fail: 2, pass: 3 });
      // A broken positive round trip must not add to the issue detector count.
      Object.assign(r.checks.find((t: { name: string }) => t.name === 'validRecordRoundTrip'), { fail: 5, pass: 0 });
      return r;
    });
    data.studies.push(followup); await route.fulfill({ json: data });
  });
  await page.goto('/');
  await expect(page.locator('.combination-table tbody tr')).toHaveCount(32);
  await expect(page.locator('.combination-table thead th')).toHaveCount(11);
  await expect(page.locator('select')).toHaveCount(0);
  const row = page.locator('[data-condition="generation_sfb__task"]');
  await expect(row.locator('.security-context')).toHaveText('Task-focused');
  await expect(row.locator('[data-stat="unit"]')).toHaveText('100.0');
  await expect(row.locator('[data-stat="securityIssues"]')).toHaveText('2/50 (↓3)');
  await expect(page.locator('[data-condition="generation_sfb__overview"] [data-stat="securityIssues"]')).toHaveText('7/50 (↑2)');
  await expect(page.locator('[data-condition="generation_sfb__flows"] [data-stat="securityIssues"]')).toHaveText('1/49 (Δ —)');
  await expect(page.locator('[data-condition="generation_sfb__none"] [data-stat="securityIssues"]')).toHaveText('5/50');
  expect(await page.locator('.combination-scroll').evaluate(el => el.scrollWidth <= el.clientWidth)).toBe(true);
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export XLSX', exact: true }).click();
  const wb = new ExcelJS.Workbook(); await wb.xlsx.readFile((await (await download).path())!);
  const sheet = wb.getWorksheet('Combinations')!, columns = sheet.getRow(1).values as string[];
  const exported = sheet.getRows(2, sheet.rowCount - 1)!.find(r => r.getCell(columns.indexOf('method')).value === 'Generation' && r.getCell(columns.indexOf('paper_context')).value === 'S+F+B' && r.getCell(columns.indexOf('security_strategy')).value === 'task')!;
  expect(exported.getCell(columns.indexOf('security_issues_detected')).value).toBe(2);
  expect(exported.getCell(columns.indexOf('security_issues_delta')).value).toBe(-3);
  await page.setViewportSize({ width: 390, height: 844 });
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
  expect(errors).toEqual([]);
});
