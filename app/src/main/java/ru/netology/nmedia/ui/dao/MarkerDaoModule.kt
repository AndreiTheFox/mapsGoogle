package ru.netology.nmedia.ui.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.ui.db.MarkersDB

@InstallIn(SingletonComponent::class)
@Module
class MarkerDaoModule {
    @Provides
    fun provideMarkerDao (db:MarkersDB):MarkerDao = db.markerDao()
}