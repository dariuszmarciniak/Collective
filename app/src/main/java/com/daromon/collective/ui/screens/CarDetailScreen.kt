package com.daromon.collective.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.model.FuelType
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
        var model by remember { mutableStateOf(car.model ?: "") }
        var brand by remember { mutableStateOf(car.brand ?: "") }
        var year by remember { mutableStateOf(car.year?.toString() ?: "") }
        var photoUri by remember { mutableStateOf(car.photoUri?.toUri()) }
        var vin by remember { mutableStateOf(car.vin ?: "") }
        var registrationNumber by remember { mutableStateOf(car.registrationNumber ?: "") }
        var mileage by remember { mutableStateOf(car.mileage?.toString() ?: "") }
        var fuelType by remember {
            mutableStateOf(
                FuelType.values().find { it.displayName == car.fuelType })
        }
        var engineCapacity by remember { mutableStateOf(car.engineCapacity?.toString() ?: "") }
        var power by remember { mutableStateOf(car.power?.toString() ?: "") }
        var color by remember { mutableStateOf(car.color ?: "") }
        var notes by remember { mutableStateOf(car.notes ?: "") }
        var inspectionDate by remember { mutableStateOf(car.inspectionDate ?: "") }
        var insuranceExpiry by remember { mutableStateOf(car.insuranceExpiry ?: "") }
        var errors by remember { mutableStateOf(mapOf<String, String>()) }
        var showConfirmDialog by remember { mutableStateOf(false) }
        var showFuelTypeMenu by remember { mutableStateOf(false) }
        var showImageDialog by remember { mutableStateOf(false) }

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

        fun validate(): Boolean {
            val newErrors = mutableMapOf<String, String>()
            if (brand.isBlank()) newErrors["brand"] = "Required field"
            if (model.isBlank()) newErrors["model"] = "Required field"
            if (year.isNotBlank() && (year.length != 4 || year.toIntOrNull() == null)) newErrors["year"] =
                "Year must be 4 digits"
            if (mileage.isNotBlank() && mileage.toIntOrNull() == null) newErrors["mileage"] =
                "Numbers only"
            if (engineCapacity.isNotBlank() && engineCapacity.toDoubleOrNull() == null) newErrors["engineCapacity"] =
                "Numbers only"
            if (power.isNotBlank() && power.toIntOrNull() == null) newErrors["power"] =
                "Numbers only"
            errors = newErrors
            return newErrors.isEmpty()
        }

        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text("Car Details", fontWeight = FontWeight.Bold)
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }, bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        if (validate()) {
                            viewModel.onEvent(
                                CarEvent.Update(
                                    Car(
                                        id = car.id,
                                        model = model,
                                        brand = brand,
                                        year = year.toIntOrNull(),
                                        photoUri = photoUri?.toString(),
                                        vin = vin.ifBlank { null },
                                        registrationNumber = registrationNumber.ifBlank { null },
                                        mileage = mileage.toIntOrNull(),
                                        fuelType = fuelType?.displayName,
                                        engineCapacity = engineCapacity.toDoubleOrNull(),
                                        power = power.toIntOrNull(),
                                        color = color.ifBlank { null },
                                        notes = notes.ifBlank { null },
                                        inspectionDate = inspectionDate.ifBlank { null },
                                        insuranceExpiry = insuranceExpiry.ifBlank { null })
                                )
                            )
                            navController.popBackStack()
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Update")
                }
            }
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
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { showImageDialog = true })
                }
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Change Photo")
                }
                if (showImageDialog && photoUri != null) {
                    AlertDialog(
                        onDismissRequest = { showImageDialog = false },
                        confirmButton = {},
                        title = { Text("Preview") },
                        text = {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = "Car photo",
                                modifier = Modifier.size(300.dp)
                            )
                        })
                }
                OutlinedTextField(
                    value = brand,
                    onValueChange = {
                        brand = it
                        errors = errors - "brand"
                    },
                    label = { Text("Brand*") },
                    placeholder = { Text("e.g. Toyota") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.containsKey("brand"),
                    supportingText = {
                        errors["brand"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                OutlinedTextField(
                    value = model,
                    onValueChange = {
                        model = it
                        errors = errors - "model"
                    },
                    label = { Text("Model*") },
                    placeholder = { Text("e.g. Corolla") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.containsKey("model"),
                    supportingText = {
                        errors["model"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        if (it.length <= 4) year = it.filter { c -> c.isDigit() }
                        errors = errors - "year"
                    },
                    label = { Text("Year") },
                    placeholder = { Text("e.g. 2020") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("year"),
                    supportingText = {
                        errors["year"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                OutlinedTextField(
                    value = vin,
                    onValueChange = { vin = it },
                    label = { Text("VIN") },
                    placeholder = { Text("17 characters") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = registrationNumber,
                    onValueChange = { registrationNumber = it },
                    label = { Text("Registration Number") },
                    placeholder = { Text("e.g. WX12345") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mileage,
                    onValueChange = {
                        mileage = it.filter { c -> c.isDigit() }
                        errors = errors - "mileage"
                    },
                    label = { Text("Mileage") },
                    placeholder = { Text("e.g. 150000") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("mileage"),
                    supportingText = {
                        errors["mileage"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                ExposedDropdownMenuBox(
                    expanded = showFuelTypeMenu,
                    onExpandedChange = { showFuelTypeMenu = !showFuelTypeMenu }) {
                    OutlinedTextField(
                        value = fuelType?.displayName ?: "",
                        onValueChange = {},
                        label = { Text("Fuel Type") },
                        placeholder = { Text("Select fuel type") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showFuelTypeMenu) })
                    ExposedDropdownMenu(
                        expanded = showFuelTypeMenu,
                        onDismissRequest = { showFuelTypeMenu = false }) {
                        FuelType.values().forEach { type ->
                            DropdownMenuItem(text = { Text(type.displayName) }, onClick = {
                                fuelType = type
                                showFuelTypeMenu = false
                            })
                        }
                    }
                }
                OutlinedTextField(
                    value = engineCapacity,
                    onValueChange = {
                        engineCapacity = it.filter { c -> c.isDigit() || c == '.' }
                        errors = errors - "engineCapacity"
                    },
                    label = { Text("Engine Capacity") },
                    placeholder = { Text("e.g. 1.8") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("engineCapacity"),
                    supportingText = {
                        errors["engineCapacity"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                OutlinedTextField(
                    value = power,
                    onValueChange = {
                        power = it.filter { c -> c.isDigit() }
                        errors = errors - "power"
                    },
                    label = { Text("Power") },
                    placeholder = { Text("e.g. 140") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.containsKey("power"),
                    supportingText = {
                        errors["power"]?.let {
                            Text(
                                it, color = MaterialTheme.colorScheme.error
                            )
                        }
                    })
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    placeholder = { Text("e.g. Red") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    placeholder = { Text("Additional notes") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inspectionDate,
                    onValueChange = { inspectionDate = it },
                    label = { Text("Inspection Date") },
                    placeholder = { Text("e.g. 01.01.2025") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = insuranceExpiry,
                    onValueChange = { insuranceExpiry = it },
                    label = { Text("Insurance Expiry") },
                    placeholder = { Text("e.g. 01.01.2025") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(80.dp))
                Button(
                    onClick = { navController.navigate("service_history/${car.id}") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Service History")
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