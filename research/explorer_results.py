"""Use explicitly saved qualified measurements in the explorer when available."""
import argparse
import json

from research.import_evidence import ROOT, canonical
from research.iteration_results import index as original_index


def index(public=False, iteration=None, original=False):
    data = original_index(public=public, iteration=iteration)
    if original or len(data['studies']) != 1: return data
    identifier = data['studies'][0]['plan']['id']
    path = ROOT / 'research/iterations' / identifier / 'qualified-results.json'
    if not path.exists(): return data
    if public:
        result = json.loads(path.read_text()); result['local'] = False; return result
    from research.qualification import apply_available
    data['studies'][0], metadata = apply_available(data['studies'][0])
    data['measurementQualification'] = metadata
    return data


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration'); parser.add_argument('--original', action='store_true'); parser.add_argument('--write-public', action='store_true')
    args = parser.parse_args(); data = index(public=args.write_public, iteration=args.iteration, original=args.original)
    if args.write_public: (ROOT / 'workbench/public/data/matrix.json').write_bytes(canonical(data))
    else: print(json.dumps(data))
