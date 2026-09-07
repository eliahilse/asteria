import ExcelJS from 'exceljs';
import type { Artifact, Dataset, Run } from './types';
import { cells, wilson } from './analysis';

type Scalar = string | number | boolean | null | undefined;
type Row = Record<string, Scalar>;
export type Selection = { cohort: string; model: string; strategy: string; security: string; query: string };

export function textChunks(text: string, size = 30000): string[] {
  const parts: string[] = [];
  let start = 0;
  while (start < text.length) {
    let end = Math.min(start + size, text.length);
    // Do not split a UTF-16 surrogate pair at the Excel cell boundary.
    if (end < text.length && text.charCodeAt(end - 1) >= 0xd800 && text.charCodeAt(end - 1) <= 0xdbff) end--;
    parts.push(text.slice(start, end)); start = end;
  }
  return parts.length ? parts : [''];
}

export async function buildWorkbook(data: Dataset, runs: Run[], selection: Selection, read: (a: Artifact) => Promise<Uint8Array> = async a => {
  const response = await fetch(a.href);
  if (!response.ok) throw new Error(`Could not export evidence: ${a.path}`);
  return new Uint8Array(await response.arrayBuffer());
}) {
  const workbook = new ExcelJS.Workbook();
  workbook.creator = 'Elia'; workbook.subject = 'Asteria Highscore evidence export';
  const longText: Row[] = [];
  function sheet(name: string, rows: Row[], emptyColumns: string[] = []) {
    const ws = workbook.addWorksheet(name, { views: [{ state: 'frozen', ySplit: 1 }] });
    const columns = rows.length ? [...new Set(rows.flatMap(r => Object.keys(r)))] : emptyColumns;
    ws.columns = columns.map(key => ({ header: key, key, width: /detail|reason|path|note|text/.test(key) ? 70 : /id|sha256/.test(key) ? 36 : 22 }));
    rows.forEach((row, rowIndex) => {
      const safe: Row = {};
      for (const [key, value] of Object.entries(row)) {
        if (typeof value === 'string' && value.length > 30000) {
          const ref = `${name}!${rowIndex + 2}:${key}`;
          textChunks(value).forEach((chunk, index) => longText.push({ ref, part: index + 1, text: chunk }));
          safe[key] = `See Long text: ${ref}`;
        } else safe[key] = value ?? null;
      }
      ws.addRow(safe);
    });
    ws.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } };
    ws.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FF233C31' } };
    ws.getRow(1).height = 25;
    if (columns.length) ws.autoFilter = { from: { row: 1, column: 1 }, to: { row: Math.max(1, rows.length + 1), column: columns.length } };
    ws.eachRow((row, index) => { if (index > 1) { row.alignment = { vertical: 'top', wrapText: true }; if (index % 2 === 0) row.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF5F7F0' } }; } });
    return ws;
  }

  sheet('Read me', [
    { field: 'source_dataset_sha256', value: data.fingerprint },
    { field: 'exported_at', value: new Date().toISOString() },
    { field: 'selection', value: JSON.stringify(selection) },
    { field: 'included_attempts', value: String(runs.length) },
    { field: 'threat_model', value: data.threatModel },
    { field: 'denominators', value: 'Compilation and functionality use all selected attempts per cell. Source findings use reviewed outputs. Security suite pass uses assessed outputs; missing counts are explicit.' },
    { field: 'intervals', value: 'Two-sided 95% Wilson score intervals, z=1.959963984540054. Descriptive exploratory estimates; no causal claims. Tests within a run are not independent replicates.' },
    { field: 'missing_values', value: 'Blank means unrecorded. not_run, compile_error and infrastructure_error are not executed pass/fail observations.' },
    { field: 'cohort_selection', value: data.cohorts.find(c => c.id === selection.cohort)?.selection ?? 'See Cohorts sheet.' },
    { field: 'evidence_text', value: 'Exact selected prompts, original responses, generated code and context artifacts are split into ordered chunks. Concatenate by SHA-256 and part; encoding is declared. SHA-256 is verified before export.' },
    { field: 'manifest_scope', value: 'Artifacts lists the entire source dataset manifest. Evidence text embeds the artifacts used by the selected runs plus the historical context block.' },
    ...data.limitations.map((value, i) => ({ field: `limitation_${i + 1}`, value })),
  ]);
  sheet('Cohorts', data.cohorts.map(c => ({ ...c })));
  sheet('Conditions', cells(runs).map(c => ({ cohort: c.run.cohort, model: c.run.model, reasoning: c.run.reasoning, temperature: c.run.temperature, strategy: c.run.strategy, condition: c.run.condition,
    base_context: c.run.baseContext, security_context: c.run.securityContext, security_type_count: c.run.contextTypes.length, security_fact_count: c.run.factIds.length,
    attempts: c.n, compiled: c.compiled, compile_denominator: c.n, compile_rate: c.compiled / c.n, compile_ci_low: wilson(c.compiled,c.n)?.[0], compile_ci_high: wilson(c.compiled,c.n)?.[1],
    functional: c.functional, functional_denominator: c.n, functional_rate: c.functional/c.n, functional_ci_low: wilson(c.functional,c.n)?.[0], functional_ci_high: wilson(c.functional,c.n)?.[1],
    outputs_with_findings: c.findings, reviewed_outputs: c.reviewed, unreviewed_outputs: c.n-c.reviewed,
    security_suite_pass: c.securityPass, security_assessed: c.securityAssessed, security_unevaluated: c.n-c.securityAssessed, joint_success_observed: c.joint,
  })), ['cohort','model','condition','attempts']);
  sheet('Runs', runs.map(r => ({ id: r.id, cohort: r.cohort, model: r.model, served_model: r.servedModel, reasoning: r.reasoning, temperature: r.temperature,
    strategy: r.strategy, condition: r.condition, base_context: r.baseContext, security_context: r.securityContext, security_type_count: r.contextTypes.length, security_fact_count: r.factIds.length,
    repetition: r.repetition, prompt_sha256: r.prompt?.sha256, prompt_chars: r.promptChars, input_tokens: r.usage.prompt_tokens, output_tokens: r.usage.completion_tokens,
    reasoning_tokens: r.usage.reasoning_tokens, cached_input_tokens: r.usage.cache_read_tokens, queue_elapsed_seconds: r.elapsedSeconds, billed_cost_usd: r.costUsd,
    submitted_at: r.submittedAt, finish_reason: r.finishReason, compile_status: r.compileStatus, compile_detail: r.compileDetail, functional_success: r.functionalSuccess,
    source_assessment: r.assessment, source_review_notes: r.reviewNotes.join('\n'), security_suite_status: r.securityEvaluation?.status ?? 'not_run',
    security_protocol: r.securityEvaluation?.protocol, source: r.source })), ['id','cohort']);
  sheet('Historical checks', runs.flatMap(r => r.tests.map(t => ({ run_id: r.id, ...t }))), ['run_id','suite','name','status','detail','source']);
  sheet('New security checks', runs.flatMap(r => (r.securityEvaluation?.checks ?? []).map(t => ({ run_id: r.id, protocol: r.securityEvaluation!.protocol, ...t }))), ['run_id','protocol','suite','name','status','detail','source']);
  sheet('Security evaluations', runs.filter(r => r.securityEvaluation).map(r => ({ run_id: r.id, protocol: r.securityEvaluation!.protocol, status: r.securityEvaluation!.status, source: r.securityEvaluation!.source,
    environment: JSON.stringify(r.securityEvaluation!.environment), input_hashes: JSON.stringify(r.securityEvaluation!.inputHashes ?? {}), detail: r.securityEvaluation!.detail })), ['run_id','protocol','status']);
  sheet('Published suite summaries', runs.flatMap(r => Object.entries(r.reportedSuites ?? {}).map(([suite, reported]) => ({ run_id: r.id, suite, reported, evidence_level: 'archived aggregate; individual observations not imported' }))), ['run_id','suite','reported']);
  sheet('Adjudicated findings', runs.flatMap(r => r.findings.map(f => ({ run_id: r.id, cwe: f.cwe, signature: f.signature, severity: f.severity, reason: f.reason, status: f.status,
    dynamic_proof: f.dynamicProof, evidence_path: f.evidence.path, evidence_sha256: f.evidence.sha256, cited_lines: f.lines.join(', ') }))), ['run_id','cwe','signature','status']);
  sheet('Scanner candidates', runs.flatMap(r => r.candidates.flatMap((c,i) => c.evidence.map(e => ({ run_id: r.id, candidate_index: i, cwe: c.cwe, signature: c.signature, category: c.category, severity: c.severity,
    evidence_file: e.file, evidence_line: e.line, evidence_code: e.code, evidence_level: 'candidate; not a confirmed vulnerability' })))), ['run_id','candidate_index','cwe','signature']);
  const usedFacts = data.facts.filter(f => runs.some(r => r.factIds.includes(f.id)));
  sheet('Context facts', usedFacts.map(f => ({ id: f.id, type: f.type, scope: f.scope, status: f.status, cwes: f.cwes.join(', '), text: f.text, extraction: f.extraction, source: f.source.path, source_sha256: f.source.sha256 })), ['id','type','scope','text']);
  sheet('Run context links', runs.flatMap(r => r.factIds.map(id => ({ run_id: r.id, fact_id: id }))), ['run_id','fact_id']);
  sheet('Attachments', runs.flatMap(r => r.attachments.map(a => ({ run_id: r.id, ...a }))), ['run_id','kind','name','bytes','sha256']);
  sheet('Artifacts', data.artifacts.map(a => ({ ...a })));
  const included = new Map<string, Artifact>();
  for (const r of runs) for (const a of [r.prompt, r.rawResponse, ...r.code, ...r.findings.map(f => f.evidence)]) if (a) included.set(a.sha256,a);
  for (const f of usedFacts) included.set(f.source.sha256, f.source);
  const textRows: Row[] = [];
  for (const artifact of included.values()) {
    const bytes = await read(artifact);
    const hash = [...new Uint8Array(await crypto.subtle.digest('SHA-256', new Uint8Array(bytes).buffer))].map(b => b.toString(16).padStart(2,'0')).join('');
    if (hash !== artifact.sha256) throw new Error(`Evidence hash mismatch: ${artifact.path}`);
    let content: string, encoding = 'utf-8';
    try {
      content = new TextDecoder('utf-8', { fatal: true }).decode(bytes);
      if (/[\x00-\x08\x0b\x0c\x0e-\x1f]/.test(content)) throw new Error('XML control character');
    } catch {
      encoding = 'base64'; let binary = ''; for (const b of bytes) binary += String.fromCharCode(b); content = btoa(binary);
    }
    textChunks(content).forEach((chunk,index) => textRows.push({ sha256: artifact.sha256, path: artifact.path, encoding, part: index+1, text: chunk }));
  }
  sheet('Evidence text', textRows, ['sha256','path','encoding','part','text']);
  sheet('Long text', [...longText], ['ref','part','text']);
  return workbook;
}

export function downloadBlob(blob: Blob, name: string) {
  const url = URL.createObjectURL(blob), a = document.createElement('a');
  a.href = url; a.download = name; a.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
}

export async function exportXlsx(data: Dataset, runs: Run[], selection: Selection) {
  const workbook = await buildWorkbook(data,runs,selection);
  const buffer = await workbook.xlsx.writeBuffer();
  downloadBlob(new Blob([new Uint8Array(buffer)], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }), 'asteria-highscore-evidence.xlsx');
}
