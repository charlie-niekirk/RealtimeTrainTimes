package me.cniekirk.realtimetrains.feature.departureboard

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.model.StationBoard
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.orbitmvi.orbit.test.test
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DepartureBoardViewModelTest {

    private val realtimeTrainsRepository = mockk<RealtimeTrainsRepository>()
    private val savedStateHandle = SavedStateHandle(
        route = DepartureBoard(
            searchStationCrs = "WAT",
            searchStationName = "London Waterloo",
            filterStationCrs = null,
            filterStationName = null,
            isDeparting = true
        )
    )

    private lateinit var sut: DepartureBoardViewModel

    @Before
    fun setup() {
        sut = DepartureBoardViewModel(savedStateHandle, realtimeTrainsRepository)
    }

    @Test
    fun `when viewModel runs onCreate then verify loadStationBoard executed and state reduced correctly`() = runTest {
        coEvery { realtimeTrainsRepository.getStationBoard(any(), any(), any()) } returns
                Result.Success(departureBoard)

        sut.test(this) {
            expectInitialState()

            runOnCreate()

            expectState {
                copy(
                    isLoading = false,
                    stationBoard = departureBoard
                )
            }
        }

        coVerify(exactly = 1) { realtimeTrainsRepository.getStationBoard(any(), any(), any()) }
    }

    @Test
    fun `when viewModel runs onCreate & loadStationBoard returns error then verify state reduced correctly`() = runTest {
        coEvery { realtimeTrainsRepository.getStationBoard(any(), any(), any()) } returns
                Result.Failure(Error.UnknownError)

        sut.test(this) {
            expectInitialState()

            runOnCreate()

            expectState {
                copy(
                    isLoading = false
                )
            }
        }

        coVerify(exactly = 1) { realtimeTrainsRepository.getStationBoard(any(), any(), any()) }
    }

    @Test
    fun `when onServiceClicked called then verify NavigateToServiceDetails effect posted with correct id`() = runTest {
        sut.test(this) {
            expectInitialState()

            containerHost.onServiceClicked(SERVICE_ID)

            expectSideEffect(DepartureBoardEffect.NavigateToServiceDetails(SERVICE_ID))
        }
    }

    companion object {
        const val SERVICE_ID = "123abc"
        val departureBoard = StationBoard.DepartureBoard("London Waterloo", persistentListOf())
    }
}