import type { MatrixCondition, MatrixData, MatrixRun, MatrixStudy } from './experiment-types';
import { fraction, type Fraction } from './combination-stats';

export const paperContexts = ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B'];
export const functionalSuites = { unit: 7, invoked: 4, autonomous: 5 } as const;  // Highscore; taskSuites has every task
export const taskSuites: Record<string, { unit: number; invoked: number; autonomous: number }> = { Highscore: { unit: 7, invoked: 4, autonomous: 5 }, Achievements: { unit: 16, invoked: 7, autonomous: 5 } };
export const taskName = (plan: object): string => ({ highscore: 'Highscore', achievements: 'Achievements' })[(plan as { task?: string }).task ?? 'highscore'] ?? 'Highscore';
export const suitesFor = (task: string) => taskSuites[task] ?? taskSuites.Highscore;
export const totalFor = (task: string): number => { const s = suitesFor(task); return s.unit + s.invoked + s.autonomous; };
export type FunctionalSuite = keyof typeof functionalSuites;
export type ReportEntry = { study: string; task: string; method: string; context: string; security: string; condition: string; run: MatrixRun };
export type ReportGroup = { study: MatrixStudy; task: string; method: string; security: string; entries: ReportEntry[] };
export type CheckCounts = { passed: number; failed: number; evaluated: number; expected: number; unresolved: number; rate: number | null };

export const ratio = (numerator: number, denominator: number) => denominator ? numerator / denominator : null;
export const paperContext = (condition: MatrixCondition) => ['S', 'F', 'B'].filter(c => condition.contextTypes.includes(c)).join('+') || 'None';
export const compiledEntries = (entries: ReportEntry[]) => entries.filter(e => e.run.mainCompilation === 'pass');
// Counts first (docs/REPORTING.md): report tables take the Fraction forms; the numeric rates remain for analysis.
/** Compiled / all recorded runs in the group (unit: trajectory or attempt). */
export const compileCounts = (entries: ReportEntry[]): Fraction => fraction(compiledEntries(entries).length, entries.length);
export const compileRate = (entries: ReportEntry[]) => ratio(compiledEntries(entries).length, entries.length);
/** Fully functional / all recorded runs (the primary endpoint). */
export const fullCounts = (entries: ReportEntry[]): Fraction => fraction(entries.filter(e => e.run.functionalSuccess === true).length, entries.length);
export const fullRate = (entries: ReportEntry[]) => ratio(entries.filter(e => e.run.functionalSuccess === true).length, entries.length);

export function reportGroups(data: MatrixData): ReportGroup[] {
  return data.studies.flatMap(study => {
    const groups = new Map<string, ReportGroup>();
    const conditions = new Map(study.plan.conditions.map(c => [c.id, c]));
    for (const condition of conditions.values()) {
      const key = JSON.stringify([condition.strategy, condition.securityStrategy]);
      if (!groups.has(key)) groups.set(key, { study, task: taskName(study.plan), method: condition.strategy, security: condition.securityStrategy, entries: [] });
    }
    for (const run of study.runs) {
      const condition = conditions.get(run.condition);
      if (!condition) throw new Error(`Run ${run.runId} has no matching condition.`);
      groups.get(JSON.stringify([condition.strategy, condition.securityStrategy]))!.entries.push({
        study: study.plan.id, task: taskName(study.plan), method: condition.strategy, context: paperContext(condition),
        security: condition.securityStrategy, condition: condition.id, run,
      });
    }
    return [...groups.values()].sort((a, b) => a.security.localeCompare(b.security) || b.method.localeCompare(a.method));
  });
}

/** Match the report's compiled-run view; unknown checks never become failures. */
export function functionalCounts(entries: ReportEntry[], suite?: FunctionalSuite, name?: string): CheckCounts {
  const compiled = compiledEntries(entries); const task = entries[0]?.task ?? 'Highscore'; const suites = suitesFor(task);
  const expected = compiled.length * (name ? 1 : suite ? suites[suite] : totalFor(task));
  const checks = compiled.flatMap(e => e.run.checks.filter(c => Object.hasOwn(functionalSuites, c.suite) &&
    (!suite || c.suite === suite) && (!name || c.name === name)));
  const passed = checks.filter(c => c.status === 'pass').length;
  const failed = checks.filter(c => c.status === 'fail').length;
  const evaluated = passed + failed;
  if (evaluated > expected) throw new Error('Functional observations exceed the declared test count.');
  return { passed, failed, evaluated, expected, unresolved: expected - evaluated, rate: ratio(passed, evaluated) };
}

/** Passed / evaluated checks on compiled runs, pooled from counts; unresolved checks travel with the fraction. */
export function passCounts(entries: ReportEntry[], suite?: FunctionalSuite, name?: string): Fraction {
  const c = functionalCounts(entries, suite, name);
  return fraction(c.passed, c.evaluated, { unresolved: c.unresolved, units: entries.length });
}

/** Passed functional checks / (16 × all recorded runs): the fixed-denominator view that keeps incomplete delivery visible. */
export const allRunPassCounts = (entries: ReportEntry[]): Fraction => fraction(functionalCounts(entries).passed, totalFor(entries[0]?.task ?? 'Highscore') * entries.length, { units: entries.length });
export function allRunPassRate(entries: ReportEntry[]) {
  return ratio(functionalCounts(entries).passed, totalFor(entries[0]?.task ?? 'Highscore') * entries.length);
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
