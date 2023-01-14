package com.chefshub.data.repository.tutorial

import com.chefshub.data.remote.TutorialApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TutorialRepositoryImpl @Inject constructor(private val tutorialApi: TutorialApi) :
    TutorialRepository {

    override suspend fun getTutorials(page: Int) = tutorialApi.getTutorials(page)


    override suspend fun getTutorials(page: Int,userId:Int) = tutorialApi.getTutorials(page,userId)

}