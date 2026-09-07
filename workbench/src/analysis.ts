import type { Run } from './types';

export function wilson(successes: number, attempts: number): [number, number] | null {
  if (!attempts) return null;
  const z = 1.959963984540054, p = successes / attempts, denominator = 1 + z * z / attempts;
  const center = (p + z * z / (2 * attempts)) / denominator;
  const half = z * Math.sqrt(p * (1 - p) / attempts + z * z / (4 * attempts * attempts)) / denominator;
  return [Math.max(0, center - half), Math.min(1, center + half)];
}

export function summarize(runs: Run[]) {
  const compiled = runs.filter(r => r.compileStatus === 'pass').length;
  const functional = runs.filter(r => r.functionalSuccess).length;
  const reviewed = runs.filter(r => r.assessment !== 'not_reviewed').length;
  const findings = runs.filter(r => r.assessment === 'findings_present').length;
  const securityAssessed = runs.filter(r => ['pass', 'fail'].includes(r.securityEvaluation?.status ?? '')).length;
  const securityPass = runs.filter(r => r.securityEvaluation?.status === 'pass').length;
  const joint = runs.filter(r => r.functionalSuccess && r.securityEvaluation?.status === 'pass').length;
  return { n: runs.length, compiled, functional, reviewed, findings, securityAssessed, securityPass, joint };
}

export function cells(runs: Run[]) {
  const groups = new Map<string, Run[]>();
  for (const run of runs) {
    // Including cohort and model settings prevents accidental pooling.
    const key = [run.cohort, run.model, run.reasoning, run.temperature, run.strategy, run.condition].join('|');
    groups.set(key, [...(groups.get(key) ?? []), run]);
  }
  return [...groups].map(([key, members]) => ({ key, run: members[0], runs: members, ...summarize(members) }));
}

export function compareRuns(left: Run, right: Run) {
  const fields = ['cohort', 'model', 'reasoning', 'temperature', 'strategy', 'baseContext'] as const;
  return fields.filter(key => left[key] !== right[key]).map(key => `${key}: ${String(left[key])} → ${String(right[key])}`);
}

export const formatNumber = (value: number | null | undefined) => value == null ? 'Not recorded' : value.toLocaleString('en-US');
export const percent = (n: number, total: number) => total ? `${Math.round(100 * n / total)}%` : '—';
