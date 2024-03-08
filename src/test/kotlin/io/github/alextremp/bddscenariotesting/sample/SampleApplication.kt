package io.github.alextremp.bddscenariotesting.sample

import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsRequest
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsResponse
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsService

class SampleApplication(
    private val ingestUserCookieIdsService: IngestUserCookieIdsService
) {

    fun ingest(ingestUserCookieIdsRequest: IngestUserCookieIdsRequest): IngestUserCookieIdsResponse {
        return ingestUserCookieIdsService.execute(ingestUserCookieIdsRequest)
    }
}