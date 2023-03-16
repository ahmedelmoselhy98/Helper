package com.chefshub.data.remote

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.ingredients.IngredientsModel
import com.chefshub.data.entity.tutorial.TutorialModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TutorialApi {
//    @GET("tutorials/6/videos")
//    suspend fun getTutorials(@Query("page") page: Int):
//            Response<EndPointModel<ArrayList<TutorialModel>, Any>>


    @GET("videos/random")
    suspend fun getTutorials(@Query("page") page: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    @GET("tutorials")
    suspend fun getTutorials(@Query("page") page: Int,
                             @Query("by") userId: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    @GET("tutorials/{id}}/videos")
    suspend fun getCookingSteps(
                             @Path("id") id: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    @GET("videos/random")
    suspend fun getTutorialsVideosChef(
                            @Query("by") id: Int):
            Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    @GET("videos/{id}/ingredients")
    suspend fun getTutorialsIngredients1(
                             @Path("id") id: Int):
            Response<EndPointModel<ArrayList<IngredientsModel>, Any>>


}