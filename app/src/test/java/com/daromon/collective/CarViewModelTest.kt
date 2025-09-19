package com.daromon.collective

import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepository
import com.daromon.collective.domain.usecase.AddCarUseCase
import com.daromon.collective.domain.usecase.DeleteCarUseCase
import com.daromon.collective.domain.usecase.GetCarsUseCase
import com.daromon.collective.domain.usecase.UpdateCarUseCase
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.features.car.CarViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CarViewModelTest {

    private val repo = mock<CarRepository>()
    private lateinit var viewModel: CarViewModel

    @Before
    fun setup() {
        whenever(repo.getCars()).thenReturn(flowOf(emptyList()))
        viewModel = CarViewModel(
            getCars = GetCarsUseCase(repo),
            addCar = AddCarUseCase(repo),
            deleteCar = DeleteCarUseCase(repo),
            updateCar = UpdateCarUseCase(repo)
        )
    }

    @Test
    fun `onEvent Add should call addCar use case when brand and model are not blank`() = runTest {
        val car = Car(model = "Test", brand = "Brand")
        viewModel.onEvent(CarEvent.Add(car))
        verify(repo, timeout(1000)).insert(car)
    }

    @Test
    fun `onEvent Add should NOT call addCar when model is blank`() = runTest {
        val car = Car(model = "", brand = "Brand")
        viewModel.onEvent(CarEvent.Add(car))
        verify(repo, never()).insert(any())
    }

    @Test
    fun `onEvent Add should NOT call addCar when brand is blank`() = runTest {
        val car = Car(model = "Test", brand = "")
        viewModel.onEvent(CarEvent.Add(car))
        verify(repo, never()).insert(any())
    }

    @Test
    fun `onEvent Update should call updateCar use case when brand and model are not blank`() =
        runTest {
            val car = Car(model = "Test", brand = "Brand")
            viewModel.onEvent(CarEvent.Update(car))
            verify(repo, timeout(1000)).updateCar(car)
        }

    @Test
    fun `onEvent Update should NOT call updateCar when model is blank`() = runTest {
        val car = Car(model = "", brand = "Brand")
        viewModel.onEvent(CarEvent.Update(car))
        verify(repo, never()).updateCar(any())
    }

    @Test
    fun `onEvent Update should NOT call updateCar when brand is blank`() = runTest {
        val car = Car(model = "Test", brand = "")
        viewModel.onEvent(CarEvent.Update(car))
        verify(repo, never()).updateCar(any())
    }

    @Test
    fun `onEvent Delete should call deleteCar use case`() = runTest {
        val car = Car(model = "Test", brand = "Brand")
        viewModel.onEvent(CarEvent.Delete(car))
        verify(repo, timeout(1000)).delete(car)
    }


    @Test
    fun `should emit error state when add fails`() = runTest {
        whenever(repo.insert(any())).thenThrow(RuntimeException("fail"))
        val emittedStates = mutableListOf<com.daromon.collective.ui.state.CarUiState>()
        val job = launch {
            viewModel.state.toList(emittedStates)
        }
        viewModel.onEvent(CarEvent.Add(Car(model = "A", brand = "B")))
        advanceUntilIdle()
        job.cancel()
        assert(emittedStates.any { it is com.daromon.collective.ui.state.CarUiState.Error })
    }
}