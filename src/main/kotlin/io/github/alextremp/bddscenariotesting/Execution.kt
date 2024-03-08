package io.github.alextremp.bddscenariotesting

open class Execution<In, Out>(
    val input: Input<In>,
    val interaction: Interaction<In, Out>
) {

    private fun runExecution(): Out {
        if (input.scenario is SubScenario<*, *>) {
            input.scenario.from.execution.runExecution()
        }
        input.scenario.creator()
        return interaction.action(input.givenData.data)
    }

    private fun finish() {
        input.scenario.cleaner()
        if (input.scenario is SubScenario<*, *>) {
            input.scenario.from.execution.finish()
        }
    }

    fun execute(action: (AssertionData<In, Out>) -> Unit) {
        println("\u001B[34mExecuting > \u001B[0m${input.scenario.description} > ${interaction.description} with data: ${input.givenData.data}")
        try {
            val out = runExecution()
            action(AssertionData(input.givenData.data, out))
        } catch (error: Throwable) {
            println("\u001B[31mError executing > \u001B[0m${input.scenario.description} > ${interaction.description} with data: ${input.givenData.data}")
            throw error
        } finally {
            finish()
        }
    }
}