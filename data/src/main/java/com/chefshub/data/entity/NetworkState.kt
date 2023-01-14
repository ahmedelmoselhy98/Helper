package com.chefshub.data.entity

sealed class NetworkState {
    object Loading : NetworkState()
    object StopLoading : NetworkState()
    object Idle : NetworkState()
    data class Success<T>(val data: T) : NetworkState()
    data class Error(val throwable: Throwable) : NetworkState()
}
