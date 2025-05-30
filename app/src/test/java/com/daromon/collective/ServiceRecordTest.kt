package com.daromon.collective

import com.daromon.collective.domain.model.ServiceRecord
import com.daromon.collective.domain.model.toDomain
import com.daromon.collective.domain.model.toEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ServiceRecordTest {

    @Test
    fun createsServiceRecordWithAllFields() {
        val record = ServiceRecord(
            id = 1,
            carId = 2,
            date = "01.01.2024",
            description = "Oil change",
            cost = 199.99,
            type = "Maintenance"
        )
        assertEquals(1, record.id)
        assertEquals(2, record.carId)
        assertEquals("01.01.2024", record.date)
        assertEquals("Oil change", record.description)
        assertEquals(199.99, record.cost, 0.01)
        assertEquals("Maintenance", record.type)
    }

    @Test
    fun serviceRecordsWithSameDataAreEqual() {
        val record1 = ServiceRecord(1, 2, "01.01.2024", "Oil change", 100.0, "Maintenance")
        val record2 = ServiceRecord(1, 2, "01.01.2024", "Oil change", 100.0, "Maintenance")
        assertEquals(record1, record2)
    }

    @Test
    fun serviceRecordsWithDifferentIdsAreNotEqual() {
        val record1 = ServiceRecord(1, 2, "01.01.2024", "Oil change", 100.0, "Maintenance")
        val record2 = ServiceRecord(2, 2, "01.01.2024", "Oil change", 100.0, "Maintenance")
        assertNotEquals(record1, record2)
    }

    @Test
    fun serviceRecordCostCanBeZero() {
        val record = ServiceRecord(1, 2, "01.01.2024", "Free check", 0.0, "Inspection")
        assertEquals(0.0, record.cost, 0.0)
    }

    @Test
    fun serviceRecordWithEmptyDescription() {
        val record = ServiceRecord(1, 2, "01.01.2024", "", 50.0, "Other")
        assertEquals("", record.description)
    }

    @Test
    fun serviceRecordToEntityAndBack() {
        val record = ServiceRecord(1, 2, "01.01.2024", "Oil change", 100.0, "Maintenance")
        val entity = record.toEntity()
        val mappedBack = entity.toDomain()
        assertEquals(record, mappedBack)
    }
}
