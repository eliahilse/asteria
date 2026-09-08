import { expect, test } from 'vitest';
import type { MatrixStudy } from './experiment';
import { matrixComparisons } from './experiment-analysis';
import { experimentWorkbook } from './experiment-export';

test('security comparisons use fresh matching controls and preserve missing denominators', () => {
  const check = { id: 'security_v1.fixture', name: 'fixture', suite: 'security_v1', kind: 'security', pass: 1, fail: 0, not_run: 1, attempts: 2, executed: 1, passRate: 1, allAttemptRate: 0.5 };
  const study = { plan: { id: 'fixture-only', phase: 'security_followup', deviations: [], conditions: [
    { id: 'new-control', parentCondition: 'reuse_s', securityStrategy: 'none' },
    { id: 'new-treatment', parentCondition: 'reuse_s', securityStrategy: 'flows' },
    { id: 'unrelated-control', parentCondition: 'generation_s', securityStrategy: 'none' },
  ] }, summary: { selected: [], conditions: [
    { id: 'new-control', checks: [check] },
    { id: 'new-treatment', checks: [{ ...check, fail: 1, not_run: 0, executed: 2, passRate: 0.5 }] },
    { id: 'unrelated-control', checks: [{ ...check, passRate: 0 }] },
  ] }, runs: [] } as unknown as MatrixStudy;
  for (const c of study.plan.conditions) Object.assign(c, { strategy: 'Reuse', paperPromptId: 3, contextTypes: ['S'], baseContext: 'S' });
  for (const r of study.summary.conditions) Object.assign(r, { attempts: 2, pending: 0, compiled: 1, fullFunctional: 0, unverifiedSettings: 0, transportErrors: 0 });
  const [comparison] = matrixComparisons(study);
  expect(comparison.control).toBe('new-control');
  expect(comparison.testedDeltaPp).toBe(-50);
  expect(comparison.allAttemptDeltaPp).toBe(0);
  const sheet = experimentWorkbook({ local: true, studies: [study] }).getWorksheet('Security comparisons')!;
  expect(sheet.rowCount).toBe(2);
  const columns = sheet.getRow(1).values as string[];
  expect(sheet.getRow(2).getCell(columns.indexOf('delta_all_attempt_pp')).value).toBe(0);
  study.summary.conditions[0].checks[0] = { ...check, passRate: null, allAttemptRate: null } as typeof study.summary.conditions[0]['checks'][0];
  expect(matrixComparisons(study)[0].testedDeltaPp).toBeNull();
  expect(matrixComparisons(study)[0].allAttemptDeltaPp).toBeNull();
  study.plan.phase = 'screening';
  expect(matrixComparisons(study)).toEqual([]);
});
