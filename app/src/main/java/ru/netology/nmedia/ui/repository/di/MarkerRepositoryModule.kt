package ru.netology.nmedia.ui.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRepositoryRoomImp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MarkerRepositoryModule {
    @Singleton
    @Binds
    fun bindsMarkerRepository(impl: MarkerRepositoryRoomImp): MarkerRepository
}