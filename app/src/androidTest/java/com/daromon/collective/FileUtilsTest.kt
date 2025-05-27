package com.daromon.collective

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.daromon.collective.domain.repository.CarRepository
import com.daromon.collective.domain.usecase.AddCarUseCase
import com.daromon.collective.domain.usecase.DeleteCarUseCase
import com.daromon.collective.domain.usecase.GetCarsUseCase
import com.daromon.collective.domain.usecase.UpdateCarUseCase
import com.daromon.collective.viewmodel.CarViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File
import java.io.FileOutputStream

class FileUtilsTest {

    private val mockRepo = mock<CarRepository>()
    private val viewModel = CarViewModel(
        getCars = GetCarsUseCase(mockRepo),
        addCar = AddCarUseCase(mockRepo),
        deleteCar = DeleteCarUseCase(mockRepo),
        updateCar = UpdateCarUseCase(mockRepo)
    )


    @Test
    fun `copyImageToInternalStorage should copy file and return valid path`() {
        // Arrange
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testFileName = "test_image.jpg"
        val testFileContent = "Test content".toByteArray()
        val testFile = File(context.cacheDir, testFileName)
        FileOutputStream(testFile).use { it.write(testFileContent) }
        val testUri = Uri.fromFile(testFile)

        // Act
        val resultPath = viewModel.copyImageToInternalStorage(context, testUri)

        // Assert
        assertNotNull(resultPath)
        val resultFile = File(resultPath!!)
        assertTrue(resultFile.exists())
        assertEquals(testFileContent.size, resultFile.readBytes().size)

        // Clean up
        resultFile.delete()
        testFile.delete()
    }
}