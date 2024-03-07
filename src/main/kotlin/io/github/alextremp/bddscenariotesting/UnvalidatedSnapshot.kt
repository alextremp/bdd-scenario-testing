package io.github.alextremp.bddscenariotesting

class UnvalidatedSnapshot<In, Out>(execution: Execution<In, Out>) : Snapshot<In, Out>(execution) {
    private val assertions = mutableListOf<Assertion<In, Out>>()

    fun then(description: String, assertion: (AssertionData<In, Out>) -> Unit): UnvalidatedSnapshot<In, Out> = apply {
        assertions.add(Assertion(description, assertion))
    }

    fun validate(): ValidatedSnapshot<In, Out> {
        val assertionResults = mutableListOf<Assertion.Result>()
        execution.execute {
            assertions.forEach { assertion ->
                assertion.validate(it).also { assertionResult ->
                    assertionResults.add(assertionResult)
                    val logCode = if (assertionResult.status is Assertion.AssertionStatus.PASSED) {
                        "\u001B[32m[success]\u001B[0m"
                    } else {
                        "\u001B[31m[error]\u001B[0m"
                    }
                    println("$logCode : ${assertion.description} => ${assertionResult.status}")
                }
            }
        }
        return ValidatedSnapshot(execution, assertionResults)
    }
}