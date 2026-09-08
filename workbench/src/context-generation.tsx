import { useEffect, useState } from 'react';

type Insert = { id: string; strategy: string; label: string; text: string; sha256: string };
type InsertData = { local: boolean; iteration: string | null; groups: { method: string; task: string; inserts: Insert[] }[] };

export function ContextGeneration() {
  const [data, setData] = useState<InsertData>(), [error, setError] = useState(''), [copied, setCopied] = useState('');
  useEffect(() => {
    let current = true;
    const fetchData = async (url: string) => {
      const response = await fetch(url);
      if (!response.ok || !response.headers.get('content-type')?.includes('application/json')) throw new Error('Cannot load saved prompt inserts.');
      return await response.json() as InsertData;
    };
    const refresh = async () => {
      try {
        const iteration = new URLSearchParams(location.search).get('iteration');
        let result;
        try { result = await fetchData('api/context-inserts' + (iteration ? `?iteration=${encodeURIComponent(iteration)}` : '')); }
        catch (e) { if (import.meta.env.DEV) throw e; result = await fetchData('data/context-inserts.json'); }
        if (current) { setData(result); setError(''); }
      } catch (e) { if (current) setError((e as Error).message); }
    };
    refresh(); const timer = setInterval(refresh, 5000);
    return () => { current = false; clearInterval(timer); };
  }, []);
  return <section aria-label="Context generation">
    {error && <p role="alert">{error}</p>}
    {!data && !error && <p role="status">Loading generated context…</p>}
    {data?.groups.length === 0 && <p className="empty">No generated context in this snapshot.</p>}
    {data?.groups.map(group => <section key={group.method + group.task} className="prompt-context-group">
      <h2>{group.method} · Highscore</h2>
      <label>Task<textarea aria-label="Task" readOnly rows={4} value={group.task} /></label>
      {group.inserts.map(insert => <article key={insert.id} className="prompt-context">
        <div className="prompt-insert-heading"><h3>{insert.label}</h3><button onClick={async () => { try { await navigator.clipboard.writeText(insert.text); setCopied(insert.id); } catch { setError('Could not copy the insert. Select and copy its text below.'); } }}>{copied === insert.id ? 'Copied' : 'Copy insert'}</button></div>
        <pre className="prompt-insert" data-context={insert.id}>{insert.text}</pre>
      </article>)}
    </section>)}
  </section>;
}
