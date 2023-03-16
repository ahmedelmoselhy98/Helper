package com.chefshub.domain.usecase.tutorial

import com.chefshub.data.entity.ingredients.IngredientsModel
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.user.UserModel
import com.chefshub.data.repository.tutorial.TutorialRepository
import com.chefshub.domain.usecase.transformResponseData

import javax.inject.Inject


class TutorialUseCase @Inject constructor(private val tutorialRepository: TutorialRepository) {

    fun getPagingSource(id: Int? = null) =
        TutorialPagingSource(tutorialRepository).apply { userId = id }


    suspend fun getTutorialsVideosChef(tutorialId: Int? = null) =
        tutorialRepository.getTutorialsVideosChef(tutorialId!!).transformResponseData<ArrayList<TutorialModel>, Any, ArrayList<TutorialModel>> { emit(it) }

    suspend fun getCookingSteps(tutorialId: Int? = null) =
        tutorialRepository.getCookingSteps(tutorialId!!).transformResponseData<ArrayList<TutorialModel>, Any, ArrayList<TutorialModel>> { emit(it) }


    suspend fun getIngredients(tutorialId: Int? = null) =
        tutorialRepository.getTutorialsIngredients(tutorialId!!).transformResponseData<ArrayList<IngredientsModel>, Any, ArrayList<IngredientsModel>> { emit(it) }







}