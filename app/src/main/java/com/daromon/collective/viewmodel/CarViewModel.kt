package com.daromon.collective.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.usecase.AddCarUseCase
import com.daromon.collective.domain.usecase.DeleteCarUseCase
import com.daromon.collective.domain.usecase.GetCarsUseCase
import com.daromon.collective.domain.usecase.UpdateCarUseCase
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.state.CarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val getCars: GetCarsUseCase,
    private val addCar: AddCarUseCase,
    private val deleteCar: DeleteCarUseCase,
    private val updateCar: UpdateCarUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CarUiState>(CarUiState.Loading)
    val state: StateFlow<CarUiState> = _state.asStateFlow()

    init {
        onEvent(CarEvent.Load)
    }

    fun onEvent(event: CarEvent) {
        when (event) {
            is CarEvent.Load -> loadCars()
            is CarEvent.Add -> viewModelScope.launch {
                try {
                    if (!isCarValid(event.car)) return@launch
                    addCar(event.car)
                    loadCars()
                } catch (e: Exception) {
                    _state.value = CarUiState.Error("Nie udało się dodać auta")
                }
            }
            is CarEvent.Update -> viewModelScope.launch {
                if (!isCarValid(event.car)) return@launch
                updateCar(event.car)
                loadCars()
            }
            is CarEvent.Delete -> viewModelScope.launch {
                deleteCar(event.car)
                loadCars()
            }
        }
    }

    private fun loadCars() {
        viewModelScope.launch {
            getCars().onStart { _state.value = CarUiState.Loading }
                .catch { _state.value = CarUiState.Error("Failed to load") }
                .collect { cars -> _state.value = CarUiState.Success(cars) }
        }
    }

    fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileName = "car_photo_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream: OutputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    private fun isCarValid(car: Car): Boolean {
        return !car.model.isNullOrBlank() && !car.brand.isNullOrBlank()
    }
}