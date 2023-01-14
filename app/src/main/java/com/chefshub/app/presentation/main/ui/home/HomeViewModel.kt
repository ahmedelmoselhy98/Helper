package com.chefshub.app.presentation.main.ui.home

import androidx.lifecycle.viewModelScope
import com.chefshub.base.BaseViewModel
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.home.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : BaseViewModel() {


    private val _dataListFlow = MutableSharedFlow<NetworkState>()
    val dataListFlow get() = _dataListFlow.asSharedFlow()

    fun searchFor(text: CharSequence?) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_dataListFlow, homeUseCase.searchForKey(text.toString()))
        }
    }
}