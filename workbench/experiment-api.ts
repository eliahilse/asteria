import { execFile } from 'node:child_process';
import { promisify } from 'node:util';
import { readFile } from 'node:fs/promises';
import { resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import type { Plugin } from 'vite';

const root = fileURLToPath(new URL('../', import.meta.url));
const exec = promisify(execFile);
const valid = /^[a-zA-Z0-9_-]+$/;
async function manifest(id: string) {
  if (!valid.test(id)) throw new Error();
  for (const directory of ['research/studies', '.local/studies']) {
    const base = resolve(root, directory, id);
    try { return { base, plan: JSON.parse(await readFile(resolve(base, 'manifest.json'), 'utf8')) }; } catch { /* Try the other study directory. */ }
  }
  throw new Error();
}
export function experimentApi(): Plugin {
  return { name: 'local-experiment-results', apply: 'serve', configureServer(server) {
    server.middlewares.use('/api/experiment', async (req, res) => {
      res.setHeader('Cache-Control', 'no-store'); res.setHeader('Content-Type', 'application/json');
      try {
        if (req.method !== 'GET') throw new Error();
        const parts = new URL(req.url || '/', 'http://localhost').pathname.split('/').filter(Boolean);
        if (!parts.length) {
          const { stdout } = await exec('python3', ['-m', 'research.study_results'], { cwd: root, maxBuffer: 64 * 1024 * 1024 });
          res.end(stdout); return;
        }
        if (parts.length !== 3 || !parts.every(p => valid.test(p))) throw new Error();
        const { base, plan } = await manifest(parts[0]);
        if (parts[1] === 'prompt') {
          const condition = plan.conditions.find((c: { id: string }) => c.id === parts[2]);
          if (!condition) throw new Error();
          const path = resolve(base, condition.promptFile);
          if (!path.startsWith(base + '/')) throw new Error();
          res.end(JSON.stringify({ text: await readFile(path, 'utf8'), sha256: condition.promptSha256 })); return;
        }
        if (parts[1] === 'run' && plan.schedule.some((r: { runId: string }) => r.runId === parts[2])) {
          const directory = resolve(root, '.local/experiments', plan.id);
          const observation = JSON.parse(await readFile(resolve(directory, 'runs', parts[2] + '.json'), 'utf8'));
          let evaluation = null;
          try { evaluation = JSON.parse(await readFile(resolve(directory, 'evaluations', parts[2], 'report.json'), 'utf8')); } catch { /* Evaluation may still be pending. */ }
          res.end(JSON.stringify({ observation, evaluation })); return;
        }
        throw new Error();
      } catch {
        res.statusCode = 400; res.end(JSON.stringify({ error: 'Cannot load experiment evidence. Check its local records and lineage.' }));
      }
    });
  } };
}
