package com.daromon.collective.domain.repository

import com.daromon.collective.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getCars(): Flow<List<Car>>
    suspend fun insert(car: Car)
    suspend fun delete(car: Car)
}