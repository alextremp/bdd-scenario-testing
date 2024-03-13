package io.github.alextremp.bddscenariotesting

class ScenarioBranch<Data>(
    val action: () -> Data,
    description: String,
    val branch: Branch,
) : Scenario(
    description = "${branch.scenario.description} > $description",
    creator = {
        branch.scenario.creator()
        action()
    },
    cleaner = branch.scenario.cleaner,
) {

    fun then(description: String, assertion: (Data) -> Unit): Scenario =
        then(description to assertion)

    fun then(vararg assertions: Pair<String, (Data) -> Unit>): Scenario =
        apply {
            Logger.info("Start $description > ${branch.description}")
            try {
                creator()
                action().also { data ->
                    assertions.forEach { (description, assertion) ->
                        try {
                            assertion(data)
                            Logger.success("✅ $description")
                        } catch (e: Throwable) {
                            Logger.error("❌ $description")
                            throw e
                        }
                    }
                }
            } finally {
                cleaner()
                Logger.info("Finished [${description}]")
            }
        }
}