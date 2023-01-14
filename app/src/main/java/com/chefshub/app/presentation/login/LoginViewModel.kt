package com.chefshub.app.presentation.login

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
        avatar_path: String,
        device_id: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            executeSharedFlow(
                _userFlow,
                authUseCase.loginWithSocial(
                    provider_id,
                    provider_name,
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


    private val _isValidEmail =
        MutableStateFlow<Validation>(Validation.IDLE)
    private val _isValidPassword =
        MutableStateFlow<Validation>(Validation.IDLE)
    val isValidEmail get() = _isValidEmail.asSharedFlow()
    val isValidPassword get() = _isValidPassword.asSharedFlow()
    fun isValidEmail(email: String?) {
        _isValidEmail.value = authUseCase.isValidEmail(email)
    }
    fun isValidPassword(password: String?) {
        _isValidPassword.value = authUseCase.isValidPassword(password)
    }

}