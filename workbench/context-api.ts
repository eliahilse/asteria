import { spawn, execFile } from 'node:child_process';
import { promisify } from 'node:util';
import { readFile, readdir } from 'node:fs/promises';
import { resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { loadEnv, type Plugin } from 'vite';

const root = fileURLToPath(new URL('../', import.meta.url));
const output = resolve(root, '.local/context-generation');
const validId = /^context-[a-f0-9]{32}$/;

export function contextApi(): Plugin {
  return {
    name: 'local-context-generation',
    apply: 'serve',
    configureServer(server) {
      server.middlewares.use('/api/context-inserts', async (req, res) => {
        res.setHeader('Content-Type', 'application/json'); res.setHeader('Cache-Control', 'no-store');
        try {
          if (req.method !== 'GET') throw new Error();
          const iteration = new URL(req.url || '/', 'http://localhost').searchParams.get('iteration');
          const args = ['-m', 'research.context_inserts', ...(iteration ? ['--iteration', iteration] : [])];
          const { stdout } = await promisify(execFile)('python3', args, { cwd: root, maxBuffer: 16 * 1024 * 1024 });
          res.end(stdout);
        } catch { res.statusCode = 400; res.end(JSON.stringify({ error: 'Cannot read the saved prompt inserts.' })); }
      });
      // Server-only environment. No provider configuration is sent to the browser or build.
      const env = { ...loadEnv(server.config.mode, root, 'ASTERIA_'), ...process.env };
      const targets: Record<string, string> = env.ASTERIA_CONTEXT_REPOS
        ? JSON.parse(env.ASTERIA_CONTEXT_REPOS)
        : { ApoMario: 'apogames/Java/ApoMario', ApoIcarus: 'apogames/Java/ApoIcarus' };
      const ready = (() => { try { const c = JSON.parse(env.ASTERIA_ADAPTER_COMMAND || 'null'); return Array.isArray(c) && c.length > 0 && c.every(v => typeof v === 'string' && v.length); } catch { return false; } })();
      const record = async (id: string) => {
        if (!validId.test(id)) throw new Error('Invalid generation ID');
        return JSON.parse(await readFile(resolve(output, id, 'record.json'), 'utf8'));
      };
      server.middlewares.use('/api/context-generation', async (req, res) => {
        const send = (status: number, data: unknown) => { res.statusCode = status; res.setHeader('Content-Type', 'application/json'); res.setHeader('Cache-Control', 'no-store'); res.end(JSON.stringify(data)); };
        try {
          const path = new URL(req.url || '/', 'http://localhost').pathname.split('/').filter(Boolean);
          if (req.method === 'GET' && path.length === 0) {
            const dirs = await readdir(output).catch(() => []);
            const runs = await Promise.all(dirs.filter(id => validId.test(id)).map(async id => {
              const r = await record(id);
              return { id, repository: r.repository, task: r.task, strategy: r.strategy, status: r.status,
                startedAt: r.startedAt, turns: r.turns.length, items: r.output?.items.length ?? null,
                generatorHash: r.generatorHashes?.['research/generate_context.py'] ?? null };
            }));
            send(200, { local: true, adapterReady: ready, targets: Object.keys(targets), runs: runs.sort((a, b) => b.startedAt.localeCompare(a.startedAt)) });
          } else if (req.method === 'GET' && path.length === 1) {
            send(200, await record(path[0]));
          } else if (req.method === 'GET' && path.length === 2 && path[1] === 'snapshot' && validId.test(path[0])) {
            send(200, JSON.parse(await readFile(resolve(output, path[0], 'snapshot.json'), 'utf8')));
          } else if (req.method === 'POST' && path.length === 0) {
            // Generation spends model budget: accept only an explicit same-origin JSON request.
            if (req.headers.origin !== `http://${req.headers.host}` || !req.headers['content-type']?.startsWith('application/json')) {
              send(403, { error: 'Use the local explorer to submit a generation.' }); return;
            }
            let body = '';
            for await (const chunk of req) { body += chunk; if (Buffer.byteLength(body) > 20000) { send(413, { error: 'Task is too large.' }); return; } }
            const input = JSON.parse(body);
            if (!Object.hasOwn(targets, input.repository) || !['overview', 'task', 'flows'].includes(input.strategy) ||
                typeof input.task !== 'string' || !input.task.trim() || input.task.length > 12000 || !['prepare', 'generate'].includes(input.action)) {
              send(400, { error: 'Choose a repository, strategy and task.' }); return;
            }
            if (input.action === 'generate' && !ready) { send(409, { error: 'Configure ASTERIA_ADAPTER_COMMAND locally first.' }); return; }
            const args = ['-m', 'research.generate_context', '--repo', resolve(root, targets[input.repository]),
              '--task', input.task, '--strategy', input.strategy, '--output', output];
            if (input.action === 'generate') args.push('--execute');
            const id = await new Promise<string>((accept, reject) => {
              const child = spawn(env.ASTERIA_PYTHON || 'python3', args, { cwd: root, env, stdio: ['ignore', 'pipe', 'ignore'] });
              let text = '', announced = false;
              child.stdout.on('data', chunk => {
                text += chunk;
                if (!announced && text.includes('\n')) {
                  try { const data = JSON.parse(text.split('\n')[0]); if (!validId.test(data.id)) throw new Error(); announced = true; accept(data.id); }
                  catch { reject(new Error('Generation did not return a valid record.')); }
                }
              });
              child.on('error', () => reject(new Error('Cannot start the local context generator.')));
              child.on('exit', () => { if (!announced) reject(new Error('Cannot prepare repository input. Check the local repository path and source size.')); });
            });
            send(202, { id });
          } else send(404, { error: 'Unknown context-generation endpoint.' });
        } catch (error) {
          // Never reflect environment, process stderr or filesystem diagnostics.
          send(400, { error: error instanceof SyntaxError ? 'Invalid JSON request or record.' : 'Cannot read or start this context generation.' });
        }
      });
    },
  };
}
