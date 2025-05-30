package com.daromon.collective.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_records")
data class ServiceRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val date: String,
    val description: String,
    val cost: Double,
    val type: String
)