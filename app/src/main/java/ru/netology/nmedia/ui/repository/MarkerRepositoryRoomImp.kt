package ru.netology.nmedia.ui.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.entity.MarkerEntity
import javax.inject.Inject


class MarkerRepositoryRoomImp @Inject constructor(
    private val dao: MarkerDao,
) : MarkerRepository {

    override fun observeMarkers(): Flow<List<MyMarker>> = dao.observeAll().map {
        it.map(MarkerEntity::toDto)
    }

    override suspend fun removeById(id: Int) {
        println("удален маркер с ID :$id")
        dao.removeById(id)
    }

    override fun updateMarkerById(id: Int, content: String) {
        dao.updateMarkerById(id, content)
    }

    override suspend fun addMarker(marker: MyMarker) {
        println("добавлен маркер с ID :${marker.id}")
        dao.insert(MarkerEntity.fromDto(marker))
    }
}