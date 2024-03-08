package io.github.alextremp.bddscenariotesting

class Assertion<In, Out>(
    val description: String,
    val assertion: (AssertionData<In, Out>) -> Unit
) {
    private var status: AssertionStatus = AssertionStatus.PENDING

    fun validate(data: AssertionData<In, Out>): Result {
        try {
            assertion(data)
            status = AssertionStatus.PASSED
        } catch (error: Throwable) {
            status = AssertionStatus.FAILED(error)
        }
        return Result(description, status)
    }

    interface AssertionStatus {
        object PENDING : AssertionStatus
        object PASSED : AssertionStatus
        data class FAILED(val error: Throwable) : AssertionStatus
    }

    data class Result(
        val description: String,
        val status: AssertionStatus
    )
}