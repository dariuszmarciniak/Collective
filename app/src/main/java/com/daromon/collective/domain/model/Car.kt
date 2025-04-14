package com.daromon.collective.domain.model

import com.daromon.collective.data.local.CarEntity

data class Car(
    val id: Int = 0,
    val model: String,
    val year: Int
)

fun CarEntity.toDomain() = Car(id, model, year)
fun Car.toEntity() = CarEntity(id, model, year)