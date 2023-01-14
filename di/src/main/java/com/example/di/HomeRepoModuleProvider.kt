package com.example.di

import com.chefshub.data.repository.home.HomeRepository
import com.chefshub.data.repository.home.HomeRepositoryImpl
import com.chefshub.data.repository.tutorial.TutorialRepository
import com.chefshub.data.repository.tutorial.TutorialRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepoModuleProvider {
    @Binds
    abstract fun provideAuthModule(repository: HomeRepositoryImpl): HomeRepository
}