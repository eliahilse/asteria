import { it, expect } from 'vitest';
import { tally, testComparisons, wilson } from './analysis';
import type { Run, TestDefinition, Status } from './types';
const test = { id: 'security_v1.bounds', label: 'Bounds' } as TestDefinition;
const run = (status: Status, signature = 'same') => ({ evaluationSignature: signature, checks: [{ id: test.id, status }] }) as Run;
it('separates tested rates, all-attempt rates, and each missing/error outcome', () => {
  const t = tally([run('pass'), run('fail'), run('not_run'), run('unknown'), run('compile_error'), run('infrastructure_error')], test.id);
  expect(t).toMatchObject({ pass: 1, fail: 1, not_run: 1, unknown: 1, compile_error: 1, infrastructure_error: 1, attempts: 6, executed: 2, unresolved: 4, passRate: 0.5, allAttemptRate: 1 / 6 });
  expect(tally([], test.id).passRate).toBeNull();
  expect(tally([run('not_run')], test.id).passRate).toBeNull();
});
it('compares individual tests and withholds differences across evaluator versions', () => {
  const [r] = testComparisons([test], [run('pass'), run('not_run')], [run('pass'), run('fail')]);
  expect(r.delta).toBe(-50);
  expect(r.baseline.allAttemptRate).toBe(0.5);
  expect(r.baseline.passRate).toBe(1);
  expect(testComparisons([test], [run('pass')], [run('fail', 'changed')])[0].delta).toBeNull();
  expect(testComparisons([test], [], [run('pass')])[0].delta).toBeNull();
});
it('keeps empty and small-sample intervals explicit', () => {
  expect(wilson(0, 0)).toBeNull();
  expect(wilson(0, 2)![1]).toBeCloseTo(0.65762, 4);
});
