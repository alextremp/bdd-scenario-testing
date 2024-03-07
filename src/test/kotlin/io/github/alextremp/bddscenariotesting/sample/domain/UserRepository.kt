package io.github.alextremp.bddscenariotesting.sample.domain

import java.util.UUID

interface UserRepository {
    fun save(user: User)
    fun findByCookieIds(cookieIds: Set<CookieId>): List<User>
    fun removeById(id: UUID)
}