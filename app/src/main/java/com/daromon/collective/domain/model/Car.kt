package com.daromon.collective.domain.model

import com.daromon.collective.data.local.CarEntity

data class Car(
    val id: Int = 0,
    val model: String,
    val year: Int,
    val photoUri: String? = null
)

fun CarEntity.toDomain() = Car(id, model, year, photoUri)
fun Car.toEntity() = CarEntity(id, model, year, photoUri)