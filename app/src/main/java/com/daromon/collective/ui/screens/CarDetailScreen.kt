package com.daromon.collective.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.state.CarUiState
import com.daromon.collective.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    carId: Int, navController: NavController, viewModel: CarViewModel
) {
    val state by viewModel.state.collectAsState()
    val car = (state as? CarUiState.Success)?.cars?.find { it.id == carId }

    if (car != null) {
        var model by remember { mutableStateOf(car.model) }
        var year by remember { mutableStateOf(car.year.toString()) }
        var photoUri by remember { mutableStateOf(car.photoUri?.toUri()) }
        var brand by remember { mutableStateOf(car.brand ?: "") }
        var vin by remember { mutableStateOf(car.vin ?: "") }
        var registrationNumber by remember { mutableStateOf(car.registrationNumber ?: "") }
        var mileage by remember { mutableStateOf(car.mileage?.toString() ?: "") }
        var fuelType by remember { mutableStateOf(car.fuelType ?: "") }
        var engineCapacity by remember { mutableStateOf(car.engineCapacity?.toString() ?: "") }
        var power by remember { mutableStateOf(car.power?.toString() ?: "") }
        var color by remember { mutableStateOf(car.color ?: "") }
        var notes by remember { mutableStateOf(car.notes ?: "") }
        var inspectionDate by remember { mutableStateOf(car.inspectionDate ?: "") }
        var insuranceExpiry by remember { mutableStateOf(car.insuranceExpiry ?: "") }
        var showError by remember { mutableStateOf(false) }
        var showConfirmDialog by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                val path = viewModel.copyImageToInternalStorage(context, uri)
                photoUri = path?.toUri()
            }
        }

        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text("Car Details", fontWeight = FontWeight.Bold)
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
            }) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Car photo",
                        modifier = Modifier.size(96.dp)
                    )
                }
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Change Photo")
                }
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it; showError = false },
                    label = { Text("Model") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it; showError = false },
                    label = { Text("Year") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && year.toIntOrNull() == null
                )
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marka") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = vin,
                    onValueChange = { vin = it },
                    label = { Text("VIN") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = registrationNumber,
                    onValueChange = { registrationNumber = it },
                    label = { Text("Numer rejestracyjny") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = { Text("Przebieg") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fuelType,
                    onValueChange = { fuelType = it },
                    label = { Text("Typ paliwa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = engineCapacity,
                    onValueChange = { engineCapacity = it },
                    label = { Text("Pojemność silnika") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = power,
                    onValueChange = { power = it },
                    label = { Text("Moc silnika") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Kolor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notatki") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inspectionDate,
                    onValueChange = { inspectionDate = it },
                    label = { Text("Data przeglądu") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = insuranceExpiry,
                    onValueChange = { insuranceExpiry = it },
                    label = { Text("Wygaśnięcie ubezpieczenia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && (model.isBlank() || year.isBlank() || year.toIntOrNull() == null)) {
                    Text(
                        "Wprowadź poprawny model i rok.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (model.isNotBlank() && year.toIntOrNull() != null) {
                            viewModel.onEvent(
                                CarEvent.Update(
                                    Car(
                                        id = car.id,
                                        model = model,
                                        year = year.toInt(),
                                        photoUri = photoUri?.toString(),
                                        brand = brand,
                                        vin = vin,
                                        registrationNumber = registrationNumber,
                                        mileage = mileage.toIntOrNull(),
                                        fuelType = fuelType,
                                        engineCapacity = engineCapacity.toDoubleOrNull(),
                                        power = power.toIntOrNull(),
                                        color = color,
                                        notes = notes,
                                        inspectionDate = inspectionDate,
                                        insuranceExpiry = insuranceExpiry
                                    )
                                )
                            )
                            navController.popBackStack()
                        } else {
                            showError = true
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete")
                }
                if (showConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Delete Car") },
                        text = { Text("Are you sure you want to delete this car?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.onEvent(CarEvent.Delete(car))
                                navController.popBackStack()
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text("No")
                            }
                        })
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}