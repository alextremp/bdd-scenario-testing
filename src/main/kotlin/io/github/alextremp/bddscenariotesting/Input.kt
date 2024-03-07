package io.github.alextremp.bddscenariotesting

class Input<In>(
    val scenario: Scenario,
    val givenData: GivenData<In>
) {

    fun <Out> on(interaction: Interaction<In, Out>): UnvalidatedSnapshot<In, Out> =
        UnvalidatedSnapshot(Execution(this, interaction))
}