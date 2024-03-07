package io.github.alextremp.bddscenariotesting

class SubScenario<In, Out>(
    val from: Snapshot<In, Out>,
    description: String,
    creator: () -> Unit = {},
    cleaner: () -> Unit = {},
) : Scenario(description, creator, cleaner)