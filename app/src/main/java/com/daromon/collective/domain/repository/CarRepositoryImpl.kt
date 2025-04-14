package com.daromon.collective.domain.repository

import com.daromon.collective.data.local.CarDao
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.model.toDomain
import com.daromon.collective.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarRepositoryImpl(private val dao: CarDao) : CarRepository {
    override fun getCars(): Flow<List<Car>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(car: Car) = dao.insert(car.toEntity())
    override suspend fun delete(car: Car) = dao.delete(car.toEntity())
}
