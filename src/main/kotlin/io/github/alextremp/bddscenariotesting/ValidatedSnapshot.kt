package io.github.alextremp.bddscenariotesting

class ValidatedSnapshot<In, Out>(
    execution: Execution<In, Out>,
    assertions: List<Assertion.Result>
) : Snapshot<In, Out>(execution)