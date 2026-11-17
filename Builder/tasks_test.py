"""Run: python3 Builder/tasks_test.py  (no invoke install needed)"""
import os
import sys
import types
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
# tasks.py only needs invoke's @task decorator at import time
sys.modules.setdefault('invoke', types.SimpleNamespace(task=lambda *a, **k: (lambda f: f) if not a or not callable(a[0]) else a[0]))
import tasks  # noqa: E402

WINDOWS_TREE = [
    r'C:\src\LunarConsole\Common\LUConsole.m',
    r'C:\src\LunarConsole\Free\Console\Plugin\LUConsolePluginImp.m',
    r'C:\src\LunarConsole\Full\Console\Plugin\LUConsolePluginImp.m',
]


class ListIosFilesTest(unittest.TestCase):
    # Windows is the documented build host (Builder/setup.md); os.walk yields '\' paths there (#229)
    def setUp(self):
        self._orig = tasks.list_files
        tasks.list_files = lambda path: list(WINDOWS_TREE)

    def tearDown(self):
        tasks.list_files = self._orig

    def test_free_excludes_full_on_windows_paths(self):
        self.assertEqual(tasks.list_ios_files('C:\\src', configuration='Free'),
                         [WINDOWS_TREE[0], WINDOWS_TREE[1]])

    def test_full_excludes_free_on_windows_paths(self):
        self.assertEqual(tasks.list_ios_files('C:\\src', configuration='Full'),
                         [WINDOWS_TREE[0], WINDOWS_TREE[2]])


if __name__ == '__main__':
    unittest.main()
