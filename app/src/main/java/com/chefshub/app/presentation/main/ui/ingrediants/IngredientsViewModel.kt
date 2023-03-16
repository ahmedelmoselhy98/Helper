package com.chefshub.app.presentation.main.ui.ingrediants

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.chefshub.base.BaseViewModel
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.auth.AuthUseCase
import com.chefshub.domain.usecase.tutorial.TutorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(

    private val tutorialUseCase: TutorialUseCase
) : BaseViewModel() {


    private val _cookingStepsFlow = MutableSharedFlow<NetworkState>()
    val cookingStepsFlow get() = _cookingStepsFlow.asSharedFlow()



    fun getCookingSteps(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_cookingStepsFlow, tutorialUseCase.getCookingSteps(id!!))
        }
    }


    private val _ingredientsFlow1 = MutableSharedFlow<NetworkState>()
    val ingredientsFlow1 get() = _ingredientsFlow1.asSharedFlow()



    fun getIngredients1(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_ingredientsFlow1, tutorialUseCase.getIngredients(id!!))
        }
    }




}