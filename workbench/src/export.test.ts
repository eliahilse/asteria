import { it, expect } from 'vitest';
import { readFile } from 'node:fs/promises';
import ExcelJS from 'exceljs';
import { buildWorkbook, textChunks } from './export';
import type { Dataset } from './types';

it('exports granular observations, nulls, safe strings and exact long prompts in a readable XLSX', async () => {
  const data: Dataset = JSON.parse(await readFile('public/data/study.json','utf8'));
  const runs = data.runs.filter(r=>r.cohort==='fresh_pilot');
  const workbook = await buildWorkbook(data,runs,{cohort:'fresh_pilot',model:'all',strategy:'all',security:'all',query:''}, a=>readFile(`public/${a.href}`));
  expect(workbook.getWorksheet('Runs')!.rowCount).toBe(17);
  expect(workbook.getWorksheet('Historical checks')!.rowCount).toBe(353);
  expect(workbook.getWorksheet('Adjudicated findings')!.rowCount).toBe(4);
  expect(workbook.getWorksheet('Context facts')!.rowCount).toBe(6);
  expect(workbook.getWorksheet('Run context links')!.rowCount).toBe(41);
  expect(workbook.getWorksheet('Extracted context')!.rowCount).toBe(146);
  expect(workbook.getWorksheet('Planned conditions')!.rowCount).toBe(17);
  expect(workbook.getWorksheet('Planned schedule')!.rowCount).toBe(141);
  const sheet = workbook.getWorksheet('Runs')!;
  const costColumn = sheet.columns.findIndex(c=>c.key==='billed_cost_usd')+1;
  expect(sheet.getCell(2,costColumn).value).toBeNull();
  const rows = workbook.getWorksheet('Evidence text')!;
  const prompt = runs[0].prompt!; let reconstructed = '';
  rows.eachRow((row,i)=>{if(i>1 && row.getCell(1).value===prompt.sha256) reconstructed+=row.getCell(5).value;});
  expect(reconstructed).toBe(await readFile(`public/${prompt.href}`,'utf8'));
  const planned = data.experimentPlans[0].conditions.find(c => c.id === 'generation_security_all')!.prompt;
  let plannedText = '';
  rows.eachRow((row,i)=>{if(i>1 && row.getCell(1).value===planned.sha256) plannedText+=row.getCell(5).value;});
  expect(plannedText).toBe(await readFile(`public/${planned.href}`,'utf8'));
  // String cells must stay strings even when they look like formulas.
  workbook.getWorksheet('Read me')!.addRow({field:'formula_test',value:'=HYPERLINK("https://invalid.example","x")'});
  const loaded = new ExcelJS.Workbook(); await loaded.xlsx.load(await workbook.xlsx.writeBuffer());
  expect(loaded.getWorksheet('Runs')!.rowCount).toBe(17);
  const value = loaded.getWorksheet('Read me')!.lastRow!.getCell(2);
  expect(value.type).toBe(ExcelJS.ValueType.String);
}, 30000);

it('never splits a Unicode surrogate pair or drops long text', () => {
  const s='a'.repeat(29999)+'🧪'+'z'.repeat(40000); const chunks=textChunks(s);
  expect(chunks.join('')).toBe(s); expect(chunks.every(c=>c.length<=30000)).toBe(true);
  expect(chunks[0]).toHaveLength(29999);
});

it('refuses an export if an evidence artifact differs from the manifest', async () => {
  const data: Dataset = JSON.parse(await readFile('public/data/study.json','utf8'));
  await expect(buildWorkbook(data,[data.runs[0]],{cohort:'fresh_pilot',model:'all',strategy:'all',security:'all',query:''},async()=>new Uint8Array([1,2,3]))).rejects.toThrow('Evidence hash mismatch');
});
