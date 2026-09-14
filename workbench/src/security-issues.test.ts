import { expect, test } from 'vitest';
import { readFileSync } from 'node:fs';
import { PERCENT_MIN_N, combinationRows, fraction, fractionText, fractionValue, percentAllowed, percentText, securityIssues } from './combination-stats';
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
  expect(securityIssues(treatment, control)).toMatchObject({ detected: 4, evaluated: 49, unresolved: 1, expected: 50, delta: null });
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

test('quality cells keep numerator and denominator, and percentages never replace them', () => {
  const { data, summary } = fixture();
  const row = combinationRows(data).find(r => r.condition === summary.id)!;
  expect(row.attempts).toBe(5);
  expect(row.stats.compiled).toEqual({ numerator: summary.compiled, denominator: 5 });
  expect(row.stats.unit).toEqual({ numerator: 35, denominator: 35, units: 5 });
  expect(row.stats.autonomous).toEqual({ numerator: 25, denominator: 25, units: 5 });
  expect(fractionText(row.stats.unit)).toBe('35/35');
  // 28/35 checks rest on five correlated artifacts: no percentage, whereas 16/20 trajectories may carry one.
  expect(percentAllowed(row.stats.unit)).toBe(false);
  expect(percentAllowed(fraction(28, 35, { units: 5 }))).toBe(false);
  expect(percentAllowed(fraction(4, 5))).toBe(false);
  expect(percentAllowed(fraction(16, 20))).toBe(true);
  expect(fractionText(fraction(4, 5))).toBe('4/5');
  expect(fractionText(fraction(2, 3, { unresolved: 4 }))).toBe('2/3; 4 unresolved');
  expect(fractionValue(fraction(4, 5))).toBe(0.8);
  expect(percentText(fraction(4, 5))).toBe('80.0%');
  // An empty rate is not zero.
  expect(fractionText(fraction(0, 0))).toBeNull();
  expect(fractionValue(fraction(0, 0))).toBeNull();
  expect(percentText(fraction(0, 0))).toBeNull();
  expect(PERCENT_MIN_N).toBe(20);
});
