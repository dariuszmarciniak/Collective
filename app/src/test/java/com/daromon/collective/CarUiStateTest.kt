package com.daromon.collective

import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.state.CarUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class CarUiStateTest {

    @Test
    fun `Success state should hold cars list`() {
        val cars = listOf(Car(id = 1, model = "A", brand = "B", year = 2020))
        val state = CarUiState.Success(cars)
        assertEquals(cars, state.cars)
    }

    @Test
    fun `Error state should hold message`() {
        val state = CarUiState.Error("error")
        assertEquals("error", state.message)
    }
}