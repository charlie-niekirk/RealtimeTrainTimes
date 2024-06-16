package me.cniekirk.realtimetrains.feature.departureboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DepartureBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val realtimeTrainsRepository: RealtimeTrainsRepository
) : ViewModel(), ContainerHost<DepartureBoardState, DepartureBoardEffect> {

    private val args = savedStateHandle.toRoute<DepartureBoard>()

    override val container = container<DepartureBoardState, DepartureBoardEffect>(
        DepartureBoardState(
            stationName = args.searchStationName,
            direction = if (args.isDeparting) Direction.DEPARTING else Direction.ARRIVING
        )
    ) {
        // Load station board
        loadStationBoard(
            StationCrs(args.searchStationCrs, args.searchStationName),
            if (args.filterStationCrs != null && args.filterStationName != null) {
                StationCrs(args.filterStationCrs, args.filterStationName)
            } else null,
            if (args.isDeparting) Direction.DEPARTING else Direction.ARRIVING
        )
    }

    private fun loadStationBoard(
        searchStation: StationCrs,
        filterStation: StationCrs?,
        direction: Direction
    ) = intent {
        when (val response = realtimeTrainsRepository.getStationBoard(searchStation, filterStation, direction)) {
            is Result.Failure -> {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
                // Emit some error effect
            }
            is Result.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                        stationBoard = response.data
                    )
                }
            }
        }
    }

    fun onServiceClicked(id: String) = intent {
        postSideEffect(DepartureBoardEffect.NavigateToServiceDetails(id))
    }
}