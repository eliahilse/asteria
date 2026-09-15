import json
from pathlib import Path
import tempfile
import unittest

from research import combined_results


def study(identifier: str, runs: int) -> dict:
    return {'plan': {'id': identifier, 'conditions': []}, 'runs': [{'runId': f'{identifier}__r{i}'} for i in range(runs)], 'summary': {}}


class CombinedResultsTests(unittest.TestCase):
    def test_rounds_in_natural_order_with_qualification_carried(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            for name, runs, top_level in (('i16b-x', 2, {'audit': 'b'}), ('i9-x', 1, {'audit': 'a'}), ('i16-x', 3, None)):
                d = root / 'research/iterations' / name; d.mkdir(parents=True)
                payload = {'local': False, 'tests': ['t'], 'studies': [study(name, runs)]}
                if top_level: payload['measurementQualification'] = top_level
                else: payload['studies'][0]['measurementQualification'] = {'audit': 'inline'}
                (d / 'qualified-results.json').write_text(json.dumps(payload))
            (root / 'research/iterations/i8-no-results').mkdir()
            data = combined_results.combine(root)
            self.assertEqual(data['rounds'], ['i9-x', 'i16-x', 'i16b-x']); self.assertEqual(data['tests'], ['t']); self.assertFalse(data['local'])
            self.assertEqual([len(s['runs']) for s in data['studies']], [1, 3, 2])
            self.assertEqual([s['measurementQualification'] for s in data['studies']], [{'audit': 'a'}, {'audit': 'inline'}, {'audit': 'b'}])
            self.assertEqual([s['plan']['round'] for s in data['studies']], ['i9-x', 'i16-x', 'i16b-x'])


if __name__ == '__main__':
    unittest.main()
