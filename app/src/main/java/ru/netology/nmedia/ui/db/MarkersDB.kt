package ru.netology.nmedia.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.entity.MarkerEntity

@Database(entities = [MarkerEntity::class], version = 1)
abstract class MarkersDB: RoomDatabase() {
    abstract fun markerDao(): MarkerDao
    companion object {
        @Volatile
        private var instance: MarkersDB? = null
        fun getInstance(context: Context): MarkersDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MarkersDB::class.java, "MarkersDB.db")
                .build()
    }
}