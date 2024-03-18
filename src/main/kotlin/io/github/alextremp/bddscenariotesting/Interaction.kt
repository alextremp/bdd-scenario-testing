package io.github.alextremp.bddscenariotesting

class Interaction<In>(
    val input: DataProvider<In>,
    val description: String,
    val branch: Branch,
) {
    companion object {
        fun <In, Out> builtAction(description: String, action: Action<In, Out>) = Action.Built(description, action)
    }

    fun <Out> on(description: String, action: Action<In, Out>): ScenarioBranch<Out> =
        on(builtAction(description, action))

    fun <Out> on(action: Action.Built<In, Out>): ScenarioBranch<Out> =
        ScenarioBranch(
            action = { action.execute(input.get()) },
            description = "${action.description} (${this.description})",
            branch = branch
        )
}