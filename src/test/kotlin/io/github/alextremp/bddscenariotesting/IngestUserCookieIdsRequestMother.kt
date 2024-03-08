package io.github.alextremp.bddscenariotesting

import io.github.alextremp.bddscenariotesting.sample.domain.CookieId
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsRequest

object IngestUserCookieIdsRequestMother {
    val firstUnloggedUser = IngestUserCookieIdsRequest(setOf(CookieId("local-id", "value-1")))
    val firstLoggedUser =
        IngestUserCookieIdsRequest(setOf(CookieId("local-id", "value-1"), CookieId("email", "email-1")))
    val secondUnloggedUser = IngestUserCookieIdsRequest(setOf(CookieId("local-id", "value-2")))
    val secondLoggedUserAsSameFirstUser =
        IngestUserCookieIdsRequest(setOf(CookieId("local-id", "value-2"), CookieId("email", "email-1")))
}