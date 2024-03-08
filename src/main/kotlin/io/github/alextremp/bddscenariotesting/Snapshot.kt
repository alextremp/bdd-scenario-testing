package io.github.alextremp.bddscenariotesting

open class Snapshot<In, Out>(
    val execution: Execution<In, Out>
) {
    fun branch(description: String, creator: () -> Unit = {}, cleaner: () -> Unit = {}) =
        SubScenario(this, description, creator, cleaner)
}