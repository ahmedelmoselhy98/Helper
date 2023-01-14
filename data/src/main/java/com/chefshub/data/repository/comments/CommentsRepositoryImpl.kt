package com.chefshub.data.repository.comments

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.comments.CommentsModel
import com.chefshub.data.remote.CommentsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(private val commentsApi: CommentsApi) :
    CommentsRepository {
    override suspend fun getComments(id: Int) = flow { emit(commentsApi.getComments(id)) }

    override suspend fun addComment(
        id: Int,
        string: String
    ) = flow { emit(commentsApi.addComment(id, string)) }
}