import { useEffect, useState } from 'react';
import type { MatrixData } from './experiment-types';
import { PERCENT_MIN_N, combinationRows, fractionText, percentAllowed, percentText, statColumns, type CombinationRow, type SecurityIssues, type StatColumn } from './combination-stats';
export type * from './experiment-types';

const securityNames: Record<string, string> = { none: 'None', overview: 'Overview', task: 'Task-focused', flows: 'Data-flow', requirements: 'Requirements', boundaries: 'Trust boundaries', operations: 'Operational guards', task_only: 'Task only', catalog: 'CWE catalog',
  'single_shot+static': 'Agent insert', 'agentic+none': 'Agentic, no context', 'agentic+static': 'Agentic + insert', 'agentic+adaptive': 'Agentic + adaptive sidecar', 'agentic+gate': 'Agentic + gate', 'agentic+coach': 'Agentic + coach', 'agentic+gate_once': 'Agentic + gate once', 'agentic+rewind': 'Agentic + rewind' };
// Rounds in natural order (i9 < i16 < i16b < i21a); the newest round is the default view.
const roundKey = (id: string) => { const m = /^i(\d+)([a-z]?)/.exec(id); return m ? [Number(m[1]), m[2]] as const : [Number.MAX_SAFE_INTEGER, id] as const; };
const byRound = (a: string, b: string) => { const [x, y] = [roundKey(a), roundKey(b)]; return x[0] - y[0] || String(x[1]).localeCompare(String(y[1])); };
// Counts first (docs/REPORTING.md): every rate cell reads k/N; the percentage is confined to the tooltip.
const cellText = (r: CombinationRow, column: StatColumn) => column.format === 'count' ? String(r.attempts) : fractionText(r.stats[column.key]) ?? '—';
const cellTitle = (r: CombinationRow, column: StatColumn, unit: string) => {
  if (column.format === 'count') return column.description;
  const f = r.stats[column.key], percent = percentText(f);
  if (!percent) return `${column.description} No observations.`;
  return `${fractionText(f)} = ${percent}. ${column.description}` + (percentAllowed(f) ? '' : ` Not printed as a percentage: N = ${r.attempts} ${unit} is below ${PERCENT_MIN_N}; the counts are the result.`);
};
function IssueCount({ issues: s }: { issues: SecurityIssues }) {
  const bounds = s.deltaBounds;
  const trend = !bounds ? 'Δ —' : bounds[1] < 0 ? `↓${-bounds[1]}${bounds[0] === bounds[1] ? '' : `–${-bounds[0]}`}` : bounds[0] > 0 ? `↑${bounds[0]}${bounds[0] === bounds[1] ? '' : `–${bounds[1]}`}` : bounds[0] === 0 && bounds[1] === 0 ? '→0' : 'Δ —';
  // Fixed denominator: every condition plans 10 issue checks × N; failed + unresolved + passed = expected.
  const explanation = `${s.detected ?? 0} failed issue checks; ${s.unresolved} unresolved (not passes); ${s.evaluated - (s.detected ?? 0)} passed; ${s.expected} planned (10 × N). ` +
    (s.hasControl ? !bounds ? 'Change unavailable: unequal N, pending observations or missing check catalog.' : `Failure-count difference from the fresh control lies between ${bounds[0]} and ${bounds[1]} for every possible assignment of unmeasured checks. This is not a confidence interval. A range containing zero has no directional arrow.` : 'No treatment comparison for replay or control rows.');
  return <td className="numeric security-issues" data-stat="securityIssues" title={explanation}>
    {!s.expected ? '—' : <>{s.detected ?? 0} failed · {s.unresolved} unresolved / {s.expected}{s.hasControl && <span className={bounds && bounds[1] < 0 ? 'pass' : bounds && bounds[0] > 0 ? 'fail' : ''}> ({trend})</span>}</>}
  </td>;
}
async function json(url: string) { const r = await fetch(url); if (!r.ok || !r.headers.get('content-type')?.includes('application/json')) throw new Error('Experiment data unavailable'); const d = await r.json(); if (d.error) throw new Error(d.error); return d; }

export function Experiment() {
  const [data, setData] = useState<MatrixData>(), [error, setError] = useState(''), [busy, setBusy] = useState(false), [round, setRound] = useState<string>();
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
  // Several rounds in one file: default to the newest round, with a selector for the others. A replay-plus-follow-up pair stays as one view.
  const studyIds = data.studies.filter(s => s.plan.phase !== 'screening').map(s => s.plan.id).sort(byRound), latest = studyIds[studyIds.length - 1], selectable = studyIds.length > 1, selected = round ?? (selectable ? latest : 'all');
  const labels = Object.fromEntries(data.studies.map(s => [s.plan.id, s.plan.label ?? s.plan.id]));
  const rows = combinationRows(data).filter(r => selected === 'all' || r.study === selected);
  const unit = data.studies.some(s => s.plan.observationUnit === 'trajectory') ? 'trajectories' : 'attempts';
  const budget = Math.max(...data.studies.map(s => s.plan.maxSubmissions ?? 1));
  const download = async (kind: 'json' | 'xlsx' | 'report') => { setBusy(true); try { const { exportExperiment } = await import('./experiment-export'); await exportExperiment(data, kind); } catch (e) { setError((e as Error).message); } finally { setBusy(false); } };
  return <section aria-label="Context combinations">
    <div className="combination-tools">{selectable && <label className="round-select">Round <select aria-label="Round" value={selected} onChange={e => setRound(e.target.value)}>
      {[...studyIds].reverse().map(id => <option key={id} value={id}>{labels[id]} · {id}{id === latest ? ' (latest)' : ''}</option>)}<option value="all">All rounds</option></select></label>}<p className="note">{rows.length} combinations · Cells are k/N counts on all N {unit}: compiled / N, unit checks / 7N, live checks / 5N, full / N. Percentages appear only in cell tooltips.{budget > 1 && ` Up to ${budget} submissions each.`}</p><div className="exports"><button disabled={busy} onClick={() => download('xlsx')}>Export XLSX</button><button disabled={busy} onClick={() => download('report')}>Export report XLSX</button><button disabled={busy} onClick={() => download('json')}>Export JSON</button></div></div>
    {!data.local && rows.every(r => !r.attempts) && <p className="note">Public plans only. No observations in this snapshot.</p>}
    {error && <p role="alert">{error}</p>}
    {[...new Set(data.studies.filter(s => (selected === 'all' || s.plan.id === selected) && s.plan.evaluationNote).map(s => s.plan.evaluationNote))].map(note => <p className="note" key={note}>{note}</p>)}
    <div className="combination-scroll" tabIndex={0} aria-label="All context combinations and numeric results"><table className="combination-table"><thead>
      <tr><th scope="col">Cohort</th><th scope="col">Method</th><th scope="col" title="S = structural, F = functional, B = behavioral">Paper context</th><th scope="col" className="security-heading">Security strategy</th>
        {statColumns.map(c => <th key={c.key} scope="col" title={c.description}>{c.label}</th>)}<th scope="col" className="security-heading">Security issues</th></tr>
    </thead><tbody>{rows.map((r, index) => <tr key={`${r.study}:${r.condition}`} data-condition={r.condition} data-study={r.study} className={index === 0 || r.phase !== rows[index - 1].phase || r.method !== rows[index - 1].method || r.paper.join() !== rows[index - 1].paper.join() ? 'combination-start' : ''}>
      <td>{r.phase}</td><td>{r.method}</td><td>{r.paper.length ? r.paper.map(p => <span key={p} className="paper-context">{p}</span>) : 'None'}</td><td>{r.security === 'none' ? 'None' : <span className={`security-context security-${r.security}`}>{securityNames[r.security] ?? r.security}</span>}</td>
      {statColumns.map(c => <td key={c.key} data-stat={c.key} className="numeric" title={cellTitle(r, c, unit)}>{cellText(r, c)}</td>)}<IssueCount issues={r.issues} />
    </tr>)}</tbody></table></div>
    <p className="note">Security issues = failed · unresolved / planned issue checks (10 checks × N {unit}), not unique vulnerabilities; the remainder passed, and unresolved checks are not passes. Arrow ranges allow every unmeasured check to pass or fail; they are not confidence intervals. Δ — = no clear direction or comparison unavailable. Per-test detail remains in the export.</p>
  </section>;
}
