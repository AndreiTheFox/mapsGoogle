package ru.netology.nmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.repository.MarkerRepository
import javax.inject.Inject

@HiltViewModel
class MarkerViewModel @Inject constructor(private val repository: MarkerRepository) : ViewModel() {

    suspend fun getMarkers(): List<MyMarker> {
        val coroutine = viewModelScope.async(Dispatchers.Default) {
            repository.getMarkers()
        }
        return coroutine.await()
    }

    fun removeById(id: Int) = viewModelScope.launch(Dispatchers.Default) {
        try {
            repository.removeById(id)
        } catch (e: Exception) {
            println("Ошибка удаления")
        }
    }

    fun addMarker(marker: MyMarker) = viewModelScope.launch(Dispatchers.Default) {
        try {
            repository.addMarker(marker)
        } catch (e: Exception) {
            println("Ошибка добавления")
        }
    }

    fun onPosition(marker: MyMarker) {

    }
}