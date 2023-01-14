package com.chefshub.data.repository.home

import com.chefshub.data.remote.AppApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val appApi: AppApi) : HomeRepository {

    override fun search(query: String) = flow { emit(appApi.searchFromKeyWord(query)) }
}