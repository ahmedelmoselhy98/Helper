package com.chefshub.app.presentation.main_video.comments

import androidx.lifecycle.viewModelScope
import com.chefshub.base.BaseViewModel
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.comments.CommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val commentsUseCase: CommentsUseCase) :
    BaseViewModel() {

    private val _commentsListFlow = MutableSharedFlow<NetworkState>()
    val commentsListFlow get() = _commentsListFlow.asSharedFlow()

    private val _addCommentFlow = MutableSharedFlow<NetworkState>()
    val addCommentFlow get() = _addCommentFlow.asSharedFlow()

    fun getComment(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_commentsListFlow, commentsUseCase.getComments(id))
        }
    }

    fun addComment(id: Int, string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(_addCommentFlow, commentsUseCase.addComment(id, string))
        }
    }
}