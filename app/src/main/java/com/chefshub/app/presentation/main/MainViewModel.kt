package com.chefshub.app.presentation.main

import com.chefshub.base.BaseViewModel
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.entity.user.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesGateway: PreferencesGateway) :
    BaseViewModel() {


    fun getUserProfile() = getUser()?.avatarPath

    fun getUser() =
        preferencesGateway.load(PrefKeys.USER, UserModel())

}