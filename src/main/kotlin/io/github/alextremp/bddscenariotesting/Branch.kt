package io.github.alextremp.bddscenariotesting

class Branch(
    val description: String,
    val scenario: Scenario,
) {
    fun <In> given(description: String, input: DataProvider<In>): Interaction<In> =
        Interaction(
            input = input,
            description = "$description",
            branch = this
        )
}