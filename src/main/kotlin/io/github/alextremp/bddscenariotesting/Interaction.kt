package io.github.alextremp.bddscenariotesting

open class Interaction<In, Out>(
    val description: String,
    val action: (In) -> Out
)