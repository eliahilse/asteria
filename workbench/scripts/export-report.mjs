import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { existsSync } from 'node:fs';
import { registerHooks } from 'node:module';
import { dirname, extname, resolve } from 'node:path';

const [input, output, ...extra] = process.argv.slice(2);
if (!input || !output || extra.length) {
  console.error('Usage: node scripts/export-report.mjs INPUT.json OUTPUT.xlsx');
  process.exit(1);
}
const sourceRoot = new URL('../src/', import.meta.url);
// Node 24 strips these modules' TypeScript types. Resolve their extensionless
// local imports without starting a dev server or changing dependency resolution.
registerHooks({ resolve(specifier, context, nextResolve) {
  if (context.parentURL?.startsWith(sourceRoot.href) && specifier.startsWith('.') && !extname(specifier)) {
    const candidate = new URL(specifier + '.ts', context.parentURL);
    if (existsSync(candidate)) return nextResolve(candidate.href, context);
  }
  return nextResolve(specifier, context);
} });
const data = JSON.parse(await readFile(resolve(input), 'utf8'));
// Optional CWE labels for the Issue Matrix sheet; the CWE column stays blank without the file.
// Accepts either { check: [ids] } or the richer { checks: { check: { cwes: [ids], ... } } } shape.
const mappingPath = new URL('../../research/security/cwe-mapping.json', import.meta.url);
let cweMapping;
if (existsSync(mappingPath)) {
  const raw = JSON.parse(await readFile(mappingPath, 'utf8'));
  cweMapping = Object.fromEntries(Object.entries(raw.checks ?? raw).map(([name, entry]) => [name, Array.isArray(entry) ? entry : entry?.cwes]));
  if (Object.values(cweMapping).some(ids => !Array.isArray(ids) || ids.some(id => typeof id !== 'string'))) {
    throw new Error('research/security/cwe-mapping.json must map every check name to a list of CWE ids');
  }
}
const { reportWorkbook } = await import(new URL('report-workbook.ts', sourceRoot));
const workbook = reportWorkbook(data, { cweMapping });
await mkdir(dirname(resolve(output)), { recursive: true });
await writeFile(resolve(output), Buffer.from(await workbook.xlsx.writeBuffer()), { flag: 'wx' });
console.log(`${workbook.worksheets.length} sheets written to ${output}${cweMapping ? ' (CWE mapping applied)' : ' (no CWE mapping; CWE column blank)'}`);
