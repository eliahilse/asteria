import ExcelJS from 'exceljs';
import type { MatrixData } from './experiment';
import { textChunks } from './export';
import { matrixComparisons } from './experiment-analysis';
import { combinationRows, statColumns } from './combination-stats';
type Row = Record<string, string | number | boolean | null | undefined>;
export function experimentWorkbook(data: MatrixData) {
  const wb = new ExcelJS.Workbook(), overflow: Row[] = [];
  const sheet = (name: string, rows: Row[], columns: string[] = []) => {
    const s = wb.addWorksheet(name, { views: [{ state: 'frozen', ySplit: 1 }] });
    s.columns = (rows.length ? [...new Set(rows.flatMap(r => Object.keys(r)))] : columns).map(key => ({ header: key, key, width: /detail|statement/.test(key) ? 70 : 28 }));
    rows.forEach((row, i) => { const safe: Row = {}; for (const [k, v] of Object.entries(row)) { if (typeof v === 'string' && v.length > 30000) { const ref = `${name}:${i + 2}:${k}`; textChunks(v).forEach((text, part) => overflow.push({ ref, part: part + 1, text })); safe[k] = `Long text: ${ref}`; } else safe[k] = v ?? null; } s.addRow(safe); });
    s.getRow(1).font = { bold: true }; return s;
  };
  sheet('Combinations', combinationRows(data).map(r => ({ study: r.study, cohort: r.phase, method: r.method, paper_context: r.paper.join('+') || 'None', security_strategy: r.security,
    ...Object.fromEntries(statColumns.map(c => [c.label, r.values[c.key]])), security_issues_detected: r.issues.detected,
    security_checks_evaluated: r.issues.evaluated, security_checks_unresolved: r.issues.expected - r.issues.evaluated, security_issues_delta: r.issues.delta })));
  sheet('Studies', data.studies.map(s => ({ id: s.plan.id, phase: s.plan.phase, manifest_sha256: s.plan.fingerprint, model: s.plan.model, requested_reasoning: s.plan.reasoning, complete: s.summary.complete, selected: s.summary.selected.join(', '), deviations: s.plan.deviations.join('\n') })));
  sheet('Matrix', data.studies.flatMap(s => s.plan.conditions.map(c => { const r = s.summary.conditions.find(r => r.id === c.id)!; return { study: s.plan.id, condition: c.id, method: c.strategy, paper_context: c.baseContext, security_context: c.securityStrategy, paper_prompt_id: c.paperPromptId, attempts: r.attempts, planned: r.planned, pending: r.pending, compilations: r.compiled, full_functional: r.fullFunctional, full_functional_rate: r.attempts ? r.fullFunctional / r.attempts : null, functional_checks_passed: r.functionalChecksPassed, functional_checks_all_attempts: 16 * r.attempts, settings_unverified: r.unverifiedSettings, transport_errors: r.transportErrors, context_acquisition_id: c.contextAcquisitionId, prompt_characters: c.promptCharacters, prompt_bytes: c.promptBytes, prompt_sha256: c.promptSha256 }; })));
  sheet('Selection', data.studies.flatMap(s => Object.entries(s.summary.ranking ?? {}).flatMap(([method, conditions]) => conditions.map((condition, i) => ({ study: s.plan.id, method, rank: i + 1, condition, selected: s.summary.selected.includes(condition) })))), ['study', 'method', 'rank', 'condition', 'selected']);
  sheet('Context acquisitions', data.studies.flatMap(s => (s.plan.acquisitions ?? []).map(a => ({ study: s.plan.id, id: a.id, method: a.method, strategy: a.strategy, status: a.status, settings_verified: a.settingsVerified, items: a.items, citations_matched: a.citationChecks.matched, citations_total: a.citationChecks.total, uncited_items: a.citationChecks.uncitedItems, ...a.citationChecks.itemsByKind, snapshot_sha256: a.snapshotFingerprint, task_sha256: a.taskSha256 }))), ['study', 'id', 'method', 'strategy', 'items', 'citations_matched', 'citations_total', 'uncited_items']);
  sheet('Source hashes', data.studies.flatMap(s => Object.entries(s.plan.sourceHashes ?? {}).map(([path, sha256]) => ({ study: s.plan.id, path, sha256 }))), ['study', 'path', 'sha256']);
  sheet('Test rates', data.studies.flatMap(s => s.summary.conditions.flatMap(c => c.checks.map(t => ({ study: s.plan.id, condition: c.id, ...t })))));
  sheet('Security comparisons', data.studies.flatMap(s => matrixComparisons(s).map(c => ({ study: s.plan.id, parent_condition: c.parentCondition, control: c.control, treatment: c.treatment, security_strategy: c.securityStrategy, test: c.testId,
    control_pass: c.controlCounts.pass, control_tested: c.controlCounts.executed, control_attempts: c.controlCounts.attempts,
    treatment_pass: c.treatmentCounts.pass, treatment_tested: c.treatmentCounts.executed, treatment_attempts: c.treatmentCounts.attempts,
    delta_tested_pp: c.testedDeltaPp, delta_all_attempt_pp: c.allAttemptDeltaPp }))), ['study', 'parent_condition', 'control', 'treatment', 'test', 'delta_tested_pp', 'delta_all_attempt_pp']);
  sheet('Attempts', data.studies.flatMap(s => s.runs.map(r => ({ study: s.plan.id, run_id: r.runId, condition: r.condition, repetition: r.repetition, status: r.status, evaluation_status: r.evaluationStatus, compilation: r.mainCompilation, full_functional: r.functionalSuccess, first_full_functional: r.firstFunctionalSuccess, model_submissions: r.submissions, first_compilation: r.firstCompilation, finish_reason: r.finishReason, input_tokens: r.usage?.input_tokens, output_tokens: r.usage?.output_tokens }))), ['study', 'run_id', 'status']);
  sheet('Check observations', data.studies.flatMap(s => s.runs.flatMap(r => r.checks.map(c => ({ study: s.plan.id, run_id: r.runId, condition: r.condition, ...c })))), ['study', 'run_id', 'suite', 'name', 'status', 'detail']);
  sheet('Long text', [...overflow], ['ref', 'part', 'text']); return wb;
}
export async function exportExperiment(data: MatrixData, format: 'json' | 'xlsx') {
  const body = format === 'json' ? JSON.stringify(data, null, 2) : new Uint8Array(await experimentWorkbook(data).xlsx.writeBuffer());
  const url = URL.createObjectURL(new Blob([body], { type: format === 'json' ? 'application/json' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }));
  const a = document.createElement('a'); a.href = url; a.download = `highscore-matrix.${format}`; a.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
}
