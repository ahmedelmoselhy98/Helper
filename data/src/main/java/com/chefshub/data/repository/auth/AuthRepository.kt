package com.chefshub.data.repository.auth

import android.net.Uri
import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.food_system.FoodSystemModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.AuthMeta
import com.chefshub.data.entity.user.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface AuthRepository {

    suspend fun loginWithSocial(
        provider_id: String,
        provider_name: String,
        name: String,
        avatar_path: String,
        device_token: String,
        device_id: String,
    ): Flow<Response<EndPointModel<UserModel, AuthMeta>>>

    suspend fun logout(
        device_token: String,
        device_id: String,
    ): Flow<Response<EndPointModel<Any, Any>>>

    suspend fun loginWithEmail(
        email: String,
        password: String,
        device_token: String,
        device_id: String,
    ): Flow<Response<EndPointModel<UserModel, AuthMeta>>>

    suspend fun signup(
        email: String,
        name: String,
        password: String,
        device_token: String,
        device_id: String,
    ): Flow<Response<EndPointModel<UserModel, AuthMeta>>>

    suspend fun updateProfile(
        email: String,
        name: String,
        password: String,
        avatar_path: String
    ): Flow<Response<EndPointModel<UserModel, AuthMeta>>>

    suspend fun getProfile(): Flow<Response<EndPointModel<UserModel, Any>>>

    suspend fun getUser(id: Int): Flow<Response<EndPointModel<UserModel, Any>>>

    suspend fun toggleFollow(id: Int): Flow<Response<EndPointModel<Any, Any>>>

    suspend fun singleVideo(id: Int): Flow<Response<EndPointModel<TutorialVideos, Any>>>

    suspend fun addFavorite(id: Int): Flow<Response<EndPointModel<Any, Any>>>

    suspend fun addSavedVideo(id: Int): Flow<Response<EndPointModel<Any, Any>>>

    suspend fun getFoodSystem(): Flow<Response<EndPointModel<ArrayList<FoodSystemModel>, Any>>>

    suspend fun getRegionalCuisines(): Flow<Response<EndPointModel<ArrayList<FoodSystemModel>, Any>>>


}