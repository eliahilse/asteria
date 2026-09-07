import type { MatrixCondition, MatrixStudy } from './experiment';
import type { TestDefinition } from './types';
import { rate } from './analysis';
import { matrixComparisons } from './experiment-analysis';
const pp = (n: number | null) => n === null ? '—' : `${n > 0 ? '+' : ''}${n.toFixed(1)}`;

export function SecurityComparison({ study, condition, definitions, suite }: { study: MatrixStudy; condition: MatrixCondition; definitions: TestDefinition[]; suite: string }) {
  const rows = matrixComparisons(study).filter(c => (c.treatment === condition.id || c.control === condition.id) && (suite === 'all' || suite === c.kind || suite === c.suite));
  if (!rows.length) return null;
  return <>
    <h3>Difference from fresh control</h3>
    <p className="note">Treatment minus the no-security control for the same method and paper context. These are independent code responses. Positive values mean more checks passed; unexecuted outcomes remain visible in the all-attempt denominator. {study.summary.complete ? '' : 'Collection is incomplete; differences are provisional.'}</p>
    <div className="table-scroll"><table className="security-comparison"><thead><tr><th>Test</th><th>Security case</th><th>Control pass / tested</th><th>Treatment pass / tested</th><th>Control pass / attempts</th><th>Treatment pass / attempts</th><th>Δ tested (pp)</th><th>Δ all attempts (pp)</th></tr></thead><tbody>{rows.map(r => <tr key={`${r.treatment}:${r.testId}`}><td>{definitions.find(d => d.id === r.testId)?.label ?? r.testName}</td><td>{r.securityStrategy}</td><td>{rate(r.controlCounts.pass, r.controlCounts.executed)}<small>{r.controlCounts.pass}/{r.controlCounts.executed}</small></td><td>{rate(r.treatmentCounts.pass, r.treatmentCounts.executed)}<small>{r.treatmentCounts.pass}/{r.treatmentCounts.executed}</small></td><td>{rate(r.controlCounts.pass, r.controlCounts.attempts)}<small>{r.controlCounts.pass}/{r.controlCounts.attempts}</small></td><td>{rate(r.treatmentCounts.pass, r.treatmentCounts.attempts)}<small>{r.treatmentCounts.pass}/{r.treatmentCounts.attempts}</small></td><td>{pp(r.testedDeltaPp)}</td><td>{pp(r.allAttemptDeltaPp)}</td></tr>)}</tbody></table></div>
  </>;
}
