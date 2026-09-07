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
  contextExtraction: ContextExtraction; experimentPlans: ExperimentPlan[];
  securityProtocols: { protocol: string; evaluatedAt: string; environment: Record<string, string>; inputHashes: Record<string, string>; limitations: string[]; controls: { target: string; validated: boolean; expected: Record<string, string>; checks: Check[] }[] }[];
};
export type ExtractedFact = {
  id: string; type: string; category: string; scope: string; status: string; text: string; cwes: string[]; origins?: string;
  source: { path: string; sha256: string; line?: number; snippet?: string; member?: string | null; method?: string; rule?: string; record?: string; reportedLine?: number; reportedCode?: { path: string; member?: string; sha256: string } };
};
export type ContextExtraction = {
  extractor: string; fingerprint: string; artifact: Artifact; facts: ExtractedFact[]; limitations: string[];
  coverage: { factsByType: Record<string, number>; parsedFiles: number; sourceFiles: number; parseErrors: unknown[] };
};
export type PlannedCondition = {
  id: string; stage: string; strategy: string; baseContext: string; contextTypes: string[]; factIds: string[]; factCount: number;
  promptSha256: string; promptCharacters: number; promptBytes: number; promptTokens: number | null; repetitions: number; prompt: Artifact;
};
export type ExperimentPlan = {
  id: string; status: string; model: string; reasoning: string; temperature: number | null; maxOutputTokens: number;
  scope: string; injection: string; fingerprint: string; artifact: Artifact; scheduleSeed: number;
  conditions: PlannedCondition[]; schedule: { runId: string; condition: string; stage: string; repetition: number; status: string }[];
  analysis: { primary: string; secondary: string[]; sampleCaveat: string; controls: string; confounds: string; timing: string };
};
