package com.example.di

import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.repository.auth.AuthRepository
import com.chefshub.domain.usecase.auth.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun providesMainUseCase(authRepository: AuthRepository,preferencesGateway: PreferencesGateway): AuthUseCase {
        return AuthUseCase(authRepository,preferencesGateway)
    }



}

