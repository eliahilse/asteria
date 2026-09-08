import { test, expect } from '@playwright/test';
import { rm } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';

test('new acquisitions accept just the repository and task without making model calls', async ({ request, baseURL }) => {
  const response = await request.post('/api/context-generation', {
    headers: { Origin: new URL(baseURL!).origin },
    data: { repository: 'ApoMario', task: 'Add Highscore to ApoMario.', action: 'prepare' },
  });
  expect(response.status()).toBe(202);
  const { id } = await response.json();
  expect(id).toMatch(/^context-[a-f0-9]{32}$/);
  try {
    const saved = await request.get(`/api/context-generation/${id}`);
    expect(saved.status()).toBe(200);
    const record = await saved.json();
    expect(record.protocol).toBe('repository-security-context-v5-task-only');
    expect(record.strategy).toBe('task_only');
    expect(record.task).toBe('Add Highscore to ApoMario.');
    expect(record.initialPrompt).toContain('TASK\nAdd Highscore to ApoMario.');
    expect(record.initialPrompt).not.toContain('\nSTRATEGY\n');
    expect(record.initialPrompt).not.toContain('OPERATION REPRESENTATION');
    expect(record.status).toBe('prepared');
    expect(record.turns).toEqual([]);
  } finally {
    await rm(fileURLToPath(new URL(`../../.local/context-generation/${id}`, import.meta.url)), { recursive: true });
  }
});

test('legacy strategy requests cannot silently become task-only acquisitions', async ({ request, baseURL }) => {
  const response = await request.post('/api/context-generation', {
    headers: { Origin: new URL(baseURL!).origin },
    data: { repository: 'ApoMario', task: 'Add Highscore.', strategy: 'task', action: 'prepare' },
  });
  expect(response.status()).toBe(400);
  expect((await response.json()).error).toContain('task_only');
});
