package com.chefshub.app.presentation.main_video.profile

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tutorialUseCase: TutorialUseCase
) : BaseViewModel() {



    private val _VideosChefFlow = MutableSharedFlow<NetworkState>()
    val VideosChefFlow get() = _VideosChefFlow.asSharedFlow()



    fun getTutorialsVideosChef(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_VideosChefFlow, tutorialUseCase.getTutorialsVideosChef(id!!))
        }
    }



    private val _userFlow = MutableSharedFlow<NetworkState>()
    val userFlow get() = _userFlow.asSharedFlow()

    private val _toggleFlow = MutableSharedFlow<NetworkState>()
    val toggleFlow get() = _toggleFlow.asSharedFlow()


    fun videos(userid: Int) = Pager(
        config = PagingConfig(15, 1),
        pagingSourceFactory = { tutorialUseCase.getPagingSource(userid) }
    ).flow.cachedIn(viewModelScope)

    fun getUserProfile(isMe: Boolean, userID: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userID != null)
                executeSharedFlow(_userFlow, authUseCase.getUser(userID))
            else if (isMe)
                executeSharedFlow(_userFlow, authUseCase.getMyProfile())
        }
    }

    fun toggleFollow(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_toggleFlow, authUseCase.toggleFollow(id))
        }
    }

}