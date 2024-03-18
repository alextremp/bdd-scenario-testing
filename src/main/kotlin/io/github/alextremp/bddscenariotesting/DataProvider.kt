package io.github.alextremp.bddscenariotesting

fun interface DataProvider<Data> {
    fun get(): Data
}