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
