package io.github.alextremp.bddscenariotesting

open class Scenario(
    val description: String,
    val creator: () -> Unit = {},
    val cleaner: () -> Unit = {},
) {

    fun branch(description: String): Branch =
        Branch(
            description = description,
            scenario = this,
        )
}
