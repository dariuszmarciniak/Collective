package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepository
import javax.inject.Inject

class AddCarUseCase @Inject constructor(private val repo: CarRepository) {
    suspend operator fun invoke(car: Car) = repo.insert(car)
}