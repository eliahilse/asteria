"""Screen delivered source for a declared set of test sentinel literals."""
import argparse
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study

PATTERN = re.compile(r'(?<![A-Za-z0-9_])(?:777|31337|54321)(?![A-Za-z0-9_])|SecurityProbe|HighscoreWiringTest|HighscoreCouplingTest|org\.junit|JUnitCore')


def save(identifiers, output):
    result = {'scope': 'Every delivered source submission in the named completed rounds. Literal screening only: zero matches is not proof that code cannot overfit tests. Hits require manual interpretation. Outcomes and model feedback are unchanged.',
        'pattern': PATTERN.pattern, 'analysisSha256': digest(Path(__file__).read_bytes()), 'iterations': []}
    for identifier in identifiers:
        if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', identifier): raise ValueError('Invalid iteration ID')
        runtime = ROOT / '.local/iterations' / identifier; study = read_study(runtime)
        if not study['summary']['complete']: raise ValueError('Complete the source round before screening')
        hashes, hits = {}, []
        for row in study['plan']['schedule']:
            path = runtime / 'runs' / row['runId'] / 'record.json'; record = json.loads(path.read_text())
            for submission in record['submissions']:
                if not submission.get('completeResponseSha256'): continue
                source = path.parent / f'submission-{submission["number"]}/complete-files.txt'; raw = source.read_bytes()
                sha = digest(raw)
                if sha != submission['completeResponseSha256']: raise ValueError('Delivered source differs from its recorded hash')
                name = str(source.relative_to(ROOT)); hashes[name] = sha
                for number, line in enumerate(raw.decode().splitlines(), 1):
                    if PATTERN.search(line): hits.append({'file': name, 'line': number, 'text': line})
        result['iterations'].append({'iteration': identifier, 'manifestFingerprint': study['plan']['fingerprint'],
            'deliveredSubmissions': len(hashes), 'sourceHashes': hashes, 'hits': hits})
        print(f'{identifier}: {len(hashes)} source submissions verified; {len(hits)} literal matches')
    output.parent.mkdir(parents=True, exist_ok=True); output.write_bytes(canonical(result))
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', nargs='+', required=True); parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args(); save(args.iteration, args.output)
