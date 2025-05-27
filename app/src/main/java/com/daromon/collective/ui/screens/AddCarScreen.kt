package com.daromon.collective.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(navController: NavController, viewModel: CarViewModel) {
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var fuelType by remember { mutableStateOf("") }
    var engineCapacity by remember { mutableStateOf("") }
    var power by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var inspectionDate by remember { mutableStateOf("") }
    var insuranceExpiry by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }


    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<String?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = viewModel.copyImageToInternalStorage(context, uri)
            photoUri = path
        }
    }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add New Car", fontWeight = FontWeight.Bold) },
                navigationIcon = {
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
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
                Text("Select Photo")
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
                            CarEvent.Add(
                                Car(
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
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}