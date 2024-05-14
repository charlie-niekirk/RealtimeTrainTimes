package me.cniekirk.realtimetrains.feature.livetrains

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LiveTrainsViewModel @Inject constructor(

) : ViewModel(), ContainerHost<LiveTrainsState, LiveTrainsEffect> {

    override val container = container<LiveTrainsState, LiveTrainsEffect>(LiveTrainsState())

    fun searchStationClicked() = intent {
        postSideEffect(LiveTrainsEffect.NavigateToStationSearch(false))
    }

    fun filterStationClicked() = intent {
        postSideEffect(LiveTrainsEffect.NavigateToStationSearch(true))
    }

    fun searchClicked() = intent {
        state.searchStation?.apply {
            postSideEffect(LiveTrainsEffect.NavigateToDepartureBoard(crsCode, stationName))
        }
    }
}