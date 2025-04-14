package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepository
import javax.inject.Inject

class DeleteCarUseCase @Inject constructor(private val repo: CarRepository) {
    suspend operator fun invoke(car: Car) = repo.delete(car)
}