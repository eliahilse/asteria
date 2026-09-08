import gzip
import io
import json
from pathlib import Path
import tarfile
import tempfile
import unittest
from unittest.mock import patch

from research import archive_results as archive
from research.import_evidence import canonical, digest


class ArchiveTests(unittest.TestCase):
    def make_snapshot(self, root):
        source = root / 'repo'; source.mkdir()
        data = source / '.local/iterations/fixture'; data.mkdir(parents=True)
        for name in ('a', 'b', 'c'): (data / name).write_bytes(name.encode() * 80)
        with patch.multiple(archive, ROOT=source, ARCHIVES=source / 'research/results', BACKUPS=root / 'outside', PART_BYTES=100), \
             patch.object(archive.subprocess, 'check_output', return_value='fixture\n'):
            manifest = archive.snapshot('fixture', ['iterations'])
        return source / 'research/results/fixture', manifest

    def test_all_parts_restore_and_external_copy_is_identical(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp); directory, manifest = self.make_snapshot(root)
            self.assertEqual(len(manifest['archiveParts']), 3)
            self.assertEqual(archive.verify(root / 'outside/fixture'), manifest)
            destination = root / 'restored'; archive.restore(directory, destination)
            for name in ('a', 'b', 'c'):
                self.assertEqual((destination / '.local/iterations/fixture' / name).read_bytes(), name.encode() * 80)

    def test_last_part_conflict_prevents_all_restore_writes(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp); directory, _ = self.make_snapshot(root)
            destination = root / 'restored'; conflict = destination / '.local/iterations/fixture/c'
            conflict.parent.mkdir(parents=True); conflict.write_text('existing evidence')
            with self.assertRaisesRegex(ValueError, 'Different file'): archive.restore(directory, destination)
            self.assertFalse(conflict.with_name('a').exists())
            self.assertEqual(conflict.read_text(), 'existing evidence')

    def test_corrupted_part_is_detected(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp); directory, manifest = self.make_snapshot(root)
            (directory / manifest['archiveParts'][-1]['name']).write_bytes(b'corrupt')
            with self.assertRaisesRegex(ValueError, 'Archive hash'): archive.verify(directory)

    def test_original_single_archive_remains_restorable(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp); directory = root / 'legacy'; directory.mkdir()
            raw = b'original evidence'; name = '.local/iterations/old/record.json'
            with tarfile.open(directory / 'evidence.tar.gz', 'w:gz') as bundle:
                info = tarfile.TarInfo(name); info.size = len(raw); bundle.addfile(info, io.BytesIO(raw))
            manifest = {'files': {name: {'sha256': digest(raw), 'bytes': len(raw)}},
                        'archiveSha256': digest((directory / 'evidence.tar.gz').read_bytes())}
            (directory / 'manifest.json').write_bytes(canonical(manifest))
            archive.restore(directory, root / 'restored')
            self.assertEqual((root / 'restored' / name).read_bytes(), raw)


if __name__ == '__main__': unittest.main()
