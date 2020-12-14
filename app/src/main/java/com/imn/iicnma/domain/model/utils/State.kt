package com.imn.iicnma.domain.model.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

sealed class State<out R> {
    object Loading : State<Nothing>()
    data class Success<out V>(val value: V) : State<V>()
    data class Failure(val error: IIError) : State<Nothing>()
}

fun <V> loadingState(): State<V> = State.Loading
fun <V> successState(value: V): State<V> = State.Success(value)
fun <V> failureState(error: IIError): State<V> = State.Failure(error)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
fun <T> withStates(debounce: Long = 0, flowProvider: () -> Flow<T>): Flow<State<T>> {
    return flow {
        emit(loadingState<T>())

        flowProvider.invoke()
            .debounce(debounce)
            .mapLatest { successState(it) }
            .catch { emit(failureState(it.toIIError())) }
            .also { emitAll(it) }
    }
}