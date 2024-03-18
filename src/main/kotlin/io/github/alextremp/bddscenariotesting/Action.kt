package io.github.alextremp.bddscenariotesting

fun interface Action<In, Out> {

    fun execute(input: In): Out

    class Built<In, Out>(
        val description: String,
        private val action: Action<In, Out>,
    ) : Action<In, Out> {

        override fun execute(input: In): Out = action.execute(input)
    }
}