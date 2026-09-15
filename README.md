# Asteria

Does repository-specific security context, given to a language model before it writes code, make the code more secure, and what does it cost? One task (a Highscore feature for the Java game ApoMario, implemented from scratch or by reusing a sibling game), ten security checks and sixteen functional tests per artifact, every arm against a control collected alongside it. Working document; results are added round by round.

- **Paper**: `paper/` (LNCS, `make -C paper` builds `paper/build/main.pdf` with the bundled Tectonic). Tables are asserted against the saved analyses by `research/test_paper_tables.py`.
- **Rounds**: `research/iterations/README.md` indexes every round; each folder has its declaration, findings, issue matrix, hook audit and two Excel workbooks. `research/iterations/all-rounds-report.xlsx` holds every round in one workbook (`python3 -m research.combined_results --export`).
- **Overview page**: https://eliahilse.github.io/asteria/ (all rounds, newest first), with `contexts.html` (every insert of every round) and `agent-io.html` (what the context agent received and produced). Locally: `cd workbench && npm run dev`.
- **Conventions**: counts over fixed denominators, no percentages below 20 trajectories (`docs/REPORTING.md`); checks named in CWE terms (`research/security/CWE_MAPPING.md`); rounds are declared and frozen before the first model call.
- **Reproduce**: `python3 -m unittest discover -s research -p 'test_*.py'`; model calls go through the private adapter named in `.env.local`.
- **Prior study** this work extends (task, tests and base-context files): https://github.com/ieiris/llm-context-generation-reuse
