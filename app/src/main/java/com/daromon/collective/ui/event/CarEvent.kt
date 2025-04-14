package com.daromon.collective.ui.event

import com.daromon.collective.domain.model.Car

sealed class CarEvent {
    object Load : CarEvent()
    data class Add(val car: Car) : CarEvent()
    data class Delete(val car: Car) : CarEvent()
}