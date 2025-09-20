package com.daromon.collective.ui.features.car

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.model.FuelType
import com.daromon.collective.domain.usecase.AddCarUseCase
import com.daromon.collective.domain.usecase.DeleteCarUseCase
import com.daromon.collective.domain.usecase.GetCarsUseCase
import com.daromon.collective.domain.usecase.UpdateCarUseCase
import com.daromon.collective.ui.components.CarFormState
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

    private val _formState = MutableStateFlow(CarFormState())
    val formState: StateFlow<CarFormState> = _formState.asStateFlow()

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
        return car.model.isBlank().not() && car.brand.isBlank().not()
    }

    fun setForm(car: Car?) {
        _formState.value = if (car == null) {
            CarFormState()
        } else {
            CarFormState(
                model = car.model,
                brand = car.brand,
                year = car.year?.toString() ?: "",
                vin = car.vin ?: "",
                registrationNumber = car.registrationNumber ?: "",
                mileage = car.mileage?.toString() ?: "",
                fuelType = FuelType.entries.find { fuel -> fuel.displayName == car.fuelType },
                engineCapacity = car.engineCapacity?.toString() ?: "",
                power = car.power?.toString() ?: "",
                color = car.color ?: "",
                notes = car.notes ?: "",
                inspectionDate = car.inspectionDate ?: "",
                insuranceExpiry = car.insuranceExpiry ?: "",
                photoUri = car.photoUri?.toUri()
            )
        }
    }

    fun onFieldChange(field: String, value: String) {
        _formState.value = when (field) {
            "model" -> _formState.value.copy(
                model = value,
                errors = _formState.value.errors - "model"
            )

            "brand" -> _formState.value.copy(
                brand = value,
                errors = _formState.value.errors - "brand"
            )

            "year" -> _formState.value.copy(year = value, errors = _formState.value.errors - "year")
            "vin" -> _formState.value.copy(vin = value)
            "registrationNumber" -> _formState.value.copy(registrationNumber = value)
            "mileage" -> _formState.value.copy(
                mileage = value,
                errors = _formState.value.errors - "mileage"
            )

            "engineCapacity" -> _formState.value.copy(
                engineCapacity = value,
                errors = _formState.value.errors - "engineCapacity"
            )

            "power" -> _formState.value.copy(
                power = value,
                errors = _formState.value.errors - "power"
            )

            "color" -> _formState.value.copy(color = value)
            "notes" -> _formState.value.copy(notes = value)
            "inspectionDate" -> _formState.value.copy(inspectionDate = value)
            "insuranceExpiry" -> _formState.value.copy(insuranceExpiry = value)
            else -> _formState.value
        }
    }

    fun onFuelTypeChange(type: FuelType?) {
        _formState.value = _formState.value.copy(fuelType = type)
    }

    fun onPhotoChange(uri: Uri?) {
        _formState.value = _formState.value.copy(photoUri = uri)
    }

    fun validate(context: Context): Boolean {
        val state = _formState.value
        val newErrors = mutableMapOf<String, String>()
        if (state.brand.isBlank()) newErrors["brand"] =
            context.getString(com.daromon.collective.R.string.required_field)
        if (state.model.isBlank()) newErrors["model"] =
            context.getString(com.daromon.collective.R.string.required_field)
        if (state.year.isNotBlank() && (state.year.length != 4 || state.year.toIntOrNull() == null)) newErrors["year"] =
            context.getString(com.daromon.collective.R.string.year_must_be_4_digits)
        if (state.mileage.isNotBlank() && state.mileage.toIntOrNull() == null) newErrors["mileage"] =
            context.getString(com.daromon.collective.R.string.numbers_only)
        if (state.engineCapacity.isNotBlank() && state.engineCapacity.toDoubleOrNull() == null) newErrors["engineCapacity"] =
            context.getString(com.daromon.collective.R.string.numbers_only)
        if (state.power.isNotBlank() && state.power.toIntOrNull() == null) newErrors["power"] =
            context.getString(com.daromon.collective.R.string.numbers_only)
        _formState.value = state.copy(errors = newErrors)
        return newErrors.isEmpty()
    }
}