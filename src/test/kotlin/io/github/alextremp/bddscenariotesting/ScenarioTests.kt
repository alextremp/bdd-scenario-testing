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

        val anUnloggedUserGivenData = GivenData(
            description = "a user which only has an anonymous id",
            data = IngestUserCookieIdsRequestMother.firstUnloggedUser,
        )

        var lastPostedResult: IngestUserCookieIdsResponse? = null
        val postingCookieIdsRequestInteraction = Interaction<IngestUserCookieIdsRequest, IngestUserCookieIdsResponse>(
            description = "the data is posted",
            action = { data -> sampleApplication.ingest(data).also { lastPostedResult = it } },
        )

        val theSystemHasAnUnloggedUserSnapshot = theSystemHasNotUsersYetScenario
            .given(anUnloggedUserGivenData)
            .on(postingCookieIdsRequestInteraction)
            .then("the system creates a new user") {
                assertThat(userRepository.count()).isEqualTo(1)
                assertThat(userRepository.findById(it.output.userId)).isNotNull
            }
            .validate()

        val theSystemHasAnUnloggedUserScenario = theSystemHasAnUnloggedUserSnapshot.branch(
            description = "the system has a user with an anonymous id",
        )

        val theSystemReceivedTheSameUnloggedUserTwoTimesSnapshot = theSystemHasAnUnloggedUserScenario
            .given(anUnloggedUserGivenData)
            .on(postingCookieIdsRequestInteraction)
            .then("the system does not create a new user so still has 1 user") {
                assertThat(userRepository.count()).isEqualTo(1)
                assertThat(it.output.userId).isEqualTo(lastPostedResult?.userId)
            }
            .validate()

        val aLoggedUserWithPreviousAnonymousIdGivenData = GivenData(
            description = "a user which has an anonymous id and a logged email",
            data = IngestUserCookieIdsRequestMother.firstLoggedUser,
        )

        val theSystemHasALoggedUserSnapshot = theSystemHasAnUnloggedUserScenario
            .given(aLoggedUserWithPreviousAnonymousIdGivenData)
            .on(postingCookieIdsRequestInteraction)
            .then("the system updates the existing user with its email") {
                assertThat(userRepository.count()).isEqualTo(1)
                val savedUser = userRepository.findById(it.output.userId)
                assertThat(savedUser!!.cookieIds.filter { cookieId -> cookieId.key == "email" }).hasSize(1)
                assertThat(savedUser!!.cookieIds.filter { cookieId -> cookieId.key == "email" })
                    .isEqualTo(aLoggedUserWithPreviousAnonymousIdGivenData.data.cookieIds.filter { cookieId -> cookieId.key == "email" })
            }
            .validate()

        val theSystemHasALoggedUserScenario = theSystemHasALoggedUserSnapshot.branch(
            description = "the system has a user with an anonymous id and a logged email",
        )

        val aSecondUnloggedUserGivenData = GivenData(
            description = "a second user which only has an anonymous id",
            data = IngestUserCookieIdsRequestMother.secondUnloggedUser
        )

        val theSystemHasTwoUsersOneOfThemIsLoggedSnapshot = theSystemHasALoggedUserScenario
            .given(aSecondUnloggedUserGivenData)
            .on(postingCookieIdsRequestInteraction)
            .then("the system creates a new user") {
                assertThat(userRepository.count()).isEqualTo(2)
                assertThat(userRepository.findById(it.output.userId)).isNotNull
            }
            .validate()

        val theSystemHasTwoUsersOneOfThemIsLoggedScenario = theSystemHasTwoUsersOneOfThemIsLoggedSnapshot.branch(
            description = "the system has two users, one of them is logged",
        )

        val aSecondLoggedUserIsReceivedWithTheFirstUserEmailGivenData = GivenData(
            description = "a second user which has the same email as the first user",
            data = IngestUserCookieIdsRequestMother.secondLoggedUserAsSameFirstUser,
        )

        val theSystemHasOneUserWithTwoAnonymousIdButUniqueUserIdSnapshot = theSystemHasTwoUsersOneOfThemIsLoggedScenario
            .given(aSecondLoggedUserIsReceivedWithTheFirstUserEmailGivenData)
            .on(postingCookieIdsRequestInteraction)
            .then("the system only keeps one user") {
                assertThat(userRepository.count()).isEqualTo(1)
            }
            .then("the system updates kept user with the second user's anonymous id") {
                val savedUser = userRepository.findById(it.output.userId)
                assertThat(savedUser!!.cookieIds.filter { cookieId -> cookieId.key == "local-id" }).hasSize(2)
            }
            .validate()
    }
}