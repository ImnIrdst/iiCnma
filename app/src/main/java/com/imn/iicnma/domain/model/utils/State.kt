package com.imn.iicnma.domain.model.utils

sealed class State<out V> {
    object Loading : State<Nothing>()
    data class Success<V>(val value: V) : State<V>()
    data class Failure(val error: IIError) : State<Nothing>()
}

fun <V> loadingState(): State<V> = State.Loading
fun <V> successState(value: V): State<V> = State.Success(value)
fun <V> failureState(error: IIError): State<V> = State.Failure(error)