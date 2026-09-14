import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { existsSync } from 'node:fs';
import { registerHooks } from 'node:module';
import { dirname, extname, resolve } from 'node:path';

const [input, output, ...extra] = process.argv.slice(2);
if (!input || !output || extra.length) {
  console.error('Usage: node scripts/export-detailed.mjs INPUT.json OUTPUT.xlsx');
  process.exit(1);
}
const sourceRoot = new URL('../src/', import.meta.url);
// Same extensionless-import resolution as export-report.mjs (Node 24 type stripping).
registerHooks({ resolve(specifier, context, nextResolve) {
  if (context.parentURL?.startsWith(sourceRoot.href) && specifier.startsWith('.') && !extname(specifier)) {
    const candidate = new URL(specifier + '.ts', context.parentURL);
    if (existsSync(candidate)) return nextResolve(candidate.href, context);
  }
  return nextResolve(specifier, context);
} });
const data = JSON.parse(await readFile(resolve(input), 'utf8'));
const { experimentWorkbook } = await import(new URL('experiment-export.ts', sourceRoot));
const workbook = experimentWorkbook(data);
await mkdir(dirname(resolve(output)), { recursive: true });
await writeFile(resolve(output), Buffer.from(await workbook.xlsx.writeBuffer()), { flag: 'wx' });
console.log(`${workbook.worksheets.length} sheets written to ${output}`);
