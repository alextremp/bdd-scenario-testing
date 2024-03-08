package io.github.alextremp.bddscenariotesting.sample.domain

import java.util.UUID

data class IngestUserCookieIdsResponse(
    val cookieIds: Set<CookieId>,
    val userId: UUID
)