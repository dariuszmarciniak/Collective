package com.daromon.collective.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val model: String,
    val year: Int,
    val photoUri: String?,
    val brand: String?,
    val vin: String?,
    val registrationNumber: String?,
    val mileage: Int?,
    val fuelType: String?,
    val engineCapacity: Double?,
    val power: Int?,
    val color: String?,
    val notes: String?,
    val inspectionDate: String?,
    val insuranceExpiry: String?
)