package com.chefshub.data.repository.home

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.remote.AppApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val appApi: AppApi) : HomeRepository {

    override fun search(query: String) = flow { emit(appApi.searchFromKeyWord(query)) }
    override fun mostViewChefs()= flow { emit(appApi.mostViewByChefs()) }

    override fun mealList(meal_type:String)= flow { emit(appApi.mealList(meal_type)) }
}