package io.github.alextremp.bddscenariotesting

open class Scenario(
    val description: String,
    val reset: () -> Unit = {}
) {

    fun branch(description: String): Branch =
        Branch(
            description = description,
            scenario = this,
        )
}
