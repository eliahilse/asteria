import type { TestDefinition } from './types';
import { statusLabel } from './analysis';

type Check = { suite: string; name: string; status: keyof typeof statusLabel; detail: string };
type Evaluation = { checks?: Check[]; security?: { checks?: Check[] }; processes?: { command: string[]; exitCode: number; stdout: string; stderr: string }[] };
export type AttemptEvidenceData = { observation: { runId: string; response?: { output_text: string }; request: unknown }; evaluation: Evaluation | null };
export function AttemptEvidence({ evidence, definitions }: { evidence: AttemptEvidenceData; definitions: TestDefinition[] }) {
  const report = evidence.evaluation;
  const checks = [...(report?.checks ?? []), ...(report?.security?.checks ?? [])];
  return <>
    <h3>{evidence.observation.runId}</h3>
    {checks.length > 0 && <div className="table-scroll"><table className="attempt-checks"><thead><tr><th>Test</th><th>Outcome</th><th>Diagnostic</th></tr></thead><tbody>{checks.map(c => {
      const id = `${c.suite}.${c.name}`;
      return <tr key={id}><td>{definitions.find(d => d.id === id)?.label ?? c.name}<small>{c.suite}</small></td><td>{statusLabel[c.status]}</td><td>{c.detail}</td></tr>;
    })}</tbody></table></div>}
    {!report && <p>No evaluation report. Inspect the response status and submitted request below.</p>}
    {report?.processes?.filter(p => p.exitCode !== 0).map((p, i) => <details key={i} open><summary>Compiler / test process exited with {p.exitCode}</summary><code>{p.command.join(' ')}</code><pre>{p.stderr || p.stdout}</pre></details>)}
    <details><summary>Complete evaluation record and provenance</summary><pre>{JSON.stringify(report, null, 2)}</pre></details>
    <details><summary>Original model response</summary><pre>{evidence.observation.response?.output_text ?? 'No response'}</pre></details>
    <details><summary>Exact submitted request</summary><pre>{JSON.stringify(evidence.observation.request, null, 2)}</pre></details>
  </>;
}
