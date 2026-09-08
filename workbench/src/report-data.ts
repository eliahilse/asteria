import type { MatrixCondition, MatrixData, MatrixRun, MatrixStudy } from './experiment-types';

export const paperContexts = ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B'];
export const functionalSuites = { unit: 7, invoked: 4, autonomous: 5 } as const;
export type FunctionalSuite = keyof typeof functionalSuites;
export type ReportEntry = { study: string; task: string; method: string; context: string; security: string; condition: string; run: MatrixRun };
export type ReportGroup = { study: MatrixStudy; task: string; method: string; security: string; entries: ReportEntry[] };
export type CheckCounts = { passed: number; failed: number; evaluated: number; expected: number; unresolved: number; rate: number | null };

export const ratio = (numerator: number, denominator: number) => denominator ? numerator / denominator : null;
export const paperContext = (condition: MatrixCondition) => ['S', 'F', 'B'].filter(c => condition.contextTypes.includes(c)).join('+') || 'None';
export const compiledEntries = (entries: ReportEntry[]) => entries.filter(e => e.run.mainCompilation === 'pass');
export const compileRate = (entries: ReportEntry[]) => ratio(compiledEntries(entries).length, entries.length);
export const fullRate = (entries: ReportEntry[]) => ratio(entries.filter(e => e.run.functionalSuccess === true).length, entries.length);

export function reportGroups(data: MatrixData): ReportGroup[] {
  return data.studies.flatMap(study => {
    const groups = new Map<string, ReportGroup>();
    const conditions = new Map(study.plan.conditions.map(c => [c.id, c]));
    for (const condition of conditions.values()) {
      const key = JSON.stringify([condition.strategy, condition.securityStrategy]);
      if (!groups.has(key)) groups.set(key, { study, task: 'Highscore', method: condition.strategy, security: condition.securityStrategy, entries: [] });
    }
    for (const run of study.runs) {
      const condition = conditions.get(run.condition);
      if (!condition) throw new Error(`Run ${run.runId} has no matching condition.`);
      groups.get(JSON.stringify([condition.strategy, condition.securityStrategy]))!.entries.push({
        study: study.plan.id, task: 'Highscore', method: condition.strategy, context: paperContext(condition),
        security: condition.securityStrategy, condition: condition.id, run,
      });
    }
    return [...groups.values()].sort((a, b) => a.security.localeCompare(b.security) || b.method.localeCompare(a.method));
  });
}

/** Match the report's compiled-run view; unknown checks never become failures. */
export function functionalCounts(entries: ReportEntry[], suite?: FunctionalSuite, name?: string): CheckCounts {
  const compiled = compiledEntries(entries);
  const expected = compiled.length * (name ? 1 : suite ? functionalSuites[suite] : 16);
  const checks = compiled.flatMap(e => e.run.checks.filter(c => Object.hasOwn(functionalSuites, c.suite) &&
    (!suite || c.suite === suite) && (!name || c.name === name)));
  const passed = checks.filter(c => c.status === 'pass').length;
  const failed = checks.filter(c => c.status === 'fail').length;
  const evaluated = passed + failed;
  if (evaluated > expected) throw new Error('Functional observations exceed the declared test count.');
  return { passed, failed, evaluated, expected, unresolved: expected - evaluated, rate: ratio(passed, evaluated) };
}

export function allRunPassRate(entries: ReportEntry[]) {
  return ratio(functionalCounts(entries).passed, 16 * entries.length);
}

export function suiteOutcome(entry: ReportEntry, suite: FunctionalSuite): string {
  if (entry.run.mainCompilation === 'fail') return 'game compile-fail';
  if (entry.run.mainCompilation !== 'pass') return 'not evaluated';
  const counts = functionalCounts([entry], suite);
  if (counts.failed) return counts.unresolved ? 'FAIL (incomplete coverage)' : 'FAIL';
  return counts.unresolved ? 'unknown' : 'PASS';
}

export function mostCommonFailure(entries: ReportEntry[], suite: string, name: string): string | null {
  const counts = new Map<string, number>();
  for (const entry of compiledEntries(entries)) for (const check of entry.run.checks) {
    if (check.suite === suite && check.name === name && check.status === 'fail' && check.detail) {
      counts.set(check.detail, (counts.get(check.detail) ?? 0) + 1);
    }
  }
  return [...counts].sort((a, b) => b[1] - a[1] || a[0].localeCompare(b[0]))[0]?.[0] ?? null;
}
