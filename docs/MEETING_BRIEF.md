# Highscore security-context study: current research record

This is an exploratory study of one feature in the Apo-Games corpus. The
contribution at this stage is a traceable context representation, a frozen future
comparison, and executable counterexamples in preserved outputs. No Luna
experiment results have been collected through the new runner yet.

## What security context was used before?

The historical pilot supplied five manually curated constraints as one Markdown
block, inserted after the source/context attachments and before the output
instructions. It mixed security facts and requirements. It was static, not
automatically extracted, and must not be retrospectively labeled C1–C4.
The workbench preserves and diffs the exact submitted prompts by SHA-256.

## Were the tests security tests?

The 16 original checks measure feature functionality: seven unit, four invoked
integration and five autonomous integration checks. Their success does not
establish security. The six historical adversarial checks did not execute because
Java was unavailable; their saved errors are infrastructure observations.

The new `highscore-security-v1` protocol executes 11 declared properties in
separate JVMs with fresh stores, a 64 MiB heap and a 15-second process budget.
Safe and deliberately weak controls establish that the harness distinguishes the
expected outcomes in the recorded environment. It tests record validation,
retention bounds, malformed stores, long physical lines, a million persisted
records and a harmless native-deserialization canary. These finite fixtures do
not prove universal security, and the canary is not a production RCE chain.

## What do the existing results establish?

| Evidence | Denominator | Observation | Interpretation |
|---|---:|---|---|
| Complete fresh pilot | 16 attempts | 4 compiled; 1 passed all functional checks | Small historical feasibility sample |
| Fresh pilot without security block | 8 attempts | 3 compiled; 1 full functional pass | Both models and strategies; descriptive aggregate |
| Fresh pilot with security block | 8 attempts | 1 compiled; 0 full functional passes | Does not establish a causal context effect |
| New security evaluation of compiling fresh outputs | 4 outputs | Each failed at least one declared property | Conditional on compilation; 12 other attempts unevaluated |
| Security-context fresh output | 1 output | 10/11 properties passed; physical-line fixture exhausted heap | Concrete residual resource-bound counterexample |
| Selected published outputs | 6 of 80 original attempts | Six fully functional outputs, each with a source-reviewed finding | Success-selected subset; cannot estimate all-attempt prevalence |

The new controlled evaluation contains 110 experimental check observations
across ten preserved outputs, plus 22 control observations. Source review,
scanner candidates and executable outcomes are separately recorded. In
particular, the earlier “no fixed targeted findings” assessment remains intact
beside the newly observed physical-line failure; neither silently replaces the
other. See [the reevaluation report](SECURITY_REEVALUATION.md).

## Can context be divided into automatically extractable types?

| Type | Representation and source | Highscore projection | Both source trees |
|---|---|---:|---:|
| C1 · security surface | Parsed API calls/imports/endpoint literals; exact syntax and location | 12 | 122 |
| C2 · local input-flow candidates | Source-order method-local reads to operations; unresolved guards/types explicit | 3 | 4 |
| C3 · historical audit leads | Matched audit records retaining their original review status | 2 | 13 |
| C4 · requirements | Versioned human-authored threat-model policy, selected automatically | 6 | 6 |
| Union | Distinct typed records, not confirmed vulnerabilities | 23 | 145 |

The extractor parsed 168/168 source files. Each record has a content-derived ID,
type, category, scope, evidence status, CWE review tags and source hash/location
or policy rule. This permits inspection of encryption API names and literal
algorithm arguments, remote-call syntax, persistence APIs and external imports
when present. It does not resolve dependency versions, prove an algorithm's
security purpose, query vulnerability databases, or establish runtime reachability.
The absence of an extracted record is not evidence of absence.

Scope is an independent dimension: the current Highscore projection uses
filenames; it is not a dependency slice. C1–C4 are information types, not four
levels of sophistication or confidence. C3 retrieval automates access to an audit;
it does not make that historical model-assisted audit an automatic validator.
C4 cannot be inferred solely from code because intended trust and acceptable
resource budgets are study assumptions. See [extractor methods](../research/context/README.md).

## What is the next comparison?

The frozen design requests `gpt-5.6-luna`, medium reasoning, no temperature
parameter and at most 65,536 output tokens. Provider settings and served model
must be recorded; the workbench does not invent model runs or costs.

1. **Historical-prompt bridge:** four exact archived conditions, five repetitions
   each (20 attempts). Generation uses structural context; reuse uses S+F+B.
2. **Security ablation:** no security facts, C1, C2, C3, C4 and their union, for
   generation and reuse, ten repetitions each (120 attempts). Every arm has the
   same task/source payload, threat-model disclosure and output instructions.

The source task's conflicting output instructions are corrected consistently in
the ablation. This means bridge-versus-ablation differences cannot be attributed
solely to security context. Within the ablation, compare treatments to the
zero-fact arm of the same strategy. The randomized block seed controls execution
order, not model sampling; matching repetition numbers do not create paired seeds.

Primary outcome: complete functional success **and** all declared security
properties passing, over all attempted generations. Secondary records include
per-property pass/fail/unknown/unevaluated, compilation, context fact counts,
exact prompt size, reported token use and billed cost where available. Neither
test cases nor context facts are extra independent experimental replicates.
Sample sizes are exploratory, without a power calculation or broad
generalization claim. The design is versioned in Git, not externally preregistered.

The adapter is configured locally through environment variables; implementation,
credentials and received observations can stay gitignored. Run records are
durable before submission; retries are never implicit. See [adapter protocol](../research/ADAPTER.md).

## What remains scientifically unresolved?

- Collect the bridge and ablation responses using the real provider adapter;
  execute and inspect functional/security evaluations before drawing conclusions.
- Independently label an extraction sample to estimate precision and coverage.
  Current parser controls establish selected mechanics, not extraction recall.
- Predeclare any inferential contrasts and multiplicity treatment before
  examining the future outcomes. The current workbench reports descriptive
  Wilson intervals, not significance tests.
- Evaluate different scope projections and dynamic regeneration as separately
  versioned experiments. Neither has been measured yet.
- Map the supervisor's spreadsheet fields once supplied. The present export
  already retains granular observations and long evidence without truncation.

The React workbench is the inspection interface; JSON and XLSX provide portable
records. Its current results are suitable for discussing methods and specific
counterexamples, not for claiming that security context generally improves models.
