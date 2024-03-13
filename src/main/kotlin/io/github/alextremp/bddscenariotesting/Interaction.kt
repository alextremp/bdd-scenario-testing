package io.github.alextremp.bddscenariotesting

class Interaction<In>(
    val given: () -> In,
    val description: String,
    val branch: Branch,
) {
    fun <Out> on(description: String, action: (In) -> Out): ScenarioBranch<Out> =
        ScenarioBranch(
            action = { action(given()) },
            description = "$description (${this.description})",
            branch = branch
        )
}