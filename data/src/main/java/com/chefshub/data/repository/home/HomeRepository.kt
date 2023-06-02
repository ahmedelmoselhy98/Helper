package com.chefshub.data.repository.home

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.search.MealResponse
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.entity.search.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeRepository {

    fun search(query: String): Flow<Response<EndPointModel<SearchResponse, Any>>>

    fun mostViewChefs(): Flow<Response<EndPointModel<ArrayList<MostViewResponse>, Any>>>

    fun mealList(meal_type:String): Flow<Response<EndPointModel<ArrayList<MealResponse>, Any>>>
}