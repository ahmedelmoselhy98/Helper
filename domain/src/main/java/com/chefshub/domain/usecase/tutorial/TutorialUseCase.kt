package com.chefshub.domain.usecase.tutorial

import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.repository.tutorial.TutorialRepository
import com.chefshub.domain.usecase.transformResponseData
import javax.inject.Inject

class TutorialUseCase @Inject constructor(private val tutorialRepository: TutorialRepository) {

    fun getPagingSource(id: Int? = null) =
        TutorialPagingSource(tutorialRepository).apply { userId = id }

}