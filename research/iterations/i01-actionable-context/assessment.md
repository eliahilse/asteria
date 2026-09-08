# I01 assessment and next setup

The fixed 18-trajectory schedule completed. Fourteen trajectories passed all
sixteen functional checks within three submissions; eleven passed on the first
submission. Generation S+F+B with requirements context had 12 failed issue checks
out of 29 evaluated, versus 20/29 in its fresh control. Other comparisons had
unequal evaluation coverage, and the Reuse contexts were too sparse to establish
an overall security improvement. No across-the-board improvement is claimed.

Two concrete problems were found after collection:

1. **JVM home directories were not isolated.** Generated code repeatedly chose
   the same `.apomario-highscores` and `.apomario-highscores.dat` names under
   `System.getProperty("user.home")`. The v1 evaluator supplied a private
   `java.io.tmpdir`, but left `user.home` unchanged. Thus concurrent trajectories
   and suites could read or write each other's persisted game records. Failing
   diagnostics included repeated scores from other test executions. This is a
   setup confound; re-evaluation of the exact saved code with private JVM homes
   is required before attributing those failures to the model or context.
2. **Citation repair removed substantial context.** Candidate item counts went
   8→6 for Generation requirements, 7→5 for Generation boundaries, 8→5→2 for
   Reuse requirements, and 6→1 for Reuse boundaries. The final Reuse boundary
   insert only mentions an elapsed-time getter. The later setup should separate
   observed repository facts from prospective security recommendations and retain
   clearly marked recommendations when a claim about current implementation
   cannot be cited. It must not invent citations or silently replace this output.

The next step is an evaluator revision that gives every functional JVM and
security-probe JVM its own home and temporary directories. First calibrate it
against preserved reference code and re-evaluate I01's exact artifacts. Keep those
re-evaluations separate from the original reports. Then acquire fresh contexts
under a documented revised acquisition protocol and collect fresh controlled
trajectories. These revisions are exploratory development decisions informed by
I01; later replication must use newly acquired contexts and fresh code responses.

All I01 inputs, intermediate responses, rejected edit batches, reports and logs
are retained. Their values are not rewritten when the setup is corrected.
