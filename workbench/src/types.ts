export type Status = 'pass' | 'fail' | 'not_run' | 'compile_error' | 'infrastructure_error' | 'unknown';
export type Artifact = { path: string; sha256: string; bytes: number; href: string };
export type Check = { suite: string; name: string; status: Status; detail: string; source: string };
export type Finding = { cwe: string; signature: string; severity: string; reason: string; status: string; evidence: Artifact; lines: number[]; dynamicProof: boolean };
export type Fact = { id: string; type: string; text: string; cwes: string[]; source: Artifact; status: string; scope: string; extraction: string };
export type SecurityEvaluation = { run_id: string; status: Status; checks: Check[]; source: string; protocol: string; environment: Record<string, string>; detail?: string; controlsPassed?: boolean; inputHashes?: Record<string, string> };
export type Run = {
  id: string; cohort: string; feature: string; model: string; servedModel: string | null;
  reasoning: string | null; temperature: number | null; strategy: string; condition: string;
  baseContext: string; securityContext: boolean; contextTypes: string[]; factIds: string[];
  repetition: number; prompt: Artifact | null; promptChars: number | null;
  attachments: { kind: string; name: string; bytes: number; sha256: string }[];
  usage: Record<string, number | null>; elapsedSeconds: number | null; costUsd: number | null;
  submittedAt: string | null; finishReason: string | null; compileStatus: Status; compileDetail: string;
  functionalSuccess: boolean; tests: Check[]; reportedSuites?: Record<string, string>;
  assessment: 'not_reviewed' | 'findings_present' | 'no_targeted_findings'; reviewNotes: string[];
  findings: Finding[]; candidates: { cwe: string; signature: string; severity: string; category: string; evidence: { file: string; line: number; code: string }[] }[];
  code: Artifact[]; rawResponse: Artifact | null; source: string; securityEvaluation: SecurityEvaluation | null;
};
export type Dataset = {
  schemaVersion: number; title: string; fingerprint: string; artifactCommit: string;
  cohorts: { id: string; label: string; attempts: number; selection: string }[];
  threatModel: string; facts: Fact[]; runs: Run[]; limitations: string[]; artifacts: Artifact[];
};
