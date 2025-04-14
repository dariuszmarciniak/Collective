package com.daromon.collective.domain.usecase

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(private val repo: CarRepository) {
    operator fun invoke(): Flow<List<Car>> = repo.getCars()
}