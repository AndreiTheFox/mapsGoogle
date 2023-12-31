package ru.netology.nmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.repository.MarkerRepository
import javax.inject.Inject

@HiltViewModel
class MarkerViewModel @Inject constructor(private val repository: MarkerRepository) : ViewModel() {

    fun getMarkers(): Flow<List<MyMarker>> = repository.observeMarkers()
    fun removeById(id: Int) = viewModelScope.launch {
        try {
            repository.removeById(id)
        } catch (e: Exception) {
            println("Ошибка удаления")
        }
    }

    fun addMarker(marker: MyMarker) = viewModelScope.launch {
        try {
            repository.addMarker(marker)
        } catch (e: Exception) {
            println("Ошибка добавления")
        }
    }
}