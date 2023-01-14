package com.chefshub.data.repository.comments

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.comments.CommentsModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CommentsRepository {

    suspend fun getComments(id: Int): Flow<Response<EndPointModel<ArrayList<CommentsModel>, Any>>>
    suspend fun addComment(
        id: Int,
        string: String
    ): Flow<Response<EndPointModel<CommentsModel, Any>>>
}