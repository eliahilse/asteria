import { expect, it } from 'vitest';
import { allRunPassRate, compileRate, functionalCounts, reportGroups, suiteOutcome, type ReportEntry } from './report-data';
import type { MatrixData, MatrixRun } from './experiment-types';

const run = (id: string, compilation: string | null, statuses: string[]): MatrixRun => ({
  runId: id, condition: 'generation_s', repetition: 1, status: 'completed', errorCategory: null,
  evaluationStatus: 'evaluated', mainCompilation: compilation, functionalSuccess: false, finishReason: 'stop', usage: null,
  checks: statuses.map((status, i) => ({ suite: 'unit', name: `test${i}`, status: status as 'pass' | 'fail' | 'unknown', detail: '' })),
});
const entry = (r: MatrixRun): ReportEntry => ({ study: 'study', task: 'Highscore', method: 'Generation', context: 'S', security: 'none', condition: r.condition, run: r });

it('separates measured pass rates, unknown coverage, and all-run denominators', () => {
  const entries = [entry(run('a', 'pass', ['pass', 'pass', 'fail', 'unknown'])), entry(run('b', 'fail', [])), entry(run('c', null, []))];
  expect(compileRate(entries)).toBe(1 / 3);
  expect(functionalCounts(entries, 'unit')).toEqual({ passed: 2, failed: 1, evaluated: 3, expected: 7, unresolved: 4, rate: 2 / 3 });
  expect(allRunPassRate(entries)).toBe(2 / 48);
  expect(suiteOutcome(entries[0], 'unit')).toBe('FAIL (incomplete coverage)');
  expect(suiteOutcome(entries[1], 'unit')).toBe('game compile-fail');
  expect(suiteOutcome(entries[2], 'unit')).toBe('not evaluated');
  expect(functionalCounts([], 'unit').rate).toBeNull();
});

it('computes overall rates from counts rather than averaging context percentages', () => {
  const entries = [entry(run('a', 'pass', ['pass'])), entry(run('b', 'pass', ['fail', 'fail', 'fail']))];
  expect(functionalCounts(entries).rate).toBe(1 / 4);
});

it('keeps studies and security strategies separate, including planned combinations with no runs', () => {
  const condition = { id: 'generation_s', strategy: 'Generation', securityStrategy: 'none', contextTypes: ['S'] };
  const data = { studies: ['first', 'second'].map(id => ({
    plan: { id, conditions: [condition, { ...condition, id: 'generation_s__task_only', securityStrategy: 'task_only' }] },
    runs: [run(id, 'pass', ['pass'])],
  })) } as MatrixData;
  const groups = reportGroups(data);
  expect(groups).toHaveLength(4);
  expect(groups.filter(g => g.security === 'none').map(g => g.entries[0].study)).toEqual(['first', 'second']);
  expect(groups.filter(g => g.security === 'task_only').every(g => g.entries.length === 0)).toBe(true);
  expect(groups.every(g => g.entries.every(e => e.study === g.study.plan.id))).toBe(true);
});
