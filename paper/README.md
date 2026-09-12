# Paper

The first page contains a title, a short abstract, and a plain-language TL;DR.
Keep content in separate components and assemble them in `main.tex`:

- [`main.tex`](main.tex): document class, title, and section order.
- [`preamble.tex`](preamble.tex): packages and shared formatting.
- [`sections/abstract.tex`](sections/abstract.tex): research question, results, and limits.
- [`sections/tldr.tex`](sections/tldr.tex): brief takeaway.
- [`references.bib`](references.bib): bibliography entries.

Add further sections as `sections/<name>.tex` and include them with
`\input{sections/<name>}` in `main.tex`. The current summary draws on the
[research readout](../research/iterations/REVIEW.md) and the
[I06/I07 comparison](../research/iterations/i07-operational-replication/replication-analysis.md).

The preamble includes typography, math, figures, tables, links, and numeric
citations. Add sources to [`references.bib`](references.bib) and uncomment the
two bibliography commands in `main.tex` when needed.

## Setup

Install [Tectonic](https://tectonic-typesetting.github.io/book/latest/installation/)
and Make. On macOS with Homebrew:

```sh
brew install tectonic
```

Make is included with the macOS Command Line Tools (`xcode-select --install`).
For Linux and other installation methods, use Tectonic's installation guide.
A standalone Tectonic executable at `.local/bin/tectonic` in the repository root
is also picked up automatically. To use another executable, pass
`TECTONIC=/absolute/path/to/tectonic` to Make.

## Build

From the repository root:

```sh
make -C paper
```

The only build output is **`paper/build/main.pdf`**. `make -C paper pdf` does the
same thing. On macOS, preview it with:

```sh
open paper/build/main.pdf
```

[Tectonic](https://tectonic-typesetting.github.io/book/latest/v2cli/compile.html)
runs the required LaTeX and BibTeX passes automatically and discards intermediate
files. The first build needs internet access to download the required TeX
packages. Later builds reuse the ignored `paper/.cache/tectonic/` cache; new
packages may need another download. Generated output and the cache stay out of Git.

To remove the PDF output while keeping cached packages:

```sh
make -C paper clean
```
