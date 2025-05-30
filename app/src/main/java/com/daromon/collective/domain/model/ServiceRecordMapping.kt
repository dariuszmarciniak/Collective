package com.daromon.collective.domain.model

import com.daromon.collective.data.local.ServiceRecordEntity

fun ServiceRecord.toEntity() = ServiceRecordEntity(
    id = id,
    carId = carId,
    date = date,
    description = description,
    cost = cost,
    type = type
)

fun ServiceRecordEntity.toDomain() = ServiceRecord(
    id = id,
    carId = carId,
    date = date,
    description = description,
    cost = cost,
    type = type
)