package com.chefshub.domain.usecase.comments

import com.chefshub.data.entity.comments.CommentsModel
import com.chefshub.data.repository.comments.CommentsRepository
import com.chefshub.domain.usecase.transformResponseData
import javax.inject.Inject

class CommentsUseCase @Inject constructor(private val commentsRepository: CommentsRepository) {

    suspend fun getComments(id: Int) =
        commentsRepository.getComments(id)
            .transformResponseData<ArrayList<CommentsModel>, Any, ArrayList<CommentsModel>> {emit(it) }

    suspend fun addComment(id: Int, string: String) = commentsRepository.addComment(id, string)
        .transformResponseData<CommentsModel, Any, CommentsModel> {emit(it) }
}