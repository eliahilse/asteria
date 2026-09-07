import type { Run, Status, TestDefinition } from './types';

export const statusLabel: Record<Status, string> = { pass: 'Pass', fail: 'Fail', not_run: 'Not run', unknown: 'Unknown', compile_error: 'Compile error', infrastructure_error: 'Environment error' };
export function rate(n: number, d: number) { return d ? `${(100 * n / d).toFixed(1)}%` : '—'; }
export function wilson(n: number, d: number): [number, number] | null {
  if (!d) return null;
  const z = 1.959963984540054, p = n / d, denominator = 1 + z * z / d;
  const middle = (p + z * z / (2 * d)) / denominator;
  const radius = z * Math.sqrt(p * (1 - p) / d + z * z / (4 * d * d)) / denominator;
  return [Math.max(0, middle - radius), Math.min(1, middle + radius)];
}
export function tally(runs: Run[], id: string) {
  const counts: Record<Status, number> = { pass: 0, fail: 0, not_run: 0, unknown: 0, compile_error: 0, infrastructure_error: 0 };
  for (const run of runs) counts[run.checks.find(c => c.id === id)?.status ?? 'not_run']++;
  const executed = counts.pass + counts.fail;
  return { ...counts, attempts: runs.length, executed, unresolved: runs.length - executed,
    passRate: executed ? counts.pass / executed : null, allAttemptRate: runs.length ? counts.pass / runs.length : null,
    coverage: runs.length ? executed / runs.length : null, interval: wilson(counts.pass, executed) };
}
export function testComparisons(tests: TestDefinition[], baseline: Run[], treatment: Run[]) {
  const signatures = new Set([...baseline, ...treatment].map(r => r.evaluationSignature).filter(Boolean));
  const comparable = signatures.size <= 1;
  return tests.map(test => {
    const a = tally(baseline, test.id), b = tally(treatment, test.id);
    return { test, baseline: a, treatment: b, comparable,
      delta: comparable && a.passRate !== null && b.passRate !== null ? 100 * (b.passRate - a.passRate) : null };
  });
}
