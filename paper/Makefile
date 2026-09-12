.DEFAULT_GOAL := pdf

# Prefer a repository-local compiler, if installed; otherwise use PATH.
TECTONIC ?= $(if $(wildcard ../.local/bin/tectonic),../.local/bin/tectonic,tectonic)
export TECTONIC_CACHE_DIR ?= $(abspath .cache/tectonic)

.PHONY: pdf clean

pdf:
	@command -v "$(TECTONIC)" >/dev/null 2>&1 || { \
		printf '%s\n' 'Tectonic is missing. See paper/README.md for installation.' >&2; \
		exit 1; \
	}
	@mkdir -p build
	"$(TECTONIC)" -X compile --outdir build main.tex
	@printf '%s\n' 'PDF: paper/build/main.pdf'

clean:
	rm -rf build
