import ExcelJS from 'exceljs';
import type { Artifact, Dataset } from './types';
import { testComparisons } from './analysis';
export type Selection = { planId: string; baseline: string; condition: string; suite: string };
type Row = Record<string, string | number | boolean | null | undefined>;
export function textChunks(text: string, size = 30000) { const result: string[] = []; for (let start = 0; start < text.length;) { let end = Math.min(start + size, text.length); if (end < text.length && /[\uD800-\uDBFF]/.test(text[end - 1])) end--; result.push(text.slice(start, end)); start = end; } return result.length ? result : ['']; }
export async function buildWorkbook(data: Dataset, selection: Selection, read: (a: Artifact) => Promise<Uint8Array> = async a => { const r = await fetch(a.href); if (!r.ok) throw new Error(`Cannot read ${a.path}`); return new Uint8Array(await r.arrayBuffer()); }) {
  const wb = new ExcelJS.Workbook(), overflow: Row[] = [];
  function sheet(name: string, rows: Row[], columns: string[] = []) {
    const s = wb.addWorksheet(name, { views: [{ state: 'frozen', ySplit: 1 }] });
    s.columns = (rows.length ? [...new Set(rows.flatMap(r => Object.keys(r)))] : columns).map(key => ({ header: key, key, width: /detail|text|source|expected|fixture/.test(key) ? 60 : 25 }));
    for (const [i, row] of rows.entries()) { const safe: Row = {}; for (const [key, v] of Object.entries(row)) { if (typeof v === 'string' && v.length > 30000) { const ref = `${name}:${i + 2}:${key}`; textChunks(v).forEach((text, part) => overflow.push({ ref, part: part + 1, text })); safe[key] = `Long text: ${ref}`; } else safe[key] = v ?? null; } s.addRow(safe); }
    s.getRow(1).font = { bold: true }; if (s.columnCount) s.autoFilter = { from: { row: 1, column: 1 }, to: { row: Math.max(s.rowCount, 1), column: s.columnCount } }; return s;
  }
  const plan = data.experimentPlans.find(p => p.id === selection.planId)!;
  const runs = data.runs.filter(r => r.planId === plan.id), a = runs.filter(r => r.condition === selection.baseline), b = runs.filter(r => r.condition === selection.condition);
  const tests = data.tests.filter(t => selection.suite === 'all' || t.kind === selection.suite || t.suite === selection.suite);
  sheet('Read me', [{ field: 'source_dataset_sha256', value: data.fingerprint }, { field: 'selection', value: JSON.stringify(selection) }, { field: 'rates', value: 'Pass / (pass + fail). Missing and error outcomes remain separate. Delta is unpaired percentage points; different evaluator signatures withhold deltas.' }, { field: 'scope', value: 'Test comparisons use selected conditions/suite. Runs and observations contain all imported attempts in the selected study. Conditions and context records describe the planned study; no historical runs are included.' }]);
  sheet('Test comparisons', testComparisons(tests, a, b).map(r => { const row: Row = { test_id: r.test.id, test: r.test.label, cwes: r.test.cwes.join(', '), baseline: selection.baseline, treatment: selection.condition, delta_pp: r.delta, comparable: r.comparable }; for (const [prefix, t] of [['baseline', r.baseline], ['treatment', r.treatment]] as const) { for (const [k, v] of Object.entries(t)) if (k !== 'interval') row[`${prefix}_${k}`] = v as number | null; row[`${prefix}_ci_low`] = t.interval?.[0]; row[`${prefix}_ci_high`] = t.interval?.[1]; } return row; }));
  sheet('Test definitions', data.tests.map(t => ({ ...t, cwes: t.cwes.join(', '), source: t.source.path, source_sha256: t.source.sha256 })));
  sheet('Runs', runs.map(r => ({ id: r.id, condition: r.condition, model: r.model, reasoning: r.reasoning, response_status: r.status, compile_status: r.compileStatus, repetition: r.repetition, error: r.errorCategory, evaluation_signature: r.evaluationSignature, input_tokens: r.usage.input_tokens, output_tokens: r.usage.output_tokens, cost_usd: r.costUsd })), ['id', 'condition', 'response_status']);
  sheet('Check observations', runs.flatMap(r => r.checks.map(c => ({ run_id: r.id, condition: r.condition, ...c }))), ['run_id', 'condition', 'id', 'status', 'detail']);
  sheet('Conditions', plan.conditions.map(c => ({ id: c.id, stage: c.stage, strategy: c.strategy, base_context: c.baseContext, context_types: c.contextTypes.join(', '), fact_count: c.factCount, fact_ids: c.factIds.join(', '), prompt_chars: c.promptCharacters, prompt_sha256: c.promptSha256, planned_repetitions: c.repetitions })));
  sheet('Context records', data.contextExtraction.facts.map(f => ({ id: f.id, type: f.type, category: f.category, scope: f.scope, status: f.status, text: f.text, cwes: f.cwes.join(', '), source: f.source.path, source_sha256: f.source.sha256, line: f.source.line, snippet: f.source.snippet, rule: f.source.rule, audit_record: f.source.record })));
  sheet('Schedule', plan.schedule.map((r, i) => ({ order: i + 1, ...r })));
  const artifacts = new Map<string, Artifact>();
  for (const art of [plan.artifact, data.contextExtraction.artifact, ...plan.conditions.map(c => c.prompt), ...data.tests.map(t => t.source), ...runs.flatMap(r => [r.observation, r.evaluation].filter((a): a is Artifact => !!a))]) artifacts.set(art.sha256, art);
  const evidence: Row[] = [];
  for (const artifact of artifacts.values()) { const bytes = await read(artifact); const hash = [...new Uint8Array(await crypto.subtle.digest('SHA-256', new Uint8Array(bytes).buffer))].map(b => b.toString(16).padStart(2, '0')).join(''); if (hash !== artifact.sha256) throw new Error(`Evidence hash mismatch: ${artifact.path}`); let text: string, encoding = 'utf-8'; try { text = new TextDecoder('utf-8', { fatal: true }).decode(bytes); if (/[\x00-\x08\x0b\x0c\x0e-\x1f]/.test(text)) throw new Error(); } catch { encoding = 'base64'; let binary = ''; for (const b of bytes) binary += String.fromCharCode(b); text = btoa(binary); } textChunks(text).forEach((text, part) => evidence.push({ sha256: hash, path: artifact.path, encoding, part: part + 1, text })); }
  sheet('Evidence text', evidence); sheet('Long text', [...overflow], ['ref', 'part', 'text']); return wb;
}
export async function exportData(data: Dataset, selection: Selection, format: 'json' | 'xlsx') {
  const payload = format === 'json' ? JSON.stringify({ sourceDatasetFingerprint: data.fingerprint, selection, study: data }, null, 2) : new Uint8Array(await (await buildWorkbook(data, selection)).xlsx.writeBuffer());
  const blob = new Blob([payload], { type: format === 'json' ? 'application/json' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
  const url = URL.createObjectURL(blob), link = document.createElement('a'); link.href = url; link.download = `highscore.${format}`; link.click(); setTimeout(() => URL.revokeObjectURL(url), 1000);
}
