package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepository
import javax.inject.Inject

class UpdateCarUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(car: Car) {
        repository.updateCar(car)
    }
}