import { useEffect, useState } from 'react';
import type { MatrixData } from './experiment-types';
import { combinationRows, statColumns, type SecurityIssues, type StatColumn } from './combination-stats';
export type * from './experiment-types';

const securityNames: Record<string, string> = { none: 'None', overview: 'Overview', task: 'Task-focused', flows: 'Data-flow', requirements: 'Requirements', boundaries: 'Trust boundaries', operations: 'Operational guards', task_only: 'Task only' };
const format = (value: number | null, column: StatColumn) => value === null ? '—' : column.format === 'count' ? String(value) : value.toFixed(1);
function IssueCount({ issues: s }: { issues: SecurityIssues }) {
  const bounds = s.deltaBounds;
  const trend = !bounds ? 'Δ —' : bounds[1] < 0 ? `↓${-bounds[1]}${bounds[0] === bounds[1] ? '' : `–${-bounds[0]}`}` : bounds[0] > 0 ? `↑${bounds[0]}${bounds[0] === bounds[1] ? '' : `–${bounds[1]}`}` : bounds[0] === 0 && bounds[1] === 0 ? '→0' : 'Δ —';
  const explanation = `${s.detected ?? 0} failed issue checks; ${s.evaluated} evaluated; ${s.expected - s.evaluated} unresolved. ` +
    (s.hasControl ? !bounds ? 'Change unavailable: unequal N, pending observations or missing check catalog.' : `Failure-count difference from the fresh control lies between ${bounds[0]} and ${bounds[1]} for every possible assignment of unmeasured checks. This is not a confidence interval. A range containing zero has no directional arrow.` : 'No treatment comparison for replay or control rows.');
  return <td className="numeric security-issues" data-stat="securityIssues" title={explanation}>
    {s.detected === null ? '—' : <>{s.detected}/{s.evaluated}{s.hasControl && <span className={bounds && bounds[1] < 0 ? 'pass' : bounds && bounds[0] > 0 ? 'fail' : ''}> ({trend})</span>}</>}
  </td>;
}
async function json(url: string) { const r = await fetch(url); if (!r.ok || !r.headers.get('content-type')?.includes('application/json')) throw new Error('Experiment data unavailable'); const d = await r.json(); if (d.error) throw new Error(d.error); return d; }

export function Experiment() {
  const [data, setData] = useState<MatrixData>(), [error, setError] = useState(''), [busy, setBusy] = useState(false);
  useEffect(() => {
    let current = true;
    const refresh = async () => {
      try {
        let result;
        const iteration = new URLSearchParams(location.search).get('iteration');
        try { result = await json('api/experiment' + (iteration ? `?iteration=${encodeURIComponent(iteration)}` : '')); }
        catch (e) { if (import.meta.env.DEV) throw e; result = await json('data/matrix.json'); }
        if (current) { setData(result); setError(''); }
      } catch (e) { if (current) setError((e as Error).message); }
    };
    refresh(); const timer = setInterval(refresh, 5000);
    return () => { current = false; clearInterval(timer); };
  }, []);
  if (!data) return <p role={error ? 'alert' : 'status'}>{error || 'Loading results…'}</p>;
  const rows = combinationRows(data);
  const unit = data.studies.some(s => s.plan.observationUnit === 'trajectory') ? 'trajectories' : 'attempts';
  const budget = Math.max(...data.studies.map(s => s.plan.maxSubmissions ?? 1));
  const download = async (kind: 'json' | 'xlsx' | 'report') => { setBusy(true); try { const { exportExperiment } = await import('./experiment-export'); await exportExperiment(data, kind); } catch (e) { setError((e as Error).message); } finally { setBusy(false); } };
  return <section aria-label="Context combinations">
    <div className="combination-tools"><p className="note">{rows.length} combinations · Quality percentages use all N {unit}.{budget > 1 && ` Up to ${budget} submissions each.`}</p><div className="exports"><button disabled={busy} onClick={() => download('xlsx')}>Export XLSX</button><button disabled={busy} onClick={() => download('report')}>Export report XLSX</button><button disabled={busy} onClick={() => download('json')}>Export JSON</button></div></div>
    {!data.local && rows.every(r => !r.values.attempts) && <p className="note">Public plans only. No observations in this snapshot.</p>}
    {error && <p role="alert">{error}</p>}
    {data.studies.filter(s => s.plan.evaluationNote).map(s => <p className="note" key={s.plan.id}>{s.plan.evaluationNote}</p>)}
    <div className="combination-scroll" tabIndex={0} aria-label="All context combinations and numeric results"><table className="combination-table"><thead>
      <tr><th scope="col">Cohort</th><th scope="col">Method</th><th scope="col" title="S = structural, F = functional, B = behavioral">Paper context</th><th scope="col" className="security-heading">Security strategy</th>
        {statColumns.map(c => <th key={c.key} scope="col" title={c.description}>{c.label}</th>)}<th scope="col" className="security-heading">Security issues</th></tr>
    </thead><tbody>{rows.map((r, index) => <tr key={`${r.study}:${r.condition}`} data-condition={r.condition} data-study={r.study} className={index === 0 || r.phase !== rows[index - 1].phase || r.method !== rows[index - 1].method || r.paper.join() !== rows[index - 1].paper.join() ? 'combination-start' : ''}>
      <td>{r.phase}</td><td>{r.method}</td><td>{r.paper.length ? r.paper.map(p => <span key={p} className="paper-context">{p}</span>) : 'None'}</td><td>{r.security === 'none' ? 'None' : <span className={`security-context security-${r.security}`}>{securityNames[r.security] ?? r.security}</span>}</td>
      {statColumns.map(c => <td key={c.key} data-stat={c.key} className="numeric">{format(r.values[c.key], c)}</td>)}<IssueCount issues={r.issues} />
    </tr>)}</tbody></table></div>
    <p className="note">Security issues = failed / evaluated checks across N {unit}, not unique vulnerabilities. Arrow ranges allow every unmeasured check to pass or fail; they are not confidence intervals. Δ — = no clear direction or comparison unavailable. Per-test detail remains in the export.</p>
  </section>;
}
