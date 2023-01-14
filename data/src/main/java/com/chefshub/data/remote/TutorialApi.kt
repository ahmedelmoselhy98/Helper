package com.chefshub.data.remote

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.tutorial.TutorialModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TutorialApi {
    @GET("tutorials/6/videos")
    suspend fun getTutorials(@Query("page") page: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    @GET("tutorials?by={id}")
    suspend fun getTutorials(@Query("page") page: Int, userId: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>
}