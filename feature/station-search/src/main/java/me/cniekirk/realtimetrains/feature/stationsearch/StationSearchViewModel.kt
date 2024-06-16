package me.cniekirk.realtimetrains.feature.stationsearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.CrsRepository
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StationSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val crsRepository: CrsRepository
) : ViewModel(), ContainerHost<StationSearchState, StationSearchEffect> {

    private val args = savedStateHandle.toRoute<StationSearch>()

    override val container = container<StationSearchState, StationSearchEffect>(StationSearchState()) {
        getAllStations()
    }

    private fun getAllStations() = intent {
        when (val response = crsRepository.getAllStations()) {
            is Result.Failure -> {
                // Emit some error
                Timber.e("ERROR!")
            }
            is Result.Success -> {
                Timber.d("GREAT!")
                reduce {
                    state.copy(trainStations = response.data)
                }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    fun queryStations(query: String) = blockingIntent {
        reduce {
            state.copy(searchQuery = query)
        }
        getStations(query)
    }

    private fun getStations(query: String) = intent {
        when (val response = crsRepository.queryStations(query)) {
            is Result.Failure -> {
                // Emit some error
            }
            is Result.Success -> {
                reduce {
                    state.copy(trainStations = response.data)
                }
            }
        }
    }

    fun stationSelected(stationCrs: StationCrs) = intent {
        postSideEffect(StationSearchEffect.StationSelected(stationCrs.crsCode, stationCrs.stationName, args.stationType))
    }
}