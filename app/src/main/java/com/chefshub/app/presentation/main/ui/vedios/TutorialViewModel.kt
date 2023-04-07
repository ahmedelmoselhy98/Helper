package com.chefshub.app.presentation.main.ui.vedios

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.chefshub.base.BaseViewModel
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.auth.AuthUseCase
import com.chefshub.domain.usecase.tutorial.TutorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val tutorialUseCase: TutorialUseCase,
    private val useCase: AuthUseCase
) : BaseViewModel() {

    private val _toggleFollow = MutableSharedFlow<NetworkState>()
    val toggleFollow get() = _toggleFollow
    fun toggleFollow(it: Int) {
        viewModelScope.launch {
            executeSharedFlow(_toggleFollow, useCase.toggleFollow(it))
        }
    }

    private val _singleVideo = MutableSharedFlow<NetworkState>()
    val singleVideo get() = _singleVideo
    fun singleVideo(it: Int) {
        viewModelScope.launch {
            executeSharedFlow(_singleVideo, useCase.singleVideo(it))
        }
    }

 private val _addFavorite = MutableSharedFlow<NetworkState>()
    val addFavorite get() = _addFavorite
    fun addFavorite(it: Int) {
        viewModelScope.launch {
            executeSharedFlow(_addFavorite, useCase.addFavorite(it))
        }
    }

 private val _addSavedVideo = MutableSharedFlow<NetworkState>()
    val addSavedVideo get() = _addSavedVideo
    fun addSavedVideo(it: Int) {
        viewModelScope.launch {
            executeSharedFlow(_addSavedVideo, useCase.addSavedVideo(it))
        }
    }


    val list = Pager(
        config = PagingConfig(15, enablePlaceholders = false , prefetchDistance = 1),
        pagingSourceFactory = { tutorialUseCase.getPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun getUserTutorialsPager(uId: Int) = Pager(
        config = PagingConfig(15, 1),
        pagingSourceFactory = { tutorialUseCase.getPagingSource().apply { userId = uId } }
    ).flow.cachedIn(viewModelScope)





//    private val _videoFlow = MutableSharedFlow<NetworkState>()
//    val videoFlow get() = _videoFlow.asSharedFlow()
//
//    fun getTutorials(page: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            executeSharedFlow(_videoFlow, tutorialUseCase.getTutorial(page))
//        }
//    }

}