package com.chefshub.data.remote

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.comments.CommentsModel
import retrofit2.Response
import retrofit2.http.*

interface CommentsApi {

    @GET("videos/{id}/comments")
    suspend fun getComments(@Path("id") id: Int): Response<EndPointModel<ArrayList<CommentsModel>, Any>>

    @FormUrlEncoded
    @POST("videos/{id}/comments")
    suspend fun addComment(
        @Path("id") id: Int,
        @Field("body") body: String
    ): Response<EndPointModel<CommentsModel, Any>>


}