package com.example.di

import com.chefshub.data.repository.comments.CommentsRepository
import com.chefshub.data.repository.comments.CommentsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentsModuleProvider {
    @Binds
    abstract fun provideModule(repository: CommentsRepositoryImpl): CommentsRepository
}