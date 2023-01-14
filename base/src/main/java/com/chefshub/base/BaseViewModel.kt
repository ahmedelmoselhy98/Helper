package com.chefshub.base

import ERROR_API
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.entity.NetworkState
import com.chefshub.utils.ext.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


open class BaseViewModel : ViewModel() {


    private var dispatcher: Job? = null

    /// un auth sharedFlow
    private val _unAuthorizedFlow = MutableSharedFlow<Boolean>()
    internal val unAuthorizedFlow get() =  _unAuthorizedFlow.asSharedFlow()

    // maintenance sharedFlow
    private val _maintenanceFlow = MutableSharedFlow<Boolean>()
    internal val maintenanceFlow = _maintenanceFlow.asSharedFlow()

    /// update sharedFlow
    private val _updateFlow = MutableSharedFlow<Boolean>()
    internal val updateFlow = _updateFlow.asSharedFlow()

    // network connection flow
    private val _connectionErrorFlow = MutableSharedFlow<Boolean>()
    internal val connectionErrorFlow = _connectionErrorFlow.asSharedFlow()


    protected fun <T> executeSharedFlow(
        sharedFlow: MutableSharedFlow<NetworkState>,
        request: Flow<T>
    ) {
        dispatcher = viewModelScope.launch(handlerSharedException(sharedFlow)) {
            request.onStart {
//                sharedFlow.emit(NetworkState.Loading)
            }
                .onCompletion { sharedFlow.emit(NetworkState.StopLoading) }
                .catch { sharedFlow.emit(NetworkState.Error(it)) }
                .collectLatest { sharedFlow.emit(NetworkState.Success(it)) }
        }
    }

    private fun handlerSharedException(state: MutableSharedFlow<NetworkState>): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            when (throwable.message) {
                ERROR_API.UNAUTHRIZED -> {
                    _unAuthorizedFlow.tryEmit(true)
                }
                ERROR_API.MAINTENANCE -> {
                    _maintenanceFlow.tryEmit(true)
                }
                ERROR_API.CONNECTION_ERROR -> {
                    _connectionErrorFlow.tryEmit(true)
                }
                else -> {
                    state.tryEmit(NetworkState.Error(throwable.handleException()))
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        dispatcher?.cancel()
    }

    fun isLogin(context: Context) = PreferencesGateway(context).isSaved(PrefKeys.IS_USER_LOGGED)
    fun logout() {
        viewModelScope.launch(Dispatchers.IO){
        _unAuthorizedFlow.emit(true)}
    }


}