package com.daromon.collective.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val model: String,
    val brand: String,
    val year: Int? = null,
    val photoUri: String? = null,
    val vin: String? = null,
    val registrationNumber: String? = null,
    val mileage: Int? = null,
    val fuelType: String? = null,
    val engineCapacity: Double? = null,
    val power: Int? = null,
    val color: String? = null,
    val notes: String? = null,
    val inspectionDate: String? = null,
    val insuranceExpiry: String? = null
)