package me.cniekirk.realtimetrains.feature.servicedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ServiceDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val realtimeTrainsRepository: RealtimeTrainsRepository
): ViewModel(), ContainerHost<ServiceDetailsState, ServiceDetailsEffect> {

    private val args = savedStateHandle.toRoute<ServiceDetails>()

    override val container = container<ServiceDetailsState, ServiceDetailsEffect>(ServiceDetailsState()) {
        getServiceDetails(args.serviceUid)
    }

    private fun getServiceDetails(serviceUid: String) = intent {
        when (val response = realtimeTrainsRepository.getServiceDetails(serviceUid)) {
            is Result.Failure -> {
                Timber.e("Some error occurred!")
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(ServiceDetailsEffect.DisplayError(response.error))
            }
            is Result.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                        serviceDetails = response.data
                    )
                }
            }
        }
    }
}