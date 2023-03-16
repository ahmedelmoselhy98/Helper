package com.chefshub.data.repository.tutorial

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.ingredients.IngredientsModel
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.user.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TutorialRepository {
    suspend fun getTutorials(page: Int): Response<EndPointModel<ArrayList<TutorialModel>, Any>>
    suspend fun getTutorials(page: Int,userId:Int): Response<EndPointModel<ArrayList<TutorialModel>, Any>>

    suspend fun getTutorialsVideosChef(tutorialId:Int):Flow<Response<EndPointModel<ArrayList<TutorialModel>, Any>>>

    suspend fun getCookingSteps(tutorialId:Int):Flow<Response<EndPointModel<ArrayList<TutorialModel>, Any>>>

    suspend fun getTutorialsIngredients(tutorialId:Int):Flow<Response<EndPointModel<ArrayList<IngredientsModel>, Any>>>

}