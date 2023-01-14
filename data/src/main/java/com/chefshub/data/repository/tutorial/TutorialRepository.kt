package com.chefshub.data.repository.tutorial

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.tutorial.TutorialModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TutorialRepository {
    suspend fun getTutorials(page: Int): Response<EndPointModel<ArrayList<TutorialModel>, Any>>
    suspend fun getTutorials(page: Int,userId:Int): Response<EndPointModel<ArrayList<TutorialModel>, Any>>
}