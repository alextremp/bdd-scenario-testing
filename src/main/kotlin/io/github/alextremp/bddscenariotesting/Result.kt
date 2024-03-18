package io.github.alextremp.bddscenariotesting

class Result<Data>(
    private val data: Data? = null,
    private val error: Throwable? = null,
) {
    companion object {
        fun <Data> success(data: Data): Result<Data> {
            return Result(data)
        }

        fun <Data> error(throwable: Throwable): Result<Data> {
            return Result(data = null, error = throwable)
        }
    }

    fun isSuccess(): Boolean = error == null

    fun isFailure(): Boolean = !isSuccess()

    fun dataOrNull(): Data? = data

    fun data(): Data = dataOrNull() ?: throw IllegalStateException("No data available")

    fun errorOrNull(): Throwable? = error

    fun error(): Throwable = errorOrNull() ?: throw IllegalStateException("No error available")
}