from copy import deepcopy
import json
from pathlib import Path
import tempfile
import unittest
from research.import_evidence import digest
from research.repeatability import checks_by_name, measures, verify_measurement


class RepeatabilityTests(unittest.TestCase):
    def test_raw_observation_is_retained_when_precondition_requires_unknown(self):
        report = {'mainCompilation': 'pass', 'functionalSuccess': True, 'checks': [],
            'security': {'checks': [{'suite': 'security_v1', 'name': 'largePersistedRecordSet', 'status': 'pass'}]}}
        original = deepcopy(report)
        observed = measures(report, {'status': 'not_demonstrated'})
        self.assertEqual(report, original)
        self.assertEqual(observed['rawChecks']['security_v1.largePersistedRecordSet'], 'pass')
        self.assertEqual(observed['qualifiedChecks']['security_v1.largePersistedRecordSet'], 'unknown')
        self.assertTrue(observed['functionalSuccess'])

    def test_matched_precondition_retains_an_observed_failure(self):
        report = {'checks': [], 'security': {'checks': [{'suite': 'security_v1', 'name': 'largePersistedRecordSet', 'status': 'fail'}]}}
        observed = measures(report, {'status': 'matched'})
        self.assertEqual(observed['qualifiedChecks']['security_v1.largePersistedRecordSet'], 'fail')

    def test_missing_checks_remain_unexecuted(self):
        self.assertEqual(len(checks_by_name([])), 27)
        self.assertEqual(set(checks_by_name([]).values()), {'not_run'})

    def fixture(self, directory):
        evaluation = directory / 'evaluation'; evaluation.mkdir()
        sources = evaluation / 'author-evidence/sanitized_generated/response'; sources.mkdir(parents=True)
        classes = evaluation / 'security-project-classes'; classes.mkdir()
        (evaluation / 'response.txt').write_bytes(b'original code')
        (sources / 'Example.java').write_bytes(b'sanitized code')
        (classes / 'Example.class').write_bytes(b'compiled code')
        report = {'responseSha256': digest(b'original code'), 'sanitizedHashes': {'Example.java': digest(b'sanitized code')},
                  'security': {'linkage': {'compiledClasses': {'Example.class': digest(b'compiled code')}}, 'checks': []}, 'checks': []}
        raw = json.dumps(report).encode(); (evaluation / 'report.json').write_bytes(raw)
        precondition = {'status': 'matched'}
        (directory / 'precondition').mkdir(); (directory / 'precondition/report.json').write_text(json.dumps(precondition))
        item = {'sourceSha256': report['responseSha256'], 'sanitizedHashes': report['sanitizedHashes'], 'compiledClasses': report['security']['linkage']['compiledClasses']}
        record = {'reportSha256': digest(raw), 'sanitizedSourceMatches': True, 'compiledClassesMatch': True,
                  'precondition': precondition, 'measures': measures(report, precondition)}
        return record, item

    def test_saved_repeat_artifacts_are_verified_before_reporting(self):
        with tempfile.TemporaryDirectory() as temporary:
            directory = Path(temporary); record, item = self.fixture(directory)
            verify_measurement(directory, record, item)
            (directory / 'evaluation/security-project-classes/Example.class').write_bytes(b'changed binary')
            with self.assertRaisesRegex(ValueError, 'compiled class changed'):
                verify_measurement(directory, record, item)

    def test_cached_comparison_and_missing_probe_cannot_establish_repeatability(self):
        with tempfile.TemporaryDirectory() as temporary:
            directory = Path(temporary); record, item = self.fixture(directory)
            record['compiledClassesMatch'] = False
            with self.assertRaisesRegex(ValueError, 'Cached source/class'):
                verify_measurement(directory, record, item)
            record['compiledClassesMatch'] = True
            (directory / 'precondition/report.json').unlink()
            with self.assertRaisesRegex(ValueError, 'Missing repeated precondition'):
                verify_measurement(directory, record, item)


if __name__ == '__main__': unittest.main()
