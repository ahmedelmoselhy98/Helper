package com.chefshub.app.presentation.login

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.chefshub.base.BaseViewModel
import com.chefshub.base.Validation
import com.chefshub.data.entity.NetworkState
import com.chefshub.domain.usecase.auth.AuthUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    private var firebaseToken = ""

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful)
                firebaseToken = it.result
        }
    }

    private val _userFlow = MutableSharedFlow<NetworkState>()
    val userFlow get() = _userFlow.asSharedFlow()


    fun loginWithSocial(
        provider_id: String,
        provider_name: String,
        name: String,
        avatar_path: String,
        device_id: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _userFlow,
                authUseCase.loginWithSocial(
                    provider_id,
                    provider_name,
                    name,
                    avatar_path,
                    firebaseToken,
                    device_id
                )
            )
        }
    }


    fun loginWithEmail(
        email: String,
        password: String,
        device_id: String,
    ) {
        if (!authUseCase.isValidAuthData(email, password)) return
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _userFlow,
                authUseCase.loginWithEmail(email, password, firebaseToken, device_id)
            )
        }
    }

    fun signup(
        email: String,
        name: String,
        password: String,
        confirmPassword: String?,
        device_id: String,
    ) {
        if (!authUseCase.isValidRegisterAuthData(email, name,password,confirmPassword)) return
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _userFlow,
                authUseCase.signup(email,name, password, firebaseToken, device_id)
            )
        }
    }

    private val _updateProfileFlow = MutableSharedFlow<NetworkState>()
    val updateProfileFlow get() = _updateProfileFlow.asSharedFlow()

    fun updateProfile(
        email: String,
        name: String,
        password: String,
        confirmPassword: String,
        avatar_path: Bitmap?=null
    ) {
        if (!authUseCase.isValidRegisterAuthData(email, name,password,confirmPassword)) return
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _updateProfileFlow,
                authUseCase.updateProfile(email,name, password,avatar_path!! )
            )
        }
    }

    private val _getTutorialFlow = MutableSharedFlow<NetworkState>()
    val getTutorialFlow get() = _getTutorialFlow.asSharedFlow()


    fun getTutorial() {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _getTutorialFlow,
                authUseCase.getTutorial()
            )
        }
    }


    private val _isValidEmail =
        MutableStateFlow<Validation>(Validation.IDLE)
    private val _isValidUserName =
        MutableStateFlow<Validation>(Validation.IDLE)
    private val _isValidPassword =
        MutableStateFlow<Validation>(Validation.IDLE)
    private val _isValidConfirmPassword =
        MutableStateFlow<Validation>(Validation.IDLE)
    val isValidEmail get() = _isValidEmail.asSharedFlow()
    val isValidUserName get() = _isValidUserName.asSharedFlow()
    val isValidPassword get() = _isValidPassword.asSharedFlow()
    val isValidConfirmPassword get() = _isValidConfirmPassword.asSharedFlow()

    fun isValidEmail(email: String?) {
        _isValidEmail.value = authUseCase.isValidEmail(email)
    }
    fun isValidUserName(name: String?) {
        _isValidUserName.value = authUseCase.isValidText(name)
    }
    fun isValidPassword(password: String?) {
        _isValidPassword.value = authUseCase.isValidPassword(password)
    }
    fun isValidConfirmPassword(password: String?, confirmPassword:String?) {
        _isValidConfirmPassword.value = authUseCase.isValidConfirmPassword(password,confirmPassword)
    }

}