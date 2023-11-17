package ru.netology.nmedia.ui.repository

import ru.netology.nmedia.ui.dto.MyMarker

interface MarkerRepository {
    suspend fun getMarkers(): List<MyMarker>
    suspend fun removeById(id: Int)
    fun updateMarkerById(id: Int, content: String)
    suspend fun addMarker(marker: MyMarker)
}