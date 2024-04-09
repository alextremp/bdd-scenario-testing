package io.github.alextremp.bddscenariotesting

class ScenarioBranch<Data>(
    val action: () -> Data,
    description: String,
    val branch: Branch,
) : Scenario(
    description = "${branch.scenario.description} > $description",
) {
    private var lastResult: Result<Data>? = null

    override val reset: () -> Unit = {
        lastResult = null
        branch.scenario.reset()
        runCatching { action() }
            .onSuccess { lastResult = Result.success(it) }
            .onFailure { lastResult = Result.error(it) }
    }

    fun then(description: String, assertion: (Result<Data>) -> Unit): Scenario =
        then(description to assertion)

    fun then(vararg assertions: Pair<String, (Result<Data>) -> Unit>): Scenario = apply {
        Logger.info("Start $description > ${branch.description}")
        try {
            reset()
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