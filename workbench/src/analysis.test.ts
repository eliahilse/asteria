import { describe, it, expect } from 'vitest';
import { wilson, summarize, cells, compareRuns } from './analysis';
import type { Run } from './types';
const run = (overrides: Partial<Run> = {}) => ({ cohort: 'pilot', model: 'm', reasoning: 'medium', temperature: null, strategy: 'Generation', condition: 'a', baseContext: 'S', compileStatus: 'pass', functionalSuccess: true, assessment: 'not_reviewed', securityEvaluation: null, ...overrides }) as Run;

describe('research denominators', () => {
  it('does not count missing security evidence as a pass or a failure', () => {
    const result = summarize([run(), run({ compileStatus: 'fail', functionalSuccess: false })]);
    expect(result).toMatchObject({ n: 2, compiled: 1, functional: 1, securityAssessed: 0, securityPass: 0, joint: 0 });
  });
  it('keeps cohorts and reasoning settings separate', () => {
    expect(cells([run(), run({ cohort: 'selected' }), run({ reasoning: 'none' })])).toHaveLength(3);
    expect(compareRuns(run(), run({ reasoning: 'none' }))).toEqual(['reasoning: medium → none']);
  });
  it('computes Wilson intervals with sensible boundary behavior', () => {
    expect(wilson(0, 0)).toBeNull();
    expect(wilson(0, 2)![1]).toBeCloseTo(0.65762, 4);
    expect(wilson(2, 2)![0]).toBeCloseTo(0.34238, 4);
    expect(wilson(5, 10)![0]).toBeCloseTo(0.23659, 4);
  });
});
