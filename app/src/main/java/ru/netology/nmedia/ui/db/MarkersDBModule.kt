package ru.netology.nmedia.ui.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
class MarkersDBModule {
    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context
    ) : MarkersDB =  Room.databaseBuilder(context, MarkersDB::class.java, "markers.db")
        .fallbackToDestructiveMigration()
        .build()
}
