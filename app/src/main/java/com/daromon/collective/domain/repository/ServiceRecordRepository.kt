package com.daromon.collective.domain.repository

import com.daromon.collective.domain.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

interface ServiceRecordRepository {
    fun getRecordsForCar(carId: Int): Flow<List<ServiceRecord>>
    suspend fun insert(record: ServiceRecord)
    suspend fun delete(record: ServiceRecord)
    suspend fun update(record: ServiceRecord)
}