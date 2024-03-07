package io.github.alextremp.bddscenariotesting.sample.domain

import java.util.UUID

class User private constructor(
    val id: UUID,
    private val mutableCookieIds: MutableSet<CookieId>,
) {

    val cookieIds: Set<CookieId>
        get() = mutableCookieIds.toSet()

    fun addCookieIds(cookieIds: Set<CookieId>) =
        this.mutableCookieIds.addAll(cookieIds)

    override fun toString() = "User(id=$id, cookieIds=$mutableCookieIds)"

    companion object {
        fun create(cookieIds: Set<CookieId>) =
            User(UUID.randomUUID(), cookieIds.toMutableSet())
    }
}