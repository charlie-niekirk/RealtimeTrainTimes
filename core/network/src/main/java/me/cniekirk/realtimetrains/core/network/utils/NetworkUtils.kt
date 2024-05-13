package me.cniekirk.realtimetrains.core.network.utils

import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.common.Result
import retrofit2.Response
import timber.log.Timber

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            if (response.code() == 400) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() == 401) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() in 400..499) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() in 500..599) {
                Result.Failure(Error.UnknownError)
            } else {
                Result.Failure(Error.UnknownError)
            }
        }
    } catch (error: Throwable) {
        Timber.e(error)
        Result.Failure(Error.UnknownError)
    }
}