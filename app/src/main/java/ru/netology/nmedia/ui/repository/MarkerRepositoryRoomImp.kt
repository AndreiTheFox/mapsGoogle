package ru.netology.nmedia.ui.repository

import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.entity.MarkerEntity
import javax.inject.Inject


class MarkerRepositoryRoomImp @Inject constructor(
    private val dao: MarkerDao,
) : MarkerRepository {

    override suspend fun getMarkers(): List<MyMarker> {
        return dao.getAll().map { it.toDto() }
    }

    override suspend fun removeById(id: Int) {
        println("удален маркер с ID :$id")
        dao.removeByHash(id)
    }

    override fun updateMarkerById(id: Int, content: String) {
        dao.updateMarkerById(id, content)
    }

    override suspend fun addMarker(marker: MyMarker) {
        println("добавлен маркер с ID :${marker.id}")
        dao.insert(MarkerEntity.fromDto(marker))
    }
}