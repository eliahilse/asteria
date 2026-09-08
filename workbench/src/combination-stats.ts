import type { MatrixData, MatrixStudy, MatrixCondition, MatrixCheck } from './experiment-types';
export type StatColumn = { key: string; group: string; label: string; description: string; format: 'count' | 'percent' | 'delta' | 'mean'; security?: boolean };
export type CombinationRow = { study: string; condition: string; phase: string; method: string; paper: string[]; security: string; acquisition?: string; values: Record<string, number | null> };
export const securityTests = [
  ['validRecordRoundTrip', 'Valid round-trip'], ['rejectsNegativeScore', 'Negative score'], ['rejectsNegativeTime', 'Negative time'],
  ['rejectsNullName', 'Null name'], ['rejectsBlankName', 'Blank name'], ['rejectsExcessiveName', 'Long name'],
  ['boundsRetainedEntries', 'Retained entries'], ['malformedStoreDoesNotCrash', 'Malformed store'], ['oversizedPhysicalLine', 'Oversized line'],
  ['nativeDeserializationCanary', 'Deserialization'], ['largePersistedRecordSet', 'Persisted records'],
];
const pct = (p: number, n: number) => n ? 100 * p / n : null;
const mean = (values: (number | null | undefined)[]) => values.length && values.every(v => typeof v === 'number') ? (values as number[]).reduce((n, v) => n + v, 0) / values.length : null;
const difference = (b: number | null, a: number | null) => b === null || a === null ? null : b - a;
const column = (key: string, group: string, label: string, description: string, format: StatColumn['format'], security = false): StatColumn => ({ key, group, label, description, format, security });
export const statColumns: StatColumn[] = [
  ...[['security_property', 'Properties'], ['existing_risk', 'Existing risks'], ['change_risk', 'Change risks'], ['unknown', 'Unknowns']].map(([key, label]) => column(key, 'Security content types · N', label, `Number of ${label.toLowerCase()} in the injected context, not a verified vulnerability count.`, 'count', true)),
  column('attempts', 'Attempts', 'N', 'All recorded code requests, including provider errors.', 'count'),
  column('pending', 'Attempts', 'Pending', 'Awaiting response or evaluation.', 'count'),
  column('compiled', 'Functionality', 'Compile %', 'Game compiled / all attempts.', 'percent'),
  column('full', 'Functionality', 'Full %', 'All 16 functional checks pass on one response / all attempts.', 'percent'),
  ...securityTests.flatMap(([id, label]) => [
    column(`${id}.pass`, label, 'Pass %', 'Passed this security check / all attempts. A pass covers only its fixture.', 'percent', true),
    column(`${id}.delta`, label, 'Δ pp', 'Change in pass/all-attempt rate from the matching fresh follow-up control; blank for replay and control rows.', 'delta', true),
    column(`${id}.unresolved`, label, 'U', 'Unresolved outcomes: not run, unknown, compilation or environment error. N − U is the tested denominator.', 'count', true),
  ]),
  column('unit', 'Functional suites', 'Unit %', 'Passed unit checks / (7 × all attempts). Checks within a response are correlated.', 'percent'),
  column('invoked', 'Functional suites', 'Invoked %', 'Passed invoked-integration checks / (4 × all attempts).', 'percent'),
  column('autonomous', 'Functional suites', 'Live %', 'Passed autonomous-integration checks / (5 × all attempts).', 'percent'),
  column('citations', 'Security evidence', 'Cited %', 'Exact citations matching inspected source / supplied citations. Does not verify the claim.', 'percent', true),
  column('uncited', 'Security evidence', 'Uncited', 'Context items without source citations.', 'count', true),
  column('inputTokens', 'Tokens per response', 'Input mean', 'Mean reported input tokens; blank if any response lacks usage.', 'mean'),
  column('outputTokens', 'Tokens per response', 'Output mean', 'Mean reported output tokens; blank if any response lacks usage.', 'mean'),
  column('unverified', 'Response quality', 'Unverified N', 'Responses whose effective model settings were not attested.', 'count'),
  column('providerErrors', 'Response quality', 'Provider errors', 'Adapter, identity-mismatch or interrupted requests.', 'count'),
];
function row(study: MatrixStudy, condition: MatrixCondition): CombinationRow {
  const summary = study.summary.conditions.find(r => r.id === condition.id)!;
  const runs = study.runs.filter(r => r.condition === condition.id);
  const acquisition = study.plan.acquisitions?.find(a => a.id === condition.contextAcquisitionId);
  const parent = condition.parentCondition;
  const controlCondition = parent && condition.securityStrategy !== 'none' && study.plan.conditions.find(c => c.parentCondition === parent && c.securityStrategy === 'none');
  const control = controlCondition && study.summary.conditions.find(c => c.id === controlCondition.id);
  const values: CombinationRow['values'] = {
    attempts: summary.attempts, pending: summary.pending, compiled: pct(summary.compiled, summary.attempts), full: pct(summary.fullFunctional, summary.attempts),
    citations: acquisition ? pct(acquisition.citationChecks.matched, acquisition.citationChecks.total) : null,
    uncited: condition.securityStrategy === 'none' ? 0 : acquisition?.citationChecks.uncitedItems ?? null,
    inputTokens: mean(runs.map(r => r.usage?.input_tokens)), outputTokens: mean(runs.map(r => r.usage?.output_tokens)),
    unverified: summary.unverifiedSettings, providerErrors: summary.transportErrors,
  };
  for (const key of ['security_property', 'existing_risk', 'change_risk', 'unknown']) values[key] = condition.securityStrategy === 'none' ? 0 : acquisition?.citationChecks.itemsByKind[key] ?? null;
  for (const [suite, count] of [['unit', 7], ['invoked', 4], ['autonomous', 5]] as const) values[suite] = pct(summary.checks.filter(c => c.suite === suite).reduce((n, c) => n + c.pass, 0), count * summary.attempts);
  for (const [name] of securityTests) {
    const check = summary.checks.find(c => c.id === `security_v1.${name}`);
    const base = control && control.checks.find(c => c.id === `security_v1.${name}`);
    const percentage = (c: MatrixCheck | undefined) => c ? pct(c.pass, c.attempts) : null;
    values[`${name}.pass`] = percentage(check);
    values[`${name}.delta`] = difference(percentage(check), percentage(base || undefined));
    values[`${name}.unresolved`] = check ? check.attempts - check.executed : null;
  }
  return { study: study.plan.id, condition: condition.id, phase: study.plan.phase === 'screening' ? 'Replay' : 'Follow-up', method: condition.strategy, paper: condition.contextTypes, security: condition.securityStrategy, acquisition: condition.contextAcquisitionId, values };
}
export function combinationRows(data: MatrixData): CombinationRow[] {
  return [...data.studies].sort((a, b) => Number(a.plan.phase === 'screening') - Number(b.plan.phase === 'screening')).flatMap(study =>
    [...study.plan.conditions].sort((a, b) => a.strategy.localeCompare(b.strategy) || a.paperPromptId - b.paperPromptId || ['none', 'overview', 'task', 'flows'].indexOf(a.securityStrategy) - ['none', 'overview', 'task', 'flows'].indexOf(b.securityStrategy)).map(c => row(study, c)));
}
