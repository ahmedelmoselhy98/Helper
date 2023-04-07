package com.chefshub.data.repository.tutorial

import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.remote.TutorialApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class TutorialRepositoryImpl @Inject constructor(private val tutorialApi: TutorialApi) :
    TutorialRepository {

    override suspend fun getTutorials(page: Int) = tutorialApi.getTutorials(page)


    override suspend fun getTutorials(page: Int,userId:Int) = flow {emit( tutorialApi.getTutorials(page,userId))}

    override suspend fun getTutorialsVideosChef(tutorialId:Int) = flow { emit(tutorialApi.getTutorialsVideosChef(tutorialId)) }

    override suspend fun getCookingSteps(tutorialId:Int) = flow { emit(tutorialApi.getCookingSteps(tutorialId)) }


    override suspend fun getTutorialsIngredients(tutorialId:Int) = flow { emit(tutorialApi.getTutorialsIngredients1(tutorialId)) }



}