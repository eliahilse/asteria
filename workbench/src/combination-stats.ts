import type { MatrixData, MatrixStudy, MatrixCondition } from './experiment-types';

type Summary = MatrixStudy['summary']['conditions'][number];
export type StatColumn = { key: string; label: string; description: string; format: 'count' | 'percent' };
export type SecurityIssues = { detected: number | null; evaluated: number; expected: number; delta: number | null; hasControl: boolean };
export type CombinationRow = { study: string; condition: string; phase: string; method: string; paper: string[]; security: string; values: Record<string, number | null>; issues: SecurityIssues };
const issueTests = ['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName', 'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet'];
const pct = (p: number, n: number) => n ? 100 * p / n : null;
export const statColumns: StatColumn[] = [
  { key: 'attempts', label: 'N', description: 'All recorded observations, including provider errors. The table states whether N counts requests or trajectories.', format: 'count' },
  { key: 'compiled', label: 'Compile %', description: 'Game compiled / all attempts.', format: 'percent' },
  { key: 'unit', label: 'Unit %', description: 'Passed unit checks / (7 × all attempts).', format: 'percent' },
  { key: 'invoked', label: 'Invoked %', description: 'Passed invoked-integration checks / (4 × all attempts).', format: 'percent' },
  { key: 'autonomous', label: 'Live %', description: 'Passed autonomous-integration checks / (5 × all attempts).', format: 'percent' },
  { key: 'full', label: 'Full %', description: 'All 16 functional checks pass on the evaluated feature / all N observations.', format: 'percent' },
];

export function securityIssues(summary: Summary, control?: Summary): SecurityIssues {
  // The valid-record round trip is a positive functional check, not an issue detector.
  const checks = issueTests.map(name => summary.checks.find(c => c.suite === 'security_v1' && c.name === name));
  const base = issueTests.map(name => control?.checks.find(c => c.suite === 'security_v1' && c.name === name));
  const evaluated = checks.reduce((n, c) => n + (c?.executed ?? 0), 0);
  const detected = evaluated ? checks.reduce((n, c) => n + (c?.fail ?? 0), 0) : null;
  // Compare counts only at equal exposure for EVERY check, not merely equal totals.
  const comparable = control && detected !== null && summary.attempts === control.attempts && !summary.pending && !control.pending &&
    checks.every((c, i) => c && base[i] && c.executed === base[i]!.executed);
  return { detected, evaluated, expected: issueTests.length * summary.attempts, hasControl: !!control,
    delta: comparable ? detected! - base.reduce((n, c) => n + c!.fail, 0) : null };
}

function row(study: MatrixStudy, condition: MatrixCondition): CombinationRow {
  const summary = study.summary.conditions.find(r => r.id === condition.id)!;
  const controlCondition = study.plan.phase === 'security_followup' && condition.parentCondition && condition.securityStrategy !== 'none' &&
    study.plan.conditions.find(c => c.parentCondition === condition.parentCondition && c.securityStrategy === 'none');
  const control = controlCondition ? study.summary.conditions.find(c => c.id === controlCondition.id) : undefined;
  const values: CombinationRow['values'] = { attempts: summary.attempts, compiled: pct(summary.compiled, summary.attempts), full: pct(summary.fullFunctional, summary.attempts) };
  for (const [suite, count] of [['unit', 7], ['invoked', 4], ['autonomous', 5]] as const) values[suite] = pct(summary.checks.filter(c => c.suite === suite).reduce((n, c) => n + c.pass, 0), count * summary.attempts);
  return { study: study.plan.id, condition: condition.id, phase: study.plan.label ?? (study.plan.phase === 'screening' ? 'Replay' : 'Follow-up'), method: condition.strategy,
    paper: condition.contextTypes, security: condition.securityStrategy, values, issues: securityIssues(summary, control) };
}
export function combinationRows(data: MatrixData): CombinationRow[] {
  return [...data.studies].sort((a, b) => Number(a.plan.phase === 'screening') - Number(b.plan.phase === 'screening')).flatMap(study =>
    [...study.plan.conditions].sort((a, b) => a.strategy.localeCompare(b.strategy) || a.paperPromptId - b.paperPromptId || (study.plan.axes?.securityContext ?? ['none', 'overview', 'task', 'flows']).indexOf(a.securityStrategy) - (study.plan.axes?.securityContext ?? ['none', 'overview', 'task', 'flows']).indexOf(b.securityStrategy)).map(c => row(study, c)));
}
