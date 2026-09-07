import { useEffect, useState } from 'react';

const strategies = [
  { id: 'overview', label: 'Repository overview', description: 'Map security-relevant architecture, trust boundaries, remote calls, storage, cryptography and dependencies.' },
  { id: 'task', label: 'Task-focused review', description: 'Find the feature and inspect the code it depends on. Derive security context for this particular change.' },
  { id: 'flows', label: 'Data-flow review', description: 'Follow relevant inputs through parsing, guards and sensitive operations across source files.' },
];
type Evidence = { path: string; start_line: number; end_line: number; quote: string; sourceMatch: boolean; inspected: boolean; sourceSha256: string | null };
type Item = { id: string; kind: string; topic: string; statement: string; task_relevance: string; cwes: string[]; evidence: Evidence[]; suggested_check: string; citationStatus: string };
type Turn = { number: number; status: string; requestSha256: string; request: { messages: { role: string; content: string }[] }; response?: { output_text: string; model: string; finish_reason: string; usage?: Record<string, number | null> }; toolResult?: { excerpts?: { path: string; start_line: number; end_line: number; text: string }[]; error?: string; totalMatches?: number; truncated?: boolean } };
type Generation = { id: string; status: string; repository: string; strategy: string; task: string; model: string; settings: { reasoning_effort: string }; settingsVerified: boolean | null; startedAt: string; snapshotFingerprint: string; sourceFiles: number; sourceLines: number; omittedFiles: number; inspectedFiles?: number; inspectedLines?: number; maxTurns: number; generatorHashes?: Record<string, string>; initialPrompt: string; turns: Turn[]; output: { summary: string; items: Item[]; limitations: string[] } | null; citationChecks: { matched: number; total: number; uncitedItems: number; limitation: string } | null; errorCategory?: string };
type Index = { local: boolean; adapterReady: boolean; targets: string[]; runs: { id: string; repository: string; task: string; strategy: string; status: string; startedAt: string; turns: number; items: number | null; generatorHash?: string | null }[] };
const label = (value: string) => value.replaceAll('_', ' ');
async function api(path = '', init?: RequestInit) {
  const response = await fetch(`api/context-generation${path}`, init);
  if (!response.headers.get('content-type')?.includes('application/json')) throw new Error('Generation runs locally. Open the local development site to create and inspect private outputs.');
  const data = await response.json();
  if (!response.ok) throw new Error(data.error || 'Cannot load context generation.');
  return data;
}
function download(value: unknown, name: string) {
  const url = URL.createObjectURL(new Blob([JSON.stringify(value, null, 2)], { type: 'application/json' }));
  const link = document.createElement('a'); link.href = url; link.download = name; link.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
}

export function ContextGeneration() {
  const [index, setIndex] = useState<Index>(), [selected, setSelected] = useState(() => new URLSearchParams(location.search).get('context') || ''), [run, setRun] = useState<Generation>();
  const [repository, setRepository] = useState('ApoMario'), [strategy, setStrategy] = useState('task');
  const [task, setTask] = useState('Implement a Highscore feature for ApoMario: store player names, scores and completion times, retain rankings across game sessions, and integrate score submission and display with the game.');
  const [error, setError] = useState(''), [submitting, setSubmitting] = useState(false), [kind, setKind] = useState('all');
  useEffect(() => {
    let current = true;
    const refresh = () => api().then(data => { if (current) { setIndex(data); setError(''); } }).catch(e => { if (current) setError(String(e.message)); });
    refresh(); const timer = setInterval(refresh, 3000);
    return () => { current = false; clearInterval(timer); };
  }, []);
  useEffect(() => {
    if (!selected) { setRun(undefined); return; }
    let current = true;
    const refresh = () => api(`/${selected}`).then(data => { if (current) setRun(data); }).catch(e => { if (current) setError(e.message); });
    refresh(); const timer = setInterval(refresh, 2500);
    return () => { current = false; clearInterval(timer); };
  }, [selected]);
  const submit = async (action: 'prepare' | 'generate') => {
    setError(''); setSubmitting(true);
    try { const data = await api('', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ repository, strategy, task, action }) }); setRun(undefined); setSelected(data.id); setIndex(await api()); }
    catch (e) { setError((e as Error).message); }
    finally { setSubmitting(false); }
  };
  const items = run?.output?.items.filter(item => kind === 'all' || item.kind === kind) || [];
  return <section aria-label="Context generation">
    <p>Repository + task → Luna inspects files → security context.</p>
    <div className="filters"><label>Repository<select value={repository} onChange={e => { setRepository(e.target.value); setTask(task.replaceAll(repository, e.target.value)); }}>{(index?.targets || ['ApoMario', 'ApoIcarus']).map(t => <option key={t}>{t}</option>)}</select></label><label>Acquisition strategy<select value={strategy} onChange={e => setStrategy(e.target.value)}>{strategies.map(s => <option key={s.id} value={s.id}>{s.label}</option>)}</select></label></div>
    <p className="note">{strategies.find(s => s.id === strategy)?.description} Each attempt starts fresh; Luna chooses what to search and read.</p>
    <label>Feature task<textarea rows={3} value={task} onChange={e => setTask(e.target.value)} /></label>
    <div className="generation-actions"><button disabled={!index?.adapterReady || submitting || !task.trim()} onClick={() => submit('generate')}>Generate context</button><button disabled={!index?.local || submitting || !task.trim()} onClick={() => submit('prepare')}>Inspect inputs</button><span className="note">gpt-5.6-luna · medium · up to 12 turns</span></div>
    {index && !index.adapterReady && <p className="note">Set ASTERIA_ADAPTER_COMMAND in your local environment or gitignored .env.local, then restart the dev server.</p>}
    {error && <p role="alert">{error}</p>}
    <details><summary>Strategy definitions and output format</summary><table><thead><tr><th>Strategy</th><th>Acquisition objective</th></tr></thead><tbody>{strategies.map(s => <tr key={s.id}><th>{s.label}</th><td>{s.description}</td></tr>)}</tbody></table><p>All strategies receive the same task and repository index, with the same read/search tools and budget. Outputs distinguish security properties, suspected existing risks, risks introduced by the change, and unknowns. These are model claims requiring review.</p><p>Previous audits, static C1–C4 records and evaluator results are not supplied. Outputs remain local until explicitly exported. The security follow-up freezes six designated acquisitions. A treatment links to the exact context it uses.</p></details>
    <h2>Generations</h2>
    <div className="table-scroll"><table><thead><tr><th>Started</th><th>Repository / strategy</th><th>Task</th><th>Status / generator</th><th>Turns</th><th>Items</th></tr></thead><tbody>{index?.runs.map(r => <tr key={r.id} className={selected === r.id ? 'selected' : ''}><td><button className="link" onClick={() => { setRun(undefined); setSelected(r.id); }}>{new Date(r.startedAt).toLocaleString()}</button><small>{r.id.slice(-8)}</small></td><td>{r.repository}<small>{strategies.find(s => s.id === r.strategy)?.label}</small></td><td><details><summary>{r.task.length > 160 ? `${r.task.slice(0, 157)}…` : r.task}</summary><p>{r.task}</p></details></td><td>{label(r.status)}<small><code>{r.generatorHash?.slice(0, 8)}</code></small></td><td>{r.turns}</td><td>{r.items ?? '—'}</td></tr>)}</tbody></table></div>
    {!index?.runs.length && <p className="empty">No context generated yet.</p>}
    {run && <>
      <h2>Output · {run.repository} · {strategies.find(s => s.id === run.strategy)?.label}</h2>
      <p>{label(run.status)} · {run.turns.length}/{run.maxTurns} turns · {run.inspectedFiles ?? 0}/{run.sourceFiles} files inspected · {run.inspectedLines ?? 0}/{run.sourceLines} source lines shown</p>
      <p className="note">{run.task}</p>
      {run.settingsVerified === false && <p role="alert">The service did not verify effective model settings. Preserve this distinction when interpreting the output.</p>}
      {run.errorCategory && <p role="alert">{label(run.errorCategory)}. The recorded attempt will not be retried automatically.</p>}
      <div className="generation-actions"><button onClick={() => download(run, `${run.id}.json`)}>Export generation JSON</button>{run.output && <button onClick={() => download(run.output, `${run.id}-context.json`)}>Export context JSON</button>}</div>
      {run.output ? <>
        <p className="context-summary">{run.output.summary}</p>
        <p className="note">{run.citationChecks?.matched}/{run.citationChecks?.total} citations match inspected source · {run.citationChecks?.uncitedItems} uncited items. {run.citationChecks?.limitation}</p>
        <div className="filters"><label>Output type<select value={kind} onChange={e => setKind(e.target.value)}><option value="all">All ({run.output.items.length})</option>{['security_property', 'existing_risk', 'change_risk', 'unknown'].map(k => <option key={k} value={k}>{label(k)} ({run.output!.items.filter(i => i.kind === k).length})</option>)}</select></label></div>
        <div className="table-scroll"><table className="generated-context"><thead><tr><th>Type / topic</th><th>Security context</th><th>Task relevance / proposed check</th><th>Source evidence</th></tr></thead><tbody>{items.map(item => <tr key={item.id}><td>{label(item.kind)}<small>{item.topic}</small><small>{item.cwes.join(', ')}</small></td><td>{item.statement}</td><td>{item.task_relevance}<details><summary>Suggested check</summary><p>{item.suggested_check}</p></details></td><td>{item.evidence.length ? item.evidence.map((e, i) => <details key={i}><summary>{e.path.split('/').at(-1)}:{e.start_line}–{e.end_line} · {e.inspected ? 'matched' : 'unmatched'}</summary><code>{e.path}</code><pre>{e.quote}</pre><p className="source-path">SHA-256 {e.sourceSha256}</p></details>) : 'No citation'}</td></tr>)}</tbody></table></div>
        <details><summary>Limitations stated by Luna</summary>{run.output.limitations.map((l, i) => <p key={i}>{l}</p>)}</details>
      </> : <p className="empty">{run.status === 'prepared' ? 'Input snapshot prepared; no model request submitted.' : 'No final context returned yet. The acquisition trace below shows progress.'}</p>}
      <details><summary>Exact starting input and repository snapshot</summary><p className="source-path">Snapshot SHA-256 {run.snapshotFingerprint}</p><details><summary>Generator source hashes</summary>{Object.entries(run.generatorHashes || {}).map(([path, hash]) => <p className="source-path" key={path}>{path}<br />{hash}</p>)}</details><p>{run.sourceFiles} readable files; {run.omittedFiles} omitted files / binary containers. Embedded Java source in JAR files is included.</p><button onClick={async () => { try { download(await api(`/${run.id}/snapshot`), `${run.id}-snapshot.json`); } catch (e) { setError((e as Error).message); } }}>Export input snapshot</button><pre>{run.initialPrompt}</pre></details>
      <details open><summary>Acquisition trace ({run.turns.length} turns)</summary>{run.turns.map(turn => <details key={turn.number}><summary>Turn {turn.number} · {turn.status} · {turn.response?.usage?.input_tokens ?? '—'} input / {turn.response?.usage?.output_tokens ?? '—'} output tokens</summary><p className="source-path">Request SHA-256 {turn.requestSha256}</p><pre>{turn.response?.output_text || 'Waiting for the model response…'}</pre>{turn.toolResult && <pre>{JSON.stringify(turn.toolResult, null, 2)}</pre>}<details><summary>Exact model request</summary><pre>{JSON.stringify(turn.request, null, 2)}</pre></details></details>)}</details>
    </>}
  </section>;
}
