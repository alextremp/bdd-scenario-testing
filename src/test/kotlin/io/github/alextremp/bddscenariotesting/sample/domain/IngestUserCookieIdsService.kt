package io.github.alextremp.bddscenariotesting.sample.domain

class IngestUserCookieIdsService(
    private val userRepository: UserRepository
) {

    fun execute(request: IngestUserCookieIdsRequest): IngestUserCookieIdsResponse {
        val usersWithAnyExistingCookieId = userRepository.findByCookieIds(request.cookieIds)
        return when (usersWithAnyExistingCookieId.size) {
            0 -> {
                val user = User.create(cookieIds = request.cookieIds)
                userRepository.save(user)
                user
            }

            1 -> {
                val user = usersWithAnyExistingCookieId.first()
                user.addCookieIds(request.cookieIds)
                userRepository.save(user)
                user
            }

            else -> {
                val firstUser = usersWithAnyExistingCookieId.first()
                val otherUsers = usersWithAnyExistingCookieId.drop(1)
                otherUsers.forEach {
                    firstUser.addCookieIds(it.cookieIds)
                    userRepository.removeById(it.id)
                }
                userRepository.save(firstUser)
                firstUser
            }
        }.let { user ->
            IngestUserCookieIdsResponse(
                userId = user.id,
                cookieIds = user.cookieIds
            )
        }
    }
}