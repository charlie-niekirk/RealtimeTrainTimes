package me.cniekirk.realtimetrains.feature.servicedetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.orbitmvi.orbit.test.test
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ServiceDetailsViewModelTest {

    private val realtimeTrainsRepository = mockk<RealtimeTrainsRepository>()

    private val savedStateHandle = SavedStateHandle(
        route = ServiceDetails(SERVICE_UID)
    )

    private lateinit var sut: ServiceDetailsViewModel

    @Before
    fun setUp() {
        sut = ServiceDetailsViewModel(savedStateHandle, realtimeTrainsRepository)
    }

    @Test
    fun `when viewModel runs onCreate then verify getServiceDetails executed and state reduced correctly`() = runTest {
        coEvery { realtimeTrainsRepository.getServiceDetails(any()) } returns
                Result.Success(details)

        sut.test(this) {
            expectInitialState()

            runOnCreate()

            expectState {
                copy(
                    isLoading = false,
                    serviceDetails = details
                )
            }
        }

        coVerify(exactly = 1) { realtimeTrainsRepository.getServiceDetails(any()) }
    }

    @Test
    fun `when viewModel runs onCreate & getServiceDetails returns Error then verify state reduced correctly`() = runTest {
        coEvery { realtimeTrainsRepository.getServiceDetails(any()) } returns
                Result.Failure(error)

        sut.test(this) {
            expectInitialState()

            runOnCreate()

            expectState {
                copy(
                    isLoading = false
                )
            }

            expectSideEffect(ServiceDetailsEffect.DisplayError(error))
        }

        coVerify(exactly = 1) { realtimeTrainsRepository.getServiceDetails(any()) }
    }

    companion object {
        const val SERVICE_UID = "123456"

        val details = me.cniekirk.realtimetrains.core.network.models.ServiceDetails(
            "",
            "",
            "",
            true,
            "",
            "",
            "",
            "",
            "",
            "",
            true,
            true,
            "",
            listOf(),
            listOf(),
            listOf()
        )
        val error = Error.UnknownError
    }
}