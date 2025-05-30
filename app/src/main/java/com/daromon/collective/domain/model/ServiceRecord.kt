package com.daromon.collective.domain.model

data class ServiceRecord(
    val id: Int = 0,
    val carId: Int,
    val date: String,
    val description: String,
    val cost: Double,
    val type: String
)