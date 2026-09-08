import { test, expect } from '@playwright/test';
import ExcelJS from 'exceljs';

test('every combination is visible without selectors and empty rates export as blanks', async ({ page }) => {
  await page.route('**/api/experiment', async route => route.fulfill({ json: await (await page.request.get('/data/matrix.json')).json() }));
  await page.goto('/');
  await expect(page.locator('.combination-table tbody tr')).toHaveCount(16);
  await expect(page.locator('select')).toHaveCount(0);
  await expect(page.locator('.combination-table tbody button')).toHaveCount(0);
  const row = page.locator('[data-condition="generation_none"]');
  await expect(row.locator('[data-stat="attempts"]')).toHaveText('0');
  await expect(row.locator('[data-stat="rejectsNegativeScore.pass"]')).toHaveText('—');
  const download = page.waitForEvent('download');
  await page.getByRole('button', { name: 'Export XLSX', exact: true }).click();
  const wb = new ExcelJS.Workbook(); await wb.xlsx.readFile((await (await download).path())!);
  const sheet = wb.getWorksheet('Combinations')!, columns = sheet.getRow(1).values as string[];
  expect(sheet.rowCount).toBe(17);
  expect(sheet.getRow(2).getCell(columns.indexOf('Negative score · Pass %')).value).toBeNull();
  expect(wb.getWorksheet('Test rates')!.rowCount).toBe(433);
  expect(wb.getWorksheet('Attempts')!.rowCount).toBe(1);
});

test('security impact and distinct content-type counts appear directly in the combination row', async ({ page }) => {
  const errors: string[] = []; page.on('pageerror', e => errors.push(e.message));
  await page.route('**/api/experiment', async route => {
    const data = await (await page.request.get('/data/matrix.json')).json(); data.local = true;
    const baseline = data.studies[0], selected = ['reuse_b', 'reuse_sb', 'generation_s', 'generation_sfb'];
    const followup = structuredClone(baseline);
    followup.plan.id = 'browser-followup-only'; followup.plan.phase = 'security_followup';
    followup.plan.conditions = baseline.plan.conditions.filter((c: { id: string }) => selected.includes(c.id)).flatMap((c: { id: string }) => ['none', 'overview', 'task', 'flows'].map(strategy => ({ ...c, id: `${c.id}__${strategy}`, parentCondition: c.id, securityStrategy: strategy, contextAcquisitionId: strategy === 'task' ? 'test-acquisition' : undefined })));
    followup.plan.acquisitions = [{ id: 'test-acquisition', citationChecks: { matched: 2, total: 2, uncitedItems: 1, itemsByKind: { security_property: 3, existing_risk: 0, change_risk: 2, unknown: 1 } } }];
    followup.summary.conditions = followup.plan.conditions.map((c: { id: string; parentCondition: string; securityStrategy: string }) => {
      const r = structuredClone(baseline.summary.conditions.find((r: { id: string }) => r.id === c.parentCondition));
      r.id = c.id; r.attempts = 5; r.planned = 5; r.compiled = 5;
      const check = r.checks.find((t: { name: string }) => t.name === 'oversizedPhysicalLine');
      Object.assign(check, { pass: c.securityStrategy === 'task' ? 3 : 0, fail: c.securityStrategy === 'task' ? 1 : 5, unknown: c.securityStrategy === 'task' ? 1 : 0, attempts: 5, executed: c.securityStrategy === 'task' ? 4 : 5, passRate: c.securityStrategy === 'task' ? 0.75 : 0, allAttemptRate: c.securityStrategy === 'task' ? 0.6 : 0 });
      return r;
    });
    data.studies.push(followup); await route.fulfill({ json: data });
  });
  await page.goto('/');
  await expect(page.locator('.combination-table tbody tr')).toHaveCount(32);
  await expect(page.locator('select')).toHaveCount(0);
  const row = page.locator('[data-condition="generation_sfb__task"]');
  await expect(row.locator('.security-context')).toHaveText('Task-focused');
  await expect(row.locator('[data-stat="security_property"]')).toHaveText('3');
  await expect(row.locator('[data-stat="change_risk"]')).toHaveText('2');
  await expect(row.locator('[data-stat="oversizedPhysicalLine.pass"]')).toHaveText('60.0');
  await expect(row.locator('[data-stat="oversizedPhysicalLine.delta"]')).toHaveText('+60.0');
  await expect(row.locator('[data-stat="oversizedPhysicalLine.unresolved"]')).toHaveText('1');
  await expect(page.locator('[data-condition="generation_sfb__none"] [data-stat="oversizedPhysicalLine.delta"]')).toHaveText('—');
  await expect(page.locator('[data-condition="generation_sfb"] [data-stat="oversizedPhysicalLine.delta"]')).toHaveText('—');
  await page.setViewportSize({ width: 390, height: 844 });
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
  expect(errors).toEqual([]);
});
