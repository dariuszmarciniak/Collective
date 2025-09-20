package com.daromon.collective.ui.components

import android.net.Uri
import com.daromon.collective.domain.model.FuelType

data class CarFormState(
    val model: String = "",
    val brand: String = "",
    val year: String = "",
    val vin: String = "",
    val registrationNumber: String = "",
    val mileage: String = "",
    val fuelType: FuelType? = null,
    val engineCapacity: String = "",
    val power: String = "",
    val color: String = "",
    val notes: String = "",
    val inspectionDate: String = "",
    val insuranceExpiry: String = "",
    val errors: Map<String, String> = emptyMap(),
    val photoUri: Uri? = null
)
