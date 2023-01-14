package com.chefshub.data.remote

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {

    @GET("search")
    suspend fun searchFromKeyWord(@Query("q") query: String): Response<EndPointModel<SearchResponse, Any>>


}