package me.cniekirk.realtimetrains.feature.departureboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DepartureBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val realtimeTrainsRepository: RealtimeTrainsRepository
) : ViewModel(), ContainerHost<DepartureBoardState, DepartureBoardEffect> {

    private val args = savedStateHandle.toRoute<DepartureBoard>()

    override val container = container<DepartureBoardState, DepartureBoardEffect>(DepartureBoardState(stationName = args.stationName)) {
        // Load departure board
        loadDepartures(args.stationCrs)
    }

    private suspend fun loadDepartures(station: String) = intent {
        when (val response = realtimeTrainsRepository.getDepartureBoard(station)) {
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
                        services = response.data.services
                    )
                }
            }
        }
    }
}