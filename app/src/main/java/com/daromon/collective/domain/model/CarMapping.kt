package com.daromon.collective.domain.model

import com.daromon.collective.data.local.CarEntity

fun Car.toEntity() = CarEntity(
    id = id,
    model = model,
    year = year,
    photoUri = photoUri,
    brand = brand,
    vin = vin,
    registrationNumber = registrationNumber,
    mileage = mileage,
    fuelType = fuelType,
    engineCapacity = engineCapacity,
    power = power,
    color = color,
    notes = notes,
    inspectionDate = inspectionDate,
    insuranceExpiry = insuranceExpiry
)

fun CarEntity.toDomain() = Car(
    id = id,
    model = model,
    year = year,
    photoUri = photoUri,
    brand = brand,
    vin = vin,
    registrationNumber = registrationNumber,
    mileage = mileage,
    fuelType = fuelType,
    engineCapacity = engineCapacity,
    power = power,
    color = color,
    notes = notes,
    inspectionDate = inspectionDate,
    insuranceExpiry = insuranceExpiry
)