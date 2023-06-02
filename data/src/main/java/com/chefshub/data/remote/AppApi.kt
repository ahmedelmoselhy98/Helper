package com.chefshub.data.remote

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.search.MealResponse
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.entity.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {

    @GET("search")
    suspend fun searchFromKeyWord(@Query("q") query: String): Response<EndPointModel<SearchResponse, Any>>

    @GET("chefs/trending/list")
    suspend fun mostViewByChefs(): Response<EndPointModel<ArrayList<MostViewResponse>, Any>>

    @GET("tutorials")
    suspend fun mealList(@Query("meal_type") meal: String): Response<EndPointModel<ArrayList<MealResponse>, Any>>


}