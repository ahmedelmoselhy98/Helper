package com.chefshub.data.repository.auth

import android.util.Log
import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.user.AuthMeta
import com.chefshub.data.entity.user.UserModel
import com.chefshub.data.remote.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val userApi: AuthApi) : AuthRepository {

    override suspend fun loginWithSocial(
        provider_id: String,
        provider_name: String,
        name: String,
        avatar_path: String,
        device_token: String,
        device_id: String
    ) = flow {
        emit(
            userApi.loginWithSocial(
                provider_id,
                provider_name,
                name,
                avatar_path,
                device_token,
                device_id
            )
        )
    }

    override suspend fun logout(
        device_token: String,
        device_id: String
    ): Flow<Response<EndPointModel<Any, Any>>> {
        return flow { emit(userApi.logout(device_token, device_id)) }
    }


    override suspend fun loginWithEmail(
        email: String,
        password: String,
        device_token: String,
        device_id: String
    ): Flow<Response<EndPointModel<UserModel, AuthMeta>>> {
        Log.e("TAG", "loginWithEmail: repo")
        return flow { emit(userApi.loginWithEmail(email, password, device_token, device_id)) }
    }

    override suspend fun getProfile() = flow { emit(userApi.getProfile()) }

    override suspend fun getUser(id: Int) = flow { emit(userApi.getUser(id)) }

    override suspend fun toggleFollow(id: Int) = flow { emit(userApi.toggleFollow(id)) }

    override suspend fun singleVideo(id: Int)= flow { emit(userApi.singleVideo(id)) }

    override suspend fun addFavorite(id: Int) = flow { emit(userApi.addFavorite(id)) }

    override suspend fun getFoodSystem() = flow { emit(userApi.getFoodSystem()) }

    override suspend fun getRegionalCuisines() = flow { emit(userApi.getRegionalCuisines()) }


}