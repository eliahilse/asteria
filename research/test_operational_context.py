import unittest
from research import generate_security_context as base
from research import operational_context as operations
from research.test_generate_security_context import item


class OperationalContextTests(unittest.TestCase):
    def test_proposed_guards_require_all_operation_fields_without_invented_evidence(self):
        proposal = item()
        action = {'summary': 'Storage', 'items': [proposal], 'limitations': []}
        with self.assertRaisesRegex(ValueError, 'requires operation'): operations.validate_output(action, {})
        proposal.update({field: 'Proposed ' + field for field in operations.FIELDS})
        output, _ = operations.validate_output(action, {})
        self.assertEqual(output['items'][0]['evidence'], [])
        text = operations.prompt_insert({'output': output})
        for field in operations.FIELDS: self.assertIn('Proposed ' + field, text)
        self.assertIn('not an implemented safeguard', text)

    def test_unknowns_can_remain_unknown_and_original_generator_is_unchanged(self):
        unknown = item('unknown', 'observed'); unknown.update({field: None for field in operations.FIELDS})
        operations.validate_output({'summary': 'Uncertainty', 'items': [unknown], 'limitations': []}, {})
        original_tool, original_protocol, original_validator = base.TOOL, base.PROTOCOL, base.validate_output
        with operations.protocol():
            self.assertIn('operations', base.STRATEGIES)
            self.assertIs(base.TOOL, operations.TOOL)
        self.assertNotIn('operations', base.STRATEGIES)
        self.assertIs(base.TOOL, original_tool); self.assertEqual(base.PROTOCOL, original_protocol)
        self.assertIs(base.validate_output, original_validator)


if __name__ == '__main__': unittest.main()
