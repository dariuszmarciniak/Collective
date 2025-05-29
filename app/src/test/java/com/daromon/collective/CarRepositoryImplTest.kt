package com.daromon.collective

import com.daromon.collective.data.local.CarDao
import com.daromon.collective.data.local.CarEntity
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.repository.CarRepositoryImpl
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CarRepositoryImplTest {

    private val dao = mock<CarDao>()
    private lateinit var repo: CarRepositoryImpl

    @Before
    fun setup() {
        repo = CarRepositoryImpl(dao)
    }

    @Test
    fun `insert should call dao insert`() = runTest {
        val car = Car(model = "Test", brand = "Brand")
        repo.insert(car)
        verify(dao).insert(any())
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val car = Car(model = "Test", brand = "Brand")
        repo.delete(car)
        verify(dao).delete(any())
    }

    @Test
    fun `updateCar should call dao update`() = runTest {
        val car = Car(model = "Test", brand = "Brand")
        repo.updateCar(car)
        verify(dao).update(any())
    }

    @Test
    fun `getCars should map entities to domain`() = runTest {
        val entity = CarEntity(id = 1, model = "A", brand = "B", year = 2020)
        whenever(dao.getAll()).thenReturn(flowOf(listOf(entity)))
        val result = repo.getCars()
        result.collect { list ->
            assert(list.size == 1)
            assert(list[0].id == 1)
            assert(list[0].model == "A")
            assert(list[0].brand == "B")
            assert(list[0].year == 2020)
        }
    }
}