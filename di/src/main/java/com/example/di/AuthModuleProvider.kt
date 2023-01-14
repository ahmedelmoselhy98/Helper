package com.example.di

import com.chefshub.data.repository.auth.AuthRepository
import com.chefshub.data.repository.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModuleProvider {
    @Binds
    abstract fun provideAuthModule (authRepository: AuthRepositoryImpl) : AuthRepository
}