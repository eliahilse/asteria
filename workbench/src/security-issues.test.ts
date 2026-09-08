import { expect, test } from 'vitest';
import { readFileSync } from 'node:fs';
import { securityIssues, combinationRows } from './combination-stats';
import type { MatrixData } from './experiment-types';

function fixture() {
  // Use the public protocol's complete test catalog, with synthetic outcomes only.
  const data = JSON.parse(readFileSync(new URL('../public/data/original-matrix.json', import.meta.url), 'utf8')) as MatrixData;
  const summary = data.studies[0].summary.conditions[0];
  Object.assign(summary, { attempts: 5, pending: 0 });
  for (const check of summary.checks) Object.assign(check, { pass: 5, fail: 0, executed: 5, attempts: 5 });
  return { data, summary };
}

test('fewer evaluated issue checks cannot be reported as fewer security issues', () => {
  const { summary: control } = fixture(), treatment = structuredClone(control);
  const a = control.checks.find(c => c.name === 'rejectsNullName')!, b = treatment.checks.find(c => c.name === a.name)!;
  Object.assign(a, { fail: 5, pass: 0 });
  Object.assign(b, { fail: 4, pass: 0, executed: 4 });
  expect(securityIssues(treatment, control)).toMatchObject({ detected: 4, evaluated: 49, expected: 50, delta: null });
  expect(securityIssues(treatment, control).deltaBounds).toEqual([-1, 0]);
  // Even equal aggregate exposure is insufficient when the tested checks differ.
  Object.assign(control.checks.find(c => c.name === 'rejectsNegativeTime')!, { executed: 4, pass: 4 });
  expect(securityIssues(treatment, control).delta).toBeNull();
  expect(securityIssues(treatment, control).deltaBounds).toEqual([-2, 0]);
});

test('missingness bounds distinguish a robust decrease from an unevaluated treatment', () => {
  const { summary: control } = fixture(), treatment = structuredClone(control);
  for (const check of control.checks.filter(c => c.suite === 'security_v1' && c.name !== 'validRecordRoundTrip')) Object.assign(check, { fail: 5, pass: 0 });
  Object.assign(treatment.checks.find(c => c.name === 'largePersistedRecordSet')!, { pass: 4, executed: 4 });
  expect(securityIssues(treatment, control).deltaBounds).toEqual([-50, -49]);
  for (const check of treatment.checks) Object.assign(check, { pass: 0, fail: 0, executed: 0 });
  expect(securityIssues(treatment, control).deltaBounds).toBeNull();
});

test('count changes require equal attempts and no pending requests, and replay has no treatment arrow', () => {
  const { summary: control } = fixture(), treatment = structuredClone(control);
  Object.assign(treatment.checks.find(c => c.name === 'rejectsNullName')!, { fail: 2, pass: 3 });
  expect(securityIssues(treatment, control).delta).toBe(2);
  treatment.pending = 1;
  expect(securityIssues(treatment, control).delta).toBeNull();
  treatment.pending = 0; treatment.attempts = 6;
  expect(securityIssues(treatment, control).delta).toBeNull();
  const { data } = fixture();
  expect(combinationRows(data).every(r => r.issues.delta === null && !r.issues.hasControl)).toBe(true);
});
