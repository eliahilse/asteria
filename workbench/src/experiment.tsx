import { useEffect, useState } from 'react';
import type { TestDefinition } from './types';
import type { MatrixData } from './experiment-types';
import { combinationRows, statColumns, type StatColumn } from './combination-stats';
export type * from './experiment-types';

const securityNames: Record<string, string> = { none: 'None', overview: 'Overview', task: 'Task-focused', flows: 'Data-flow' };
const format = (value: number | null, column: StatColumn) => value === null ? '—' : column.format === 'count' ? String(value) : column.format === 'delta' ? `${value > 0 ? '+' : ''}${value.toFixed(1)}` : value.toFixed(1);
async function json(url: string) { const r = await fetch(url); if (!r.ok || !r.headers.get('content-type')?.includes('application/json')) throw new Error('Experiment data unavailable'); const d = await r.json(); if (d.error) throw new Error(d.error); return d; }

export function Experiment({ definitions }: { definitions: TestDefinition[] }) {
  const [data, setData] = useState<MatrixData>(), [error, setError] = useState(''), [busy, setBusy] = useState(false);
  useEffect(() => {
    let current = true;
    const refresh = async () => {
      try {
        let result;
        try { result = await json('api/experiment'); }
        catch (e) { if (import.meta.env.DEV) throw e; result = await json('data/matrix.json'); }
        if (current) { setData(result); setError(''); }
      } catch (e) { if (current) setError((e as Error).message); }
    };
    refresh(); const timer = setInterval(refresh, 5000);
    return () => { current = false; clearInterval(timer); };
  }, []);
  if (!data) return <p role={error ? 'alert' : 'status'}>{error || 'Loading results…'}</p>;
  const rows = combinationRows(data);
  const groups = statColumns.reduce<{ label: string; security: boolean; columns: StatColumn[] }[]>((groups, c) => {
    if (groups.at(-1)?.label === c.group) groups.at(-1)!.columns.push(c);
    else groups.push({ label: c.group, security: !!c.security, columns: [c] });
    return groups;
  }, []);
  const download = async (kind: 'json' | 'xlsx') => { setBusy(true); try { const { exportExperiment } = await import('./experiment-export'); await exportExperiment(data, kind); } catch (e) { setError((e as Error).message); } finally { setBusy(false); } };
  return <section aria-label="Context combinations">
    <div className="combination-tools"><p className="note">{rows.length} combinations · Pass % uses all attempts. Δ pp compares with the matching fresh control. U = unresolved; tested N = N − U.</p><div className="exports"><button disabled={busy} onClick={() => download('xlsx')}>Export XLSX</button><button disabled={busy} onClick={() => download('json')}>Export JSON</button></div></div>
    {!data.local && <p className="note">Public plans only. Private observations are available in the local explorer.</p>}
    {error && <p role="alert">{error}</p>}
    <div className="combination-scroll" tabIndex={0} aria-label="All context combinations and numeric results"><table className="combination-table"><thead>
      <tr><th rowSpan={2}>Cohort</th><th rowSpan={2}>Method</th><th rowSpan={2}>Paper context</th><th rowSpan={2} className="security-heading">Security strategy</th>{groups.map(g => <th key={g.label} colSpan={g.columns.length} className={g.security ? 'security-heading' : ''} scope="colgroup">{g.label}</th>)}</tr>
      <tr>{statColumns.map(c => <th key={c.key} scope="col" className={c.security ? 'security-heading' : ''} title={`${c.description}${c.security && c.key.includes('.') ? '\n' + (definitions.find(d => d.name === c.key.split('.')[0])?.fixture ?? '') : ''}`}>{c.label}</th>)}</tr>
    </thead><tbody>{rows.map((r, index) => <tr key={`${r.study}:${r.condition}`} data-condition={r.condition} data-study={r.study} className={index === 0 || r.phase !== rows[index - 1].phase || r.method !== rows[index - 1].method || r.paper.join() !== rows[index - 1].paper.join() ? 'combination-start' : ''}>
      <td>{r.phase}</td><td>{r.method}</td><td>{r.paper.length ? r.paper.map(p => <span key={p} className="paper-context">{p}</span>) : 'None'}</td><td>{r.security === 'none' ? 'None' : <span className={`security-context security-${r.security}`}>{securityNames[r.security] ?? r.security}</span>}</td>
      {statColumns.map(c => <td key={c.key} data-stat={c.key} className={`numeric ${c.format === 'delta' && r.values[c.key] ? r.values[c.key]! > 0 ? 'positive-delta' : 'negative-delta' : ''}`}>{format(r.values[c.key], c)}</td>)}
    </tr>)}</tbody></table></div>
    <p className="note">S = structural, F = functional, B = behavioral. Security strategy describes acquisition; properties, existing risks, change risks and unknowns are content types, counted in separate columns. Suite percentages count correlated checks. Five attempts per cell are exploratory; a test pass covers its fixture.</p>
  </section>;
}
