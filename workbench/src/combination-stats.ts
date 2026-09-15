import type { MatrixData, MatrixStudy, MatrixCondition } from './experiment-types';

type Summary = MatrixStudy['summary']['conditions'][number];
/** docs/REPORTING.md: a percentage may accompany a k/N count only from this denominator upwards; it never replaces the counts. */
export const PERCENT_MIN_N = 20;
/**
 * A reported rate keeps its numerator and denominator. `unresolved` outcomes are listed beside it, never dropped.
 * `units` is the number of independent observations (trajectories or attempts) behind a check count such as 28/35;
 * checks on one artifact are correlated, so the percentage threshold is judged on units, not on the check denominator.
 */
export type Fraction = { numerator: number; denominator: number; unresolved?: number; units?: number };
export const fraction = (numerator: number, denominator: number, extra: { unresolved?: number; units?: number } = {}): Fraction =>
  ({ numerator, denominator, ...(extra.unresolved ? { unresolved: extra.unresolved } : {}), ...(extra.units === undefined ? {} : { units: extra.units }) });
export const fractionValue = (f: Fraction) => f.denominator ? f.numerator / f.denominator : null;
/** Whether a percentage may be printed beside the counts: at least PERCENT_MIN_N independent observations. */
export const percentAllowed = (f: Fraction) => (f.units ?? f.denominator) >= PERCENT_MIN_N;
/** `k/N` text with any unresolved count beside it; null when nothing was observed, because an empty rate is not zero. */
export const fractionText = (f: Fraction) => f.denominator ? `${f.numerator}/${f.denominator}${f.unresolved ? `; ${f.unresolved} unresolved` : ''}` : null;
/** The percentage for a tooltip or a wide-N table; callers decide whether N permits printing it. */
export const percentText = (f: Fraction) => { const value = fractionValue(f); return value === null ? null : `${(100 * value).toFixed(1)}%`; };
export type StatColumn = { key: string; label: string; description: string; format: 'count' | 'fraction' };
/** Issue-check totals with the fixed denominator: expected = 10 × N = detected + unresolved + passed. */
export type SecurityIssues = { detected: number | null; evaluated: number; unresolved: number; expected: number; delta: number | null; deltaBounds: [number, number] | null; hasControl: boolean };
export type CombinationRow = { study: string; condition: string; phase: string; method: string; paper: string[]; security: string; attempts: number; stats: Record<string, Fraction>; issues: SecurityIssues };
/** The ten issue checks of research/security/PROTOCOL.md in protocol order; validRecordRoundTrip is the positive persistence check. */
export const issueTests = ['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName', 'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet'];
export const positiveTest = 'validRecordRoundTrip';
// The four invoked-integration checks still count toward Full (all 16 checks) and remain in the
// per-test and raw exports; they are no longer a separate headline column.
// Every fraction column is shown as k/N; its denominator is stated here (docs/REPORTING.md).
export const statColumns: StatColumn[] = [
  { key: 'attempts', label: 'N', description: 'All recorded observations, including provider errors. The table states whether N counts requests or trajectories.', format: 'count' },
  { key: 'compiled', label: 'Compiled', description: 'Game compiled / all N observations.', format: 'fraction' },
  { key: 'unit', label: 'Unit checks', description: 'Passed unit checks / (7 × N).', format: 'fraction' },
  { key: 'autonomous', label: 'Live checks', description: 'Passed autonomous-integration checks / (5 × N).', format: 'fraction' },
  { key: 'full', label: 'Full', description: 'All 16 functional checks pass on the evaluated feature / all N observations.', format: 'fraction' },
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
  const expected = issueTests.length * summary.attempts;
  const bounded = control && detected !== null && summary.attempts === control.attempts && !summary.pending && !control.pending && checks.every(Boolean) && base.every(Boolean);
  const baseDetected = base.reduce((n, c) => n + (c?.fail ?? 0), 0), baseEvaluated = base.reduce((n, c) => n + (c?.executed ?? 0), 0);
  // Extremes over every possible assignment of unresolved checks in both arms.
  // This is a missingness bound on counts, not a statistical confidence interval.
  const deltaBounds: [number, number] | null = bounded ? [detected - baseDetected - (expected - baseEvaluated), detected + expected - evaluated - baseDetected] : null;
  return { detected, evaluated, unresolved: expected - evaluated, expected, hasControl: !!control,
    delta: comparable ? detected! - baseDetected : null, deltaBounds };
}

function row(study: MatrixStudy, condition: MatrixCondition): CombinationRow {
  const summary = study.summary.conditions.find(r => r.id === condition.id)!;
  // Fresh control of the same round and cell: 'none' for single-shot arms, '<mode>+none' for agentic arms (agentic-delivery plans label arms '<mode>+<sidecar>').
  const strategy = condition.securityStrategy, controlStrategy = strategy.includes('+') && !strategy.startsWith('single_shot') ? `${strategy.split('+')[0]}+none` : 'none';
  const controlCondition = (study.plan.phase === 'security_followup' || study.plan.phase === 'agentic_delivery') && condition.parentCondition && strategy !== controlStrategy &&
    study.plan.conditions.find(c => c.parentCondition === condition.parentCondition && c.securityStrategy === controlStrategy);
  const control = controlCondition ? study.summary.conditions.find(c => c.id === controlCondition.id) : undefined;
  // Fixed denominators: N observations for compiled and full, 7 × N unit checks and 5 × N live checks.
  const stats: CombinationRow['stats'] = { compiled: fraction(summary.compiled, summary.attempts), full: fraction(summary.fullFunctional, summary.attempts) };
  for (const [suite, count] of [['unit', 7], ['autonomous', 5]] as const) stats[suite] = fraction(summary.checks.filter(c => c.suite === suite).reduce((n, c) => n + c.pass, 0), count * summary.attempts, { units: summary.attempts });
  return { study: study.plan.id, condition: condition.id, phase: study.plan.label ?? (study.plan.phase === 'screening' ? 'Replay' : 'Follow-up'), method: condition.strategy,
    paper: condition.contextTypes, security: condition.securityStrategy, attempts: summary.attempts, stats, issues: securityIssues(summary, control) };
}
export function combinationRows(data: MatrixData): CombinationRow[] {
  return [...data.studies].sort((a, b) => Number(a.plan.phase === 'screening') - Number(b.plan.phase === 'screening')).flatMap(study =>
    [...study.plan.conditions].sort((a, b) => a.strategy.localeCompare(b.strategy) || a.paperPromptId - b.paperPromptId || (study.plan.axes?.securityContext ?? ['none', 'overview', 'task', 'flows']).indexOf(a.securityStrategy) - (study.plan.axes?.securityContext ?? ['none', 'overview', 'task', 'flows']).indexOf(b.securityStrategy)).map(c => row(study, c)));
}
