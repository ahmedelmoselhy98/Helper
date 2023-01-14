package com.chefshub.domain.usecase.home

import com.chefshub.data.entity.search.SearchResponse
import com.chefshub.data.repository.home.HomeRepository
import com.chefshub.domain.usecase.transformResponseData
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {


    suspend fun searchForKey(key: String) =
        homeRepository.search(key).transformResponseData<SearchResponse, Any, SearchResponse> { emit(it) }
}