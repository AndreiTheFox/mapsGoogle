package ru.netology.nmedia.ui.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.ui.dto.MyMarker

interface MarkerRepository {
    fun observeMarkers(): Flow<List<MyMarker>>
    suspend fun removeById(id: Int)
    fun updateMarkerById(id: Int, content: String)
    suspend fun addMarker(marker: MyMarker)
}