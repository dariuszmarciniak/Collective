package com.daromon.collective

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.model.toDomain
import com.daromon.collective.domain.model.toEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CarMappingTest {

    @Test
    fun `car toEntity and back should preserve all fields`() {
        val car = Car(
            id = 1,
            model = "A",
            brand = "B",
            year = 2020,
            photoUri = "uri",
            vin = "VIN",
            registrationNumber = "REG",
            mileage = 12345,
            fuelType = "Diesel",
            engineCapacity = 2.0,
            power = 150,
            color = "Red",
            notes = "Note",
            inspectionDate = "2024-01-01",
            insuranceExpiry = "2024-12-31"
        )
        val entity = car.toEntity()
        val domain = entity.toDomain()
        assertEquals(car, domain)
        assertEquals(car.id, entity.id)
        assertEquals(car.model, entity.model)
        assertEquals(car.brand, entity.brand)
        assertEquals(car.year, entity.year)
        assertEquals(car.photoUri, entity.photoUri)
        assertEquals(car.vin, entity.vin)
        assertEquals(car.registrationNumber, entity.registrationNumber)
        assertEquals(car.mileage, entity.mileage)
        assertEquals(car.fuelType, entity.fuelType)
        assertEquals(car.engineCapacity, entity.engineCapacity)
        assertEquals(car.power, entity.power)
        assertEquals(car.color, entity.color)
        assertEquals(car.notes, entity.notes)
        assertEquals(car.inspectionDate, entity.inspectionDate)
        assertEquals(car.insuranceExpiry, entity.insuranceExpiry)
    }

    @Test
    fun `car toEntity and back with nulls should preserve nulls`() {
        val car = Car(id = 0, model = "", brand = "")
        val entity = car.toEntity()
        val domain = entity.toDomain()
        assertEquals(car, domain)
        assertEquals(car.id, entity.id)
        assertEquals(car.model, entity.model)
        assertEquals(car.brand, entity.brand)
        assertEquals(car.year, entity.year)
        assertEquals(car.photoUri, entity.photoUri)
        assertEquals(car.vin, entity.vin)
        assertEquals(car.registrationNumber, entity.registrationNumber)
        assertEquals(car.mileage, entity.mileage)
        assertEquals(car.fuelType, entity.fuelType)
        assertEquals(car.engineCapacity, entity.engineCapacity)
        assertEquals(car.power, entity.power)
        assertEquals(car.color, entity.color)
        assertEquals(car.notes, entity.notes)
        assertEquals(car.inspectionDate, entity.inspectionDate)
        assertEquals(car.insuranceExpiry, entity.insuranceExpiry)
    }
}