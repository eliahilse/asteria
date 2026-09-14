ANGLE: DATA FLOW AND TRUST BOUNDARIES
Trace the data the change will introduce or touch from its sources to its sinks.
1. Locate the code the change will touch or imitate (existing state the feature reads, existing persistence, existing menus/panels). Use `outline.md` sink tags to find file, network, deserialization and class-loading sinks.
2. List the assets the change creates or uses and the property each must keep.
3. For each flow of untrusted data (files the user controls, server content, in-game values a user can tamper with), name the trust boundary: untrusted input, source, sink, whether it is an entry point (untrusted data enters the system there), and the transformations between them, with anchors.
4. For each boundary, enumerate threats with STRIDE and map them to weakness types (CWE) that could arise in the new code at that boundary. State reachability from the identified source.
5. Turn each reachable threat into a requirement on the operation that first receives the data, and a control at that enforcement point with explicit failure behavior (reject, bound, fall back).
6. Record verification for each requirement and every unknown edge you could not resolve.
Do not stop at the displayed result; cover reading, decoding and allocation while data crosses each boundary.
