package com.chefshub.domain.usecase.home

import com.chefshub.data.entity.bookmarked.VideoModel
import com.chefshub.data.entity.search.MealResponse
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.entity.search.SearchResponse
import com.chefshub.data.repository.home.HomeRepository
import com.chefshub.domain.usecase.transformResponseData
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {


    suspend fun searchForKey(key: String) =
        homeRepository.search(key).transformResponseData<SearchResponse, Any, SearchResponse> { emit(it) }

    suspend fun mostViewByChef() =
        homeRepository.mostViewChefs().transformResponseData<ArrayList<MostViewResponse>, Any, ArrayList<MostViewResponse>> { emit(it) }

    suspend fun mostFamousVideo() =
        homeRepository.mostFamousVideo().transformResponseData<VideoModel, Any, VideoModel> { emit(it) }


    suspend fun mealList(meal_type:String) =
        homeRepository.mealList(meal_type).transformResponseData<ArrayList<MealResponse>, Any, ArrayList<MealResponse>> { emit(it) }


}