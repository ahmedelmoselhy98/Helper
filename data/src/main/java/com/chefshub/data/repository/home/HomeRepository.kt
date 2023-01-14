package com.chefshub.data.repository.home

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.search.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeRepository {

    fun search(query: String): Flow<Response<EndPointModel<SearchResponse, Any>>>

//    fun search(query: String): Flow<Response<EndPointModel<Any, Any>>>
}