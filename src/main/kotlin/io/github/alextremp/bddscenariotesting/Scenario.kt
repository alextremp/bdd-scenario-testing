package io.github.alextremp.bddscenariotesting

open class Scenario(
    val description: String,
    val creator: () -> Unit = {},
    val cleaner: () -> Unit = {},
) {

    fun <In> given(value: GivenData<In>): Input<In> =
        Input(this, value)

    fun <In> given(description: String, data: In): Input<In> =
        given(GivenData(description, data))
}