export type Status = 'pass' | 'fail' | 'not_run' | 'unknown' | 'compile_error' | 'infrastructure_error';
export type Artifact = { path: string; sha256: string; bytes: number; href: string };
export type Check = { id: string; suite: string; name: string; status: Status; detail: string; diagnostics?: string };
export type TestDefinition = { id: string; suite: string; name: string; label: string; kind: 'security' | 'functional'; category: string; cwes: string[]; fixture: string; expected: string; limits: string; source: Artifact };
export type Run = {
  id: string; planId: string; condition: string; stage: string; strategy: string; repetition: number;
  model: string; reasoning: string; status: string; errorCategory: string | null; startedAt: string | null;
  elapsedSeconds: number | null; usage: Record<string, number | null>; costUsd: number | null; finishReason: string | null;
  compileStatus: Status; checks: Check[]; evaluationSignature: string | null;
  observation: Artifact; evaluation: Artifact | null; prompt: Artifact; factIds: string[]; contextTypes: string[];
};
export type Dataset = { schemaVersion: 2; title: string; fingerprint: string; runs: Run[]; tests: TestDefinition[]; contextExtraction: ContextExtraction; experimentPlans: ExperimentPlan[]; artifacts: Artifact[] };
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
