package com.chefshub.app.presentation.select_pref

import androidx.lifecycle.viewModelScope
import com.chefshub.base.BaseViewModel
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.auth.PrefUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefViewModel @Inject constructor(private val prefUseCase: PrefUseCase) : BaseViewModel() {


    private val _foodSystemFlow = MutableSharedFlow<NetworkState>()
    val foodSystemModel get() = _foodSystemFlow
    fun getSystemFood() {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_foodSystemFlow, prefUseCase.getFoodSystem())
        }
    }

    fun getRegionalCuisines() {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_foodSystemFlow, prefUseCase.getRegionalCuisines())
        }
    }

}
