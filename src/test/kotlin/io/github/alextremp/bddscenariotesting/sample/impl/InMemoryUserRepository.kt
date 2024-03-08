package io.github.alextremp.bddscenariotesting.sample.impl

import io.github.alextremp.bddscenariotesting.sample.domain.CookieId
import io.github.alextremp.bddscenariotesting.sample.domain.User
import io.github.alextremp.bddscenariotesting.sample.domain.UserRepository
import java.util.UUID

class InMemoryUserRepository : UserRepository {

    private val users = mutableMapOf<UUID, User>()

    override fun save(user: User) {
        users[user.id] = user
    }

    override fun findByCookieIds(cookieIds: Set<CookieId>): List<User> =
        users.values.filter { user ->
            user.cookieIds.any { cookieId ->
                cookieIds.contains(cookieId)
            }
        }

    override fun removeById(id: UUID) {
        users.remove(id)
    }

    fun clear() {
        users.clear()
    }

    fun count(): Int = users.size

    fun findById(id: UUID): User? = users[id]
}