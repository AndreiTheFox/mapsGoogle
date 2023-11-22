package ru.netology.nmedia.ui.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.ui.entity.MarkerEntity

@Dao
interface MarkerDao {
    @Query("SELECT * FROM MarkerEntity ORDER BY id DESC")
    fun observeAll(): Flow<List<MarkerEntity>>
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marker: MarkerEntity)
    @Query("UPDATE MarkerEntity SET tag = :content WHERE id = :id")
    fun updateMarkerById(id: Int, content: String)
    @Query("DELETE FROM MarkerEntity WHERE id = :id")
    suspend fun removeById(id: Int)
}