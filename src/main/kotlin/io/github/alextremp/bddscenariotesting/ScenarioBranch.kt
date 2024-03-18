package io.github.alextremp.bddscenariotesting

class ScenarioBranch<Data>(
    val action: () -> Data,
    description: String,
    val branch: Branch,
) : Scenario(
    description = "${branch.scenario.description} > $description",
    reset = {
        branch.scenario.reset()
        action()
    },
) {

    private var lastResult: Result<Data>? = null

    fun then(description: String, assertion: (Result<Data>) -> Unit): ScenarioBranch<Data> =
        then(description to assertion)

    fun then(vararg assertions: Pair<String, (Result<Data>) -> Unit>): ScenarioBranch<Data> =
        apply {
            Logger.info("Start $description > ${branch.description}")
            try {
                lastResult = null
                reset()
                runCatching { action() }
                    .onSuccess { lastResult = Result.success(it) }
                    .onFailure { lastResult = Result.error(it) }
                assertions.forEach { (description, assertion) ->
                    try {
                        assertion(lastResult())
                        Logger.success("✅ $description")
                    } catch (e: Throwable) {
                        Logger.error("❌ $description")
                        throw e
                    }
                }
            } finally {
                Logger.info("Finished [${description}]")
            }
        }

    fun lastResult(): Result<Data> = lastResult ?: throw IllegalStateException("Scenario has not been executed yet")
}