# Asteria research workbench

An offline-capable React workbench over the preserved Highscore evidence. It
separates source-review conclusions, scanner candidates and executable tests.
The selected published successes are never pooled with the complete fresh pilot.

From this directory, with Node.js 22.12+ and Python 3.10+:

```sh
npm ci
npm run dev
```

The importer runs automatically before development and production builds. It
checks exact historical prompt hashes and writes content-addressed evidence
assets. No model calls, credentials or backend are required.

```sh
npm run build
npm test
npx playwright install chromium
npm run test:e2e
```

An installed Chrome can be used with `PLAYWRIGHT_CHANNEL=chrome npm run test:e2e`.
The production output in `dist/` can be served as a static directory. The app
supports a relative base path. All evidence and fonts are bundled locally.

See `../docs/EVIDENCE_MODEL.md` for observation semantics and denominators.
Shareable URLs identify views, cohorts and individual run records. Browser print
styles support a meeting handout. Historical billed cost is unavailable and is
not estimated retrospectively.

## Exports

XLSX exports respect the active filters and contain separate sheets for condition
statistics, attempts, named historical checks, new security checks, source-reviewed
findings, scanner candidates, context facts, run-to-fact links, attachments and
provenance. Exact selected prompts, original responses and generated code are
embedded as ordered text chunks to respect Excel's cell-size limit. Binary or
XML-incompatible evidence is base64 encoded. Every embedded artifact is verified
against its SHA-256 before export; a mismatch fails the export visibly.

The source dataset fingerprint identifies the complete dataset. JSON exports
declare that fingerprint separately from the filtered selection. Missing values
remain blank in XLSX. Strings are written as string cells, never formulas.
