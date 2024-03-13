package io.github.alextremp.bddscenariotesting

class Branch(
    val description: String,
    val scenario: Scenario,
) {
    fun <Out> given(description: String, given: () -> Out): Interaction<Out> =
        Interaction(
            given = given,
            description = "$description",
            branch = this
        )
}