package com.chefshub.domain.usecase.auth

import PrefKeys
import android.util.Log
import android.util.Patterns
import com.chefshub.base.Validation
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.AuthMeta
import com.chefshub.data.entity.user.UserModel
import com.chefshub.data.repository.auth.AuthRepository
import com.chefshub.domain.usecase.transformResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesGateway: PreferencesGateway
) {

    suspend fun loginWithSocial(
        provider_id: String,
        provider_name: String,
        name: String,
        avatar_path: String,
        device_token: String,
        device_id: String,
    ) = authRepository.loginWithSocial(
        provider_id,
        provider_name,
        name,
        avatar_path,
        device_token,
        device_id
    ).transformResponseData<UserModel, AuthMeta, UserModel>(true) { emit(it) }.onEach {
        preferencesGateway.save(PrefKeys.USER, it)
        preferencesGateway.save(PrefKeys.IS_USER_LOGGED, true)
        preferencesGateway.save(PrefKeys.TOKEN, it.token!!)
    }


    suspend fun logout(
        device_token: String,
        device_id: String,
    ) = authRepository.logout(device_token, device_id)
        .transformResponseData<Any, Any, Any> { emit(it) }


    suspend fun loginWithEmail(
        email: String,
        password: String,
        device_token: String,
        device_id: String,
    ): Flow<UserModel> {
        return authRepository.loginWithEmail(email, password, device_token, device_id)
            .transformResponseData<UserModel, AuthMeta, UserModel>(true) { emit(it) }.onEach {
                preferencesGateway.save(PrefKeys.USER, it)
                preferencesGateway.save(PrefKeys.IS_USER_LOGGED, true)
                preferencesGateway.save(PrefKeys.TOKEN, it.token!!)
            }
    }

    suspend fun signup(
        email: String,
        name: String,
        password: String,
        device_token: String,
        device_id: String,
    ): Flow<UserModel> {
        return authRepository.signup(email,name, password, device_token, device_id)
            .transformResponseData<UserModel, AuthMeta, UserModel>(true) { emit(it) }.onEach {
                preferencesGateway.save(PrefKeys.USER, it)
                preferencesGateway.save(PrefKeys.IS_USER_LOGGED, true)
                preferencesGateway.save(PrefKeys.TOKEN, it.token!!)
            }
    }


    fun isValidAuthData(email: String?, password: String?) = when {
        (isValidEmail(email) == Validation.IS_VALID).not() -> false
        (isValidPassword(password) == Validation.IS_VALID).not() -> false
        else -> true
    }
    fun isValidRegisterAuthData(email: String?,name: String?, password: String?,confirmPassword: String?) = when {
        (isValidEmail(email) == Validation.IS_VALID).not() -> false
        (isValidPassword(password) == Validation.IS_VALID).not() -> false
        (isValidText(name) == Validation.IS_VALID).not() -> false
        (isValidConfirmPassword(password,confirmPassword) == Validation.IS_VALID).not() -> false
        else -> true
    }

    fun isValidEmail(email: String?): Validation {
        if (email.isNullOrEmpty())
            return Validation.EMPTY
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Validation.IS_VALID
        return Validation.NOT_VALID
    }
    fun isValidText(text: String?): Validation {
        if (text.isNullOrEmpty()) {
            return Validation.EMPTY
        }else{
            return Validation.IS_VALID
        }
    }

    fun isValidPassword(password: String?): Validation {
        if (password.isNullOrEmpty())
            return Validation.EMPTY
        if (password.length > 6)
            return Validation.IS_VALID
        return Validation.NOT_VALID
    }
    fun isValidConfirmPassword(password: String? , confirmPassword:String?): Validation {
        if (password.isNullOrEmpty()||confirmPassword.isNullOrEmpty())
            return Validation.EMPTY
        if (confirmPassword == password)
            return Validation.IS_VALID
        return Validation.NOT_VALID
    }


    suspend fun getUser(int: Int) =
        authRepository.getUser(int).transformResponseData<UserModel, Any, UserModel> { emit(it) }


    suspend fun toggleFollow(int: Int) =
        authRepository.toggleFollow(int).transformResponseData<Any, Any, Any> { emit(it) }

    suspend fun singleVideo(int: Int) =
        authRepository.singleVideo(int).transformResponseData<TutorialVideos, Any, TutorialVideos> { emit(it) }

    suspend fun addFavorite(int: Int) =
        authRepository.addFavorite(int).transformResponseData<Any, Any, Any> { emit(it) }

    suspend fun addSavedVideo(int: Int) =
        authRepository.addSavedVideo(int).transformResponseData<Any, Any, Any> { emit(it) }


    suspend fun getMyProfile() =
        authRepository.getProfile().transformResponseData<UserModel, Any, UserModel> { emit(it) }
}
