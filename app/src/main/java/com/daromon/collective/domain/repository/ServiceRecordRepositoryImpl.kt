package com.daromon.collective.domain.repository

import com.daromon.collective.data.local.ServiceRecordDao
import com.daromon.collective.domain.model.ServiceRecord
import com.daromon.collective.domain.model.toDomain
import com.daromon.collective.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServiceRecordRepositoryImpl(
    private val dao: ServiceRecordDao
) : ServiceRecordRepository {
    override fun getRecordsForCar(carId: Int): Flow<List<ServiceRecord>> =
        dao.getRecordsForCar(carId).map { list -> list.map { it.toDomain() } }

    override suspend fun insert(record: ServiceRecord) = dao.insert(record.toEntity())
    override suspend fun delete(record: ServiceRecord) = dao.delete(record.toEntity())
    override suspend fun update(record: ServiceRecord) = dao.update(record.toEntity())

}