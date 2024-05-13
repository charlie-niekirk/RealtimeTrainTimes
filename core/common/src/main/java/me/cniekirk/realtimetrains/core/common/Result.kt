package me.cniekirk.realtimetrains.core.common

sealed interface Result<out T> {

    data class Success<out T>(val data: T) : Result<T>
    data class Failure(val error: Error) : Result<Nothing>
}

sealed class Error {

    data object UnknownError : Error()
}