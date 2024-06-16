package me.cniekirk.realtimetrains.feature.livetrains

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import me.cniekirk.realtimetrains.core.common.Direction
import me.cniekirk.realtimetrains.core.common.RecentTrainSearch
import me.cniekirk.realtimetrains.core.data.repository.LocalRepository
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LiveTrainsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository
) : ViewModel(), ContainerHost<LiveTrainsState, LiveTrainsEffect> {

    override val container = container<LiveTrainsState, LiveTrainsEffect>(LiveTrainsState()) {
        getRecentSearches()
    }

    private fun getRecentSearches() = intent {
        val recentSearches = localRepository.recentSearches()
        reduce {
            state.copy(
                recentTrainSearches = recentSearches.map { it.toImmutableList() }
            )
        }
    }

    fun changeSearchStation(stationCrs: StationCrs) = intent {
        reduce {
            state.copy(searchStation = stationCrs)
        }
    }

    fun changeFilterStation(stationCrs: StationCrs) = intent {
        reduce {
            state.copy(filterStation = stationCrs)
        }
    }

    fun searchStationClicked() = intent {
        postSideEffect(LiveTrainsEffect.NavigateToStationSearch(false))
    }

    fun filterStationClicked() = intent {
        postSideEffect(LiveTrainsEffect.NavigateToStationSearch(true))
    }

    fun searchStationCleared() = intent {
        reduce {
            state.copy(searchStation = null)
        }
    }

    fun filterStationCleared() = intent {
        reduce {
            state.copy(filterStation = null)
        }
    }

    fun departingClicked() = intent {
        reduce {
            state.copy(direction = Direction.DEPARTING)
        }
    }

    fun arrivingClicked() = intent {
        reduce {
            state.copy(direction = Direction.ARRIVING)
        }
    }

    fun searchClicked() = intent {
        state.searchStation?.apply {
            postSideEffect(
                LiveTrainsEffect.NavigateToDepartureBoard(
                    searchStation = this,
                    filterStation = state.filterStation,
                    direction = state.direction
                )
            )
        }
    }

    fun recentSearchClicked(recentTrainSearch: RecentTrainSearch) = intent {
        postSideEffect(
            LiveTrainsEffect.NavigateToDepartureBoard(
                searchStation = StationCrs(recentTrainSearch.searchCrs, recentTrainSearch.searchName),
                filterStation = if (recentTrainSearch.filterName.isNotEmpty()) StationCrs(recentTrainSearch.filterCrs, recentTrainSearch.filterName) else null,
                direction = recentTrainSearch.direction
            )
        )
    }
}