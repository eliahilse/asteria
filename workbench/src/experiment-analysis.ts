import type { MatrixCheck, MatrixStudy } from './experiment';

const delta = (treatment: number | null, control: number | null) => treatment === null || control === null ? null : 100 * (treatment - control);
export type MatrixComparison = {
  parentCondition: string; control: string; treatment: string; securityStrategy: string;
  testId: string; testName: string; kind: string; suite: string;
  controlCounts: MatrixCheck; treatmentCounts: MatrixCheck;
  testedDeltaPp: number | null; allAttemptDeltaPp: number | null;
};
export function matrixComparisons(study: MatrixStudy): MatrixComparison[] {
  if (study.plan.phase !== 'security_followup') return [];
  return study.plan.conditions.filter(c => c.securityStrategy !== 'none').flatMap(treatment => {
    const control = study.plan.conditions.find(c => c.parentCondition === treatment.parentCondition && c.securityStrategy === 'none');
    if (!control) return [];
    const a = study.summary.conditions.find(c => c.id === control.id), b = study.summary.conditions.find(c => c.id === treatment.id);
    if (!a || !b) return [];
    return b.checks.flatMap(t => {
      const c = a.checks.find(c => c.id === t.id);
      return c ? [{ parentCondition: treatment.parentCondition!, control: control.id, treatment: treatment.id, securityStrategy: treatment.securityStrategy,
        testId: t.id, testName: t.name, kind: t.kind, suite: t.suite, controlCounts: c, treatmentCounts: t,
        testedDeltaPp: delta(t.passRate, c.passRate), allAttemptDeltaPp: delta(t.allAttemptRate, c.allAttemptRate) }] : [];
    });
  });
}
