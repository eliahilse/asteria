import { test, expect } from '@playwright/test';

test('context page shows the task and exact prompt inserts without generation controls or traces', async ({ page, context }) => {
  const text = '--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT ---\nPreserve this exact prompt insert.\nSource: Scores.java:2-3\n--- END REPOSITORY-DERIVED SECURITY CONTEXT ---\n';
  await page.route('**/api/context-inserts**', route => route.fulfill({ json: { local: true, iteration: 'fixture', groups: [
    { method: 'Generation', task: 'Add highscore for the game', inserts: [{ id: 'fixture', strategy: 'task_only', label: 'Task only', text, sha256: 'fixture' }] },
  ] } }));
  await page.goto('/?view=generation');
  await expect(page.getByLabel('Task', { exact: true })).toHaveValue('Add highscore for the game');
  await expect(page.getByRole('heading', { name: 'Task only', exact: true })).toBeVisible();
  expect(await page.locator('.prompt-insert').textContent()).toBe(text);
  await expect(page.locator('select')).toHaveCount(0);
  await expect(page.getByRole('button', { name: 'Generate context', exact: true })).toHaveCount(0);
  await expect(page.getByText('Acquisition trace')).toHaveCount(0);
  await context.grantPermissions(['clipboard-read', 'clipboard-write']);
  await page.getByRole('button', { name: 'Copy insert' }).click();
  expect(await page.evaluate(() => navigator.clipboard.readText())).toBe(text);
  await page.setViewportSize({ width: 390, height: 844 });
  expect(await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)).toBe(false);
});

test('missing context data is reported without fabricating a prompt insert', async ({ page }) => {
  await page.route('**/api/context-inserts**', route => route.fulfill({ contentType: 'text/html', body: '<html>Unavailable</html>' }));
  await page.goto('/?view=generation');
  await expect(page.getByRole('alert')).toContainText('Cannot load saved prompt inserts');
  await expect(page.locator('.prompt-insert')).toHaveCount(0);
});
