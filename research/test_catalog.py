"""Human-described test contracts, linked to the executable source; not vulnerability counts."""
from research.import_evidence import TEST_NAMES

SECURITY = [
    ('validRecordRoundTrip', 'Valid record survives reload', 'persistence', [], 'Store score 42, time 7, name Probe; persist and reload.', 'Reloaded names contain Probe.', 'Does not verify every field or persistence format.'),
    ('rejectsNegativeScore', 'Reject negative score', 'input_validation', ['CWE-20'], 'storeRun(-1, 1, Player)', 'Returns false and retains no name.', 'One invalid score value.'),
    ('rejectsNegativeTime', 'Reject negative time', 'input_validation', ['CWE-20'], 'storeRun(1, -1, Player)', 'Returns false and retains no name.', 'One invalid time value.'),
    ('rejectsNullName', 'Reject null name', 'input_validation', ['CWE-20'], 'storeRun(1, 1, null)', 'Returns false and retains no name.', 'Null input only.'),
    ('rejectsBlankName', 'Reject blank name', 'input_validation', ['CWE-20'], 'Name containing three spaces.', 'Returns false and retains no name.', 'Does not enumerate all Unicode whitespace.'),
    ('rejectsExcessiveName', 'Reject excessive name', 'input_validation', ['CWE-20'], 'Name containing 1,024 ASCII characters.', 'Returns false and retains no name.', 'Does not establish the exact accepted length boundary.'),
    ('boundsRetainedEntries', 'Bound retained entries', 'resource_exhaustion', ['CWE-400'], 'Insert 120 valid records.', 'All insertions accepted; at most 100 names retained.', 'Does not establish a bound on parsing or allocations.'),
    ('malformedStoreDoesNotCrash', 'Handle malformed store', 'input_validation', ['CWE-20'], 'Fixed invalid / NaN / negative / NUL byte sequence.', 'Constructor does not throw.', 'One malformed fixture; not fuzzing.'),
    ('oversizedPhysicalLine', 'Bound physical-line processing', 'resource_exhaustion', ['CWE-400'], 'Score file containing one 64 MiB ASCII line.', 'Constructor completes within 64 MiB heap and 15 seconds.', 'Finite resource fixture; hardware and JDK are recorded.'),
    ('nativeDeserializationCanary', 'Prevent deserialization callback', 'serialization', ['CWE-502'], 'Serialized test-only Canary object.', 'Canary readObject callback does not execute, even before rejection.', 'Instrumented classpath; not a production gadget chain or RCE proof.'),
    ('largePersistedRecordSet', 'Bound persisted-record processing', 'resource_exhaustion', ['CWE-400', 'CWE-789'], 'Persist and reload one valid record, then amplify to 1,000,000 records.', 'Load/reject within 64 MiB heap and 15 seconds; retain at most 100 names.', 'Known binary/text formats only. Unsupported encodings are unknown.'),
]

FUNCTIONAL = {
    'unit': [
        ('Empty initial board', 'Open a fresh store.', 'No records initially.'),
        ('Save adds a record', 'Store a valid score, time and player.', 'The record appears in the board.'),
        ('Record fields stay aligned', 'Store multiple records.', 'Names, scores and times retain matching positions.'),
        ('Descending score order', 'Insert records with different scores.', 'Board sorted by descending score.'),
        ('Persistence across instances', 'Persist records and reopen the store.', 'Records survive reload.'),
        ('Empty persistence', 'Persist an empty board and reload.', 'Empty board remains usable.'),
        ('Ranking after reload', 'Persist a ranked board and reopen.', 'Ranking remains correct.'),
    ],
    'invoked': [
        ('Record real score', 'Invoke the end-of-run recording API on a game level.', 'Recorded score equals the level score.'),
        ('Record real survival time', 'Invoke recording on a level with elapsed time.', 'Recorded time reflects the level time.'),
        ('Record real player name', 'Invoke recording on a named player.', 'Recorded name matches the player.'),
        ('One record per invocation', 'Invoke recording once.', 'Exactly one record is added.'),
    ],
    'autonomous': [
        ('Game end records automatically', 'End a game through the original integration driver.', 'A record is created without invoking the recording API directly.'),
        ('Automatic real player name', 'End a named player game.', 'Recorded name matches the actual player.'),
        ('No phantom records', 'Exercise gameplay without ending a run.', 'No record is created.'),
        ('Automatic elapsed time', 'End a game after measurable elapsed time.', 'Recorded survival time matches elapsed gameplay.'),
        ('Repeated games and ranking', 'Complete a second run with a different score.', 'Both records are retained and ranked.'),
    ],
}


def catalog(importer):
    result = []
    source = importer.artifact('research/security/SecurityProbe.java')
    for name, label, category, cwes, fixture, expected, limits in SECURITY:
        result.append(dict(id=f'security_v1.{name}', suite='security_v1', name=name, label=label,
                           kind='security', category=category, cwes=cwes, fixture=fixture,
                           expected=expected, limits=limits, source=source))
    files = {'unit': 'ApoMarioHighscoreTest.java', 'invoked': 'ApoMarioHighscoreInvokedTest.java', 'autonomous': 'ApoMarioHighscoreAutonomousTest.java'}
    for suite, contracts in FUNCTIONAL.items():
        for name, (label, fixture, expected) in zip(TEST_NAMES[suite], contracts, strict=True):
            result.append(dict(id=f'{suite}.{name}', suite=suite, name=name, label=label,
                               kind='functional', category='feature_behavior', cwes=[], fixture=fixture,
                               expected=expected, limits='Feature behavior only; passing does not establish security.',
                               source=importer.artifact('vamos-artifact/Tests/' + files[suite])))
    return result
