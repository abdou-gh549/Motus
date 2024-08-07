package com.compose.myapplication.di

import com.compose.myapplication.domain.MotusDataBaseImpl
import com.compose.myapplication.domain.MotusRepositoryImpl
import com.compose.myapplication.domain.interfaces.MotusDataBase
import com.compose.myapplication.domain.interfaces.MotusRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideMotusRepository(impl : MotusRepositoryImpl): MotusRepository

    @Singleton
    @Binds
    fun provideDb(impl: MotusDataBaseImpl): MotusDataBase
}