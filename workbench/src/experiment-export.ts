import ExcelJS from 'exceljs';
import type { MatrixData } from './experiment';
import { textChunks } from './export';
type Row = Record<string, string | number | boolean | null | undefined>;
export function experimentWorkbook(data: MatrixData) {
  const wb = new ExcelJS.Workbook(), overflow: Row[] = [];
  const sheet = (name: string, rows: Row[], columns: string[] = []) => {
    const s = wb.addWorksheet(name, { views: [{ state: 'frozen', ySplit: 1 }] });
    s.columns = (rows.length ? [...new Set(rows.flatMap(r => Object.keys(r)))] : columns).map(key => ({ header: key, key, width: /detail|statement/.test(key) ? 70 : 28 }));
    rows.forEach((row, i) => { const safe: Row = {}; for (const [k, v] of Object.entries(row)) { if (typeof v === 'string' && v.length > 30000) { const ref = `${name}:${i + 2}:${k}`; textChunks(v).forEach((text, part) => overflow.push({ ref, part: part + 1, text })); safe[k] = `Long text: ${ref}`; } else safe[k] = v ?? null; } s.addRow(safe); });
    s.getRow(1).font = { bold: true }; return s;
  };
  sheet('Studies', data.studies.map(s => ({ id: s.plan.id, phase: s.plan.phase, manifest_sha256: s.plan.fingerprint, model: s.plan.model, requested_reasoning: s.plan.reasoning, complete: s.summary.complete, selected: s.summary.selected.join(', '), deviations: s.plan.deviations.join('\n') })));
  sheet('Matrix', data.studies.flatMap(s => s.plan.conditions.map(c => { const r = s.summary.conditions.find(r => r.id === c.id)!; return { study: s.plan.id, condition: c.id, method: c.strategy, paper_context: c.baseContext, security_context: c.securityStrategy, paper_prompt_id: c.paperPromptId, attempts: r.attempts, planned: r.planned, pending: r.pending, compilations: r.compiled, full_functional: r.fullFunctional, full_functional_rate: r.attempts ? r.fullFunctional / r.attempts : null, functional_checks_passed: r.functionalChecksPassed, functional_checks_all_attempts: 16 * r.attempts, settings_unverified: r.unverifiedSettings, transport_errors: r.transportErrors, prompt_sha256: c.promptSha256 }; })));
  sheet('Test rates', data.studies.flatMap(s => s.summary.conditions.flatMap(c => c.checks.map(t => ({ study: s.plan.id, condition: c.id, ...t })))));
  sheet('Attempts', data.studies.flatMap(s => s.runs.map(r => ({ study: s.plan.id, run_id: r.runId, condition: r.condition, repetition: r.repetition, status: r.status, evaluation_status: r.evaluationStatus, compilation: r.mainCompilation, full_functional: r.functionalSuccess, finish_reason: r.finishReason, input_tokens: r.usage?.input_tokens, output_tokens: r.usage?.output_tokens }))), ['study', 'run_id', 'status']);
  sheet('Check observations', data.studies.flatMap(s => s.runs.flatMap(r => r.checks.map(c => ({ study: s.plan.id, run_id: r.runId, condition: r.condition, ...c })))), ['study', 'run_id', 'suite', 'name', 'status', 'detail']);
  sheet('Long text', [...overflow], ['ref', 'part', 'text']); return wb;
}
export async function exportExperiment(data: MatrixData, format: 'json' | 'xlsx') {
  const body = format === 'json' ? JSON.stringify(data, null, 2) : new Uint8Array(await experimentWorkbook(data).xlsx.writeBuffer());
  const url = URL.createObjectURL(new Blob([body], { type: format === 'json' ? 'application/json' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }));
  const a = document.createElement('a'); a.href = url; a.download = `highscore-matrix.${format}`; a.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
}
