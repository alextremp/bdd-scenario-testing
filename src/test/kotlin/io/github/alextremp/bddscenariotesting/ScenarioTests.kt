package io.github.alextremp.bddscenariotesting

import io.github.alextremp.bddscenariotesting.sample.SampleApplication
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsRequest
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsResponse
import io.github.alextremp.bddscenariotesting.sample.domain.IngestUserCookieIdsService
import io.github.alextremp.bddscenariotesting.sample.impl.InMemoryUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScenarioTests {
    private val userRepository = InMemoryUserRepository()
    private val ingestUserCookieIdsService = IngestUserCookieIdsService(userRepository)
    private val sampleApplication = SampleApplication(ingestUserCookieIdsService)

    private val ingestIngestUserCookieIdsRequestAction: (IngestUserCookieIdsRequest) -> IngestUserCookieIdsResponse =
        { data -> sampleApplication.ingest(data) }

    @Test
    fun `sample application scenarios`() {
        val theSystemHasNotUsersYetScenario = Scenario(
            description = "the system does not have any user yet",
            creator = {
                println("Creating the system")
            },
            cleaner = {
                println("Cleaning the system")
                userRepository.clear()
            },
        )

        val theSystemHasAnUnloggedUserScenario = theSystemHasNotUsersYetScenario
            .branch("the system has a user with an anonymous id")
            .given("a user which only has an anonymous id") { IngestUserCookieIdsRequestMother.firstUnloggedUser }
            .on("the data is posted", ingestIngestUserCookieIdsRequestAction)
            .then("the system creates a new user") { result ->
                assertThat(userRepository.count()).isEqualTo(1)
                val savedUser = userRepository.findById(result.userId)
                assertThat(savedUser).isNotNull
                assertThat(savedUser!!.cookieIds).containsAll(IngestUserCookieIdsRequestMother.firstUnloggedUser.cookieIds)
            }

        var lastPostedResult: IngestUserCookieIdsResponse? = null

        val theSystemReceivedTheSameUnloggedUserTwoTimesScenario = theSystemHasAnUnloggedUserScenario
            .branch("the system has a user with an anonymous id")
            .given("a user which only has an anonymous id") { IngestUserCookieIdsRequestMother.firstUnloggedUser }
            .on("the data is posted") { data ->
                ingestIngestUserCookieIdsRequestAction(data).also { lastPostedResult = it }
            }
            .then("the system does not create a new user so still has 1 user") {
                assertThat(userRepository.count()).isEqualTo(1)
                assertThat(it.userId).isEqualTo(lastPostedResult?.userId)
            }

        val theSystemHasALoggedUserScenario = theSystemHasAnUnloggedUserScenario
            .branch("the system has a user with an anonymous id and an email")
            .given("a user which has an anonymous id and an email") { IngestUserCookieIdsRequestMother.firstLoggedUser }
            .on("the data is posted", ingestIngestUserCookieIdsRequestAction)
            .then("the system updates the existing user with its email") {
                assertThat(userRepository.count()).isEqualTo(1)
                val savedUser = userRepository.findById(it.userId)
                assertThat(savedUser!!.cookieIds.filter { cookieId -> cookieId.key == "email" }).hasSize(1)
                assertThat(savedUser!!.cookieIds.filter { cookieId -> cookieId.key == "email" })
                    .isEqualTo(IngestUserCookieIdsRequestMother.firstLoggedUser.cookieIds.filter { cookieId -> cookieId.key == "email" })
            }

        val theSystemHasTwoUsersOneOfThemIsLoggedScenario = theSystemHasALoggedUserScenario
            .branch("the system has two users, one of them is logged")
            .given("a second user which only has an anonymous id") { IngestUserCookieIdsRequestMother.secondUnloggedUser }
            .on("the data is posted", ingestIngestUserCookieIdsRequestAction)
            .then("the system creates a new user") {
                assertThat(userRepository.count()).isEqualTo(2)
                assertThat(userRepository.findById(it.userId)).isNotNull
            }

        val theSystemHasOneUserWithTwoAnonymousIdButUniqueUserIdScenario = theSystemHasTwoUsersOneOfThemIsLoggedScenario
            .branch("the system has one user with two anonymous ids but unique user id")
            .given("a second user with anonymous id and email") { IngestUserCookieIdsRequestMother.secondLoggedUserAsSameFirstUser }
            .on("the data is posted", ingestIngestUserCookieIdsRequestAction)
            .then(
                "the system only keeps one user" to {
                    assertThat(userRepository.count()).isEqualTo(1)
                },
                "the system updates kept user with the second user's anonymous id" to {
                    val savedUser = userRepository.findById(it.userId)!!
                    assertThat(savedUser.cookieIds.filter { cookieId -> cookieId.key == "local-id" }).hasSize(2)
                    assertThat(savedUser.cookieIds).containsAll(IngestUserCookieIdsRequestMother.firstLoggedUser.cookieIds)
                    assertThat(savedUser.cookieIds).containsAll(IngestUserCookieIdsRequestMother.secondLoggedUserAsSameFirstUser.cookieIds)
                }
            )
    }
}