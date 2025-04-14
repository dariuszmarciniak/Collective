package com.daromon.collective.ui.state

import com.daromon.collective.domain.model.Car

sealed class CarUiState {
    object Loading : CarUiState()
    data class Success(val cars: List<Car>) : CarUiState()
    data class Error(val message: String) : CarUiState()
}
