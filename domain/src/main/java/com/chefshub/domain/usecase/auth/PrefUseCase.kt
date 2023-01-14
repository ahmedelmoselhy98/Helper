package com.chefshub.domain.usecase.auth

import com.chefshub.data.entity.food_system.FoodSystemModel
import com.chefshub.data.repository.auth.AuthRepository
import com.chefshub.domain.usecase.transformResponseData
import javax.inject.Inject

class PrefUseCase @Inject constructor(private val authRepository: AuthRepository) {


    suspend fun getFoodSystem() =
        authRepository.getFoodSystem()
            .transformResponseData<ArrayList<FoodSystemModel>, Any, ArrayList<FoodSystemModel>> { emit(it) }

    suspend fun getRegionalCuisines() =
        authRepository.getRegionalCuisines()
            .transformResponseData<ArrayList<FoodSystemModel>, Any, ArrayList<FoodSystemModel>> { emit(it) }

}