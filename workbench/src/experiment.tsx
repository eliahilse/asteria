import { useEffect, useRef, useState } from 'react';
import { SecurityComparison } from './experiment-comparison';
import { AttemptEvidence, type AttemptEvidenceData } from './attempt-evidence';
import type { TestDefinition } from './types';
import { rate, statusLabel } from './analysis';

export type MatrixCheck = { id: string; suite: string; name: string; kind: string; pass: number; fail: number; not_run: number; unknown: number; compile_error: number; infrastructure_error: number; attempts: number; executed: number; passRate: number | null; allAttemptRate: number | null };
export type MatrixCondition = { id: string; strategy: string; paperPromptId: number; baseContext: string; contextTypes: string[]; securityStrategy: string; parentCondition?: string; contextAcquisitionId?: string; repetitions: number; promptSha256: string; promptCharacters: number; attachments: { name: string; kind: string; sha256: string }[] };
export type MatrixRun = { runId: string; condition: string; repetition: number; status: string; errorCategory: string | null; evaluationStatus: string | null; mainCompilation: string | null; functionalSuccess: boolean | null; finishReason: string | null; usage: Record<string, number | null> | null; checks: { name: string; suite: string; status: keyof typeof statusLabel; detail: string }[] };
export type MatrixStudy = { plan: { id: string; phase: string; fingerprint: string; model: string; reasoning: string; conditions: MatrixCondition[]; schedule: unknown[]; axes: { method: string[]; paperContext: string[]; securityContext: string[] }; selection?: { perMethod: number; order: string[] }; deviations: string[]; followup?: { contextAcquisition: string } }; summary: { complete: boolean; selected: string[]; ranking: Record<string, string[]>; conditions: { id: string; attempts: number; planned: number; pending: number; compiled: number; fullFunctional: number; functionalChecksPassed: number; securityChecksPassed: number; securityChecksExecuted: number; unverifiedSettings: number; transportErrors: number; checks: MatrixCheck[] }[] }; runs: MatrixRun[] };
export type MatrixData = { local: boolean; studies: MatrixStudy[] };
const names: Record<string, string> = { none: 'No security context', overview: 'Security overview', task: 'Task-focused security', flows: 'Security data flows' };
const label = (s: string) => s.replaceAll('_', ' ');
async function json(url: string) { const r = await fetch(url); if (!r.ok || !r.headers.get('content-type')?.includes('application/json')) throw new Error('Experiment data unavailable'); const d = await r.json(); if (d.error) throw new Error(d.error); return d; }

export function Experiment({ definitions }: { definitions: TestDefinition[] }) {
  const [data, setData] = useState<MatrixData>(), [studyId, setStudyId] = useState(''), [conditionId, setConditionId] = useState('');
  const [error, setError] = useState(''), [suite, setSuite] = useState('functional'), [metric, setMetric] = useState('checks'), [prompt, setPrompt] = useState('');
  const [evidence, setEvidence] = useState<AttemptEvidenceData>(), [busy, setBusy] = useState(false);
  useEffect(() => {
    let current = true;
    const refresh = async () => {
      try {
        let d;
        try { d = await json('api/experiment'); }
        catch (e) { if (import.meta.env.DEV) throw e; d = await json('data/matrix.json'); }
        if (current) { setData(d); setError(''); }
      } catch (e) { if (current) setError((e as Error).message); }
    };
    refresh(); const timer = setInterval(refresh, 5000);
    return () => { current = false; clearInterval(timer); };
  }, []);
  const study = data?.studies.find(s => s.plan.id === studyId) ?? data?.studies[0];
  const baseline = data?.studies.find(s => s.plan.phase === 'screening');
  const condition = study?.plan.conditions.find(c => c.id === conditionId);
  const summary = study?.summary.conditions.find(c => c.id === conditionId);
  useEffect(() => { setPrompt(''); setEvidence(undefined); }, [conditionId, study?.plan.id]);
  const currentCell = useRef(''); currentCell.current = `${study?.plan.id}:${conditionId}`;
  const choose = (id: string) => { setConditionId(id); setEvidence(undefined); };
  const download = async (format: 'json' | 'xlsx') => { if (!data) return; setBusy(true); try { const { exportExperiment } = await import('./experiment-export'); await exportExperiment(data, format); } catch (e) { setError((e as Error).message); } finally { setBusy(false); } };
  if (!study || !baseline) return <p role={error ? 'alert' : 'status'}>{error || 'Loading the original context matrix…'}</p>;
  const attempted = study.summary.conditions.reduce((n, c) => n + c.attempts, 0), pending = study.summary.conditions.reduce((n, c) => n + c.pending, 0);
  return <section aria-label="Main experiment">
    <div className="filters"><label>Stage<select aria-label="Stage" value={study.plan.id} onChange={e => { setStudyId(e.target.value); setConditionId(''); }}>{data!.studies.map(s => <option key={s.plan.id} value={s.plan.id}>{s.plan.phase === 'screening' ? 'Original matrix replay' : 'Selected conditions + security'}</option>)}</select></label><label>Cell measure<select aria-label="Cell measure" value={metric} onChange={e => setMetric(e.target.value)}><option value="checks">Functional checks</option><option value="full">Fully functional runs</option><option value="security">Security checks</option><option value="compiled">Game compilation</option></select></label><button disabled={busy} onClick={() => download('json')}>Export results JSON</button><button disabled={busy} onClick={() => download('xlsx')}>Export results XLSX</button></div>
    <p>{study.plan.model} · requested {study.plan.reasoning} reasoning · {attempted}/{study.plan.schedule.length} attempts recorded · {pending} awaiting response or evaluation</p>
    <p className="note">Generation implements Highscore in ApoMario using its target source. Reuse additionally supplies ApoIcarus’s Highscore implementation and prioritizes adapting it. S = structure, F = functionality, B = behavior. “None” still includes the task and target source.</p>
    {!data!.local && <p className="note">Public plan only. Run the local explorer to see private attempts and evaluations.</p>}
    {error && <p role="alert">{error}</p>}
    <div className="table-scroll"><table className="experiment-matrix"><thead><tr><th>Method</th><th>Paper context</th>{baseline.plan.axes.securityContext.map(s => <th key={s}>{names[s]}</th>)}</tr></thead><tbody>{baseline.plan.conditions.map(base => <tr key={base.id}><th>{base.strategy}</th><th>{base.baseContext}</th>{baseline.plan.axes.securityContext.map(security => {
      const c = study.plan.conditions.find(c => (c.parentCondition ?? c.id) === base.id && c.securityStrategy === security);
      const r = c && study.summary.conditions.find(r => r.id === c.id);
      return <td key={security} className={c?.id === conditionId ? 'selected' : ''}>{c && r ? <><button className="link" onClick={() => choose(c.id)} aria-label={`${base.strategy} ${base.baseContext} ${names[security]}`}>{r.attempts ? metric === 'checks' ? `${r.functionalChecksPassed}/${16 * r.attempts} functional checks` : metric === 'security' ? `${r.securityChecksPassed}/${11 * r.attempts} security checks` : metric === 'compiled' ? `${r.compiled}/${r.attempts} compiled` : `${r.fullFunctional}/${r.attempts} fully functional` : `0/${r.planned} attempts`}</button><small>{r.fullFunctional}/{r.attempts} fully functional · {r.compiled}/{r.attempts} compiled</small><small> {r.attempts}/{r.planned} recorded</small></> : <span className="note">{study.plan.phase === 'screening' ? 'After selection' : 'Not selected'}</span>}</td>;
    })}</tr>)}</tbody></table></div>
    <p className="note">Check totals use all attempts × 16 functional or × 11 security tests; checks within an attempt are correlated. Fully functional means all 16 functional checks pass. Open a cell for individual test rates and missing outcomes.</p>
    <details><summary>Selection and comparison protocol</summary><p>Rank separately within Generation and Reuse: {baseline.plan.selection?.order.join('; ')}. Select the top {baseline.plan.selection?.perMethod} from each only after all baseline attempts and evaluations finish. Security outcomes do not select winners.</p><p>The next stage repeats selected controls with fresh responses alongside each security case. It does not compare treatments against their selection-winning responses. Matching repetition numbers do not imply identical model seeds.</p><p>{baseline.plan.followup?.contextAcquisition}</p>{study.plan.deviations.map((d, i) => <p className="note" key={i}>{d}</p>)}<p className="source-path">Manifest SHA-256 {study.plan.fingerprint}</p></details>
    {baseline.summary.complete && <p>Selected baseline conditions: {baseline.summary.selected.join(', ')}.</p>}
    {condition && summary && <>
      <h2>{condition.strategy} · {condition.baseContext} · {names[condition.securityStrategy]}</h2>
      <p>{summary.fullFunctional}/{summary.attempts} fully functional ({rate(summary.fullFunctional, summary.attempts)}) · {summary.compiled}/{summary.attempts} compiled · {summary.unverifiedSettings} with unverified effective settings · {summary.transportErrors} transport / identity errors</p>
      <details key={condition.id} onToggle={async e => { if (e.currentTarget.open && !prompt) try { const text = data!.local ? (await json(`api/experiment/${study.plan.id}/prompt/${condition.id}`)).text : await (await fetch(`data/matrix-prompts/${condition.promptSha256}.txt`)).text(); if (currentCell.current === `${study.plan.id}:${condition.id}`) setPrompt(text); } catch (e) { setError((e as Error).message); } }}><summary>Exact prompt and attachments ({condition.promptCharacters.toLocaleString()} characters)</summary><p className="source-path">SHA-256 {condition.promptSha256}</p>{condition.attachments.map((a, i) => <p className="note" key={i}>{a.kind}: {a.name}</p>)}<pre>{prompt || 'Loading…'}</pre></details>
      {condition.contextAcquisitionId && <p><a href={`?view=generation&context=${condition.contextAcquisitionId}`}>Inspect the acquired security context and source trace</a></p>}
      <div className="filters"><label>Test suite<select aria-label="Test suite" value={suite} onChange={e => setSuite(e.target.value)}><option value="functional">All functional</option><option value="unit">Unit</option><option value="invoked">Invoked integration</option><option value="autonomous">Autonomous integration</option><option value="security">Security</option><option value="all">All checks</option></select></label></div>
      <div className="table-scroll"><table className="matrix-checks"><thead><tr><th>Test / fixture</th><th>Pass</th><th>Fail</th><th>Not run</th><th>Unknown</th><th>Compile error</th><th>Environment error</th><th>Pass / tested</th><th>Pass / all attempts</th></tr></thead><tbody>{summary.checks.filter(c => suite === 'all' || c.kind === suite || c.suite === suite).map(c => { const d = definitions.find(d => d.id === c.id); return <tr key={c.id}><td><details><summary>{d?.label ?? c.name}</summary><p>{d?.fixture}</p><p>Expected: {d?.expected}</p><p>Limits: {d?.limits}</p><code>{c.id}</code></details></td><td>{c.pass}</td><td>{c.fail}</td><td>{c.not_run}</td><td>{c.unknown}</td><td>{c.compile_error}</td><td>{c.infrastructure_error}</td><td>{rate(c.pass, c.executed)}<small>{c.pass}/{c.executed}</small></td><td>{rate(c.pass, c.attempts)}<small>{c.pass}/{c.attempts}</small></td></tr>; })}</tbody></table></div>
      <SecurityComparison study={study} condition={condition} definitions={definitions} suite={suite} />
      <h3>Individual attempts</h3><div className="table-scroll"><table><thead><tr><th>Attempt</th><th>Response / evaluation</th><th>Compilation</th>{['Unit', 'Invoked', 'Autonomous', 'Security'].map(s => <th key={s}>{s} pass / tested</th>)}<th>Input / output tokens</th></tr></thead><tbody>{study.runs.filter(r => r.condition === condition.id).map(r => <tr key={r.runId}><td><button className="link" onClick={async () => { try { const result = await json(`api/experiment/${study.plan.id}/run/${r.runId}`); if (currentCell.current === `${study.plan.id}:${condition.id}`) setEvidence(result); } catch (e) { setError((e as Error).message); } }}>Repetition {r.repetition}</button><small>{r.runId}</small></td><td>{label(r.status)}<small>{r.evaluationStatus ?? 'Not evaluated'} · {r.finishReason}</small></td><td>{r.mainCompilation ?? 'Not evaluated'}</td>{['unit', 'invoked', 'autonomous', 'security_v1'].map(s => { const checks = r.checks.filter(c => c.suite === s); return <td key={s}>{checks.filter(c => c.status === 'pass').length}/{checks.filter(c => c.status === 'pass' || c.status === 'fail').length}</td>; })}<td>{r.usage?.input_tokens ?? '—'} / {r.usage?.output_tokens ?? '—'}</td></tr>)}</tbody></table></div>
      {evidence && <AttemptEvidence evidence={evidence} definitions={definitions} />}
    </>}
  </section>;
}
