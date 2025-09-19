package com.daromon.collective.ui.features.car

import DatePickerField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.R
import com.daromon.collective.domain.model.Car
import com.daromon.collective.domain.model.FuelType
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.state.CarUiState

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
            if (brand.isBlank()) newErrors["brand"] = context.getString(R.string.required_field)
            if (model.isBlank()) newErrors["model"] = context.getString(R.string.required_field)
            if (year.isNotBlank() && (year.length != 4 || year.toIntOrNull() == null)) newErrors["year"] =
                context.getString(R.string.year_must_be_4_digits)
            if (mileage.isNotBlank() && mileage.toIntOrNull() == null) newErrors["mileage"] =
                context.getString(R.string.numbers_only)
            if (engineCapacity.isNotBlank() && engineCapacity.toDoubleOrNull() == null) newErrors["engineCapacity"] =
                context.getString(R.string.numbers_only)
            if (power.isNotBlank() && power.toIntOrNull() == null) newErrors["power"] =
                context.getString(R.string.numbers_only)
            errors = newErrors
            return newErrors.isEmpty()
        }

        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text(stringResource(R.string.car_details), fontWeight = FontWeight.Bold)
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
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
                    Text(stringResource(R.string.update))
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
                        contentDescription = stringResource(R.string.select_photo),
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { showImageDialog = true })
                }
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.change_photo))
                }
                if (showImageDialog && photoUri != null) {
                    AlertDialog(
                        onDismissRequest = { showImageDialog = false },
                        confirmButton = {},
                        title = { Text(stringResource(R.string.preview)) },
                        text = {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = stringResource(R.string.select_photo),
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
                    label = { Text(stringResource(R.string.brand)) },
                    placeholder = { Text(stringResource(R.string.brand_placeholder)) },
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
                    label = { Text(stringResource(R.string.model)) },
                    placeholder = { Text(stringResource(R.string.model_placeholder)) },
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
                    label = { Text(stringResource(R.string.year)) },
                    placeholder = { Text(stringResource(R.string.year_placeholder)) },
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
                    label = { Text(stringResource(R.string.vin)) },
                    placeholder = { Text(stringResource(R.string.vin_placeholder)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = registrationNumber,
                    onValueChange = { registrationNumber = it },
                    label = { Text(stringResource(R.string.registration_number)) },
                    placeholder = { Text(stringResource(R.string.registration_number_placeholder)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mileage,
                    onValueChange = {
                        mileage = it.filter { c -> c.isDigit() }
                        errors = errors - "mileage"
                    },
                    label = { Text(stringResource(R.string.mileage)) },
                    placeholder = { Text(stringResource(R.string.mileage_placeholder)) },
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
                        value = fuelType?.let {
                            when (it) {
                                FuelType.PETROL -> stringResource(R.string.petrol)
                                FuelType.DIESEL -> stringResource(R.string.diesel)
                                FuelType.LPG -> stringResource(R.string.lpg)
                                FuelType.ELECTRIC -> stringResource(R.string.electric)
                                FuelType.HYBRID -> stringResource(R.string.hybrid)
                            }
                        } ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(R.string.fuel_type)) },
                        placeholder = { Text(stringResource(R.string.select_fuel_type)) },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showFuelTypeMenu) })
                    ExposedDropdownMenu(
                        expanded = showFuelTypeMenu,
                        onDismissRequest = { showFuelTypeMenu = false }) {
                        FuelType.values().forEach { type ->
                            DropdownMenuItem(text = {
                                Text(
                                    when (type) {
                                        FuelType.PETROL -> stringResource(R.string.petrol)
                                        FuelType.DIESEL -> stringResource(R.string.diesel)
                                        FuelType.LPG -> stringResource(R.string.lpg)
                                        FuelType.ELECTRIC -> stringResource(R.string.electric)
                                        FuelType.HYBRID -> stringResource(R.string.hybrid)
                                    }
                                )
                            }, onClick = {
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
                    label = { Text(stringResource(R.string.engine_capacity)) },
                    placeholder = { Text(stringResource(R.string.engine_capacity_placeholder)) },
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
                    label = { Text(stringResource(R.string.power)) },
                    placeholder = { Text(stringResource(R.string.power_placeholder)) },
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
                    label = { Text(stringResource(R.string.color)) },
                    placeholder = { Text(stringResource(R.string.color_placeholder)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.notes_label)) },
                    placeholder = { Text(stringResource(R.string.notes_placeholder)) },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(
                    value = inspectionDate,
                    onValueChange = { inspectionDate = it },
                    label = stringResource(R.string.inspection_date),
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(
                    value = insuranceExpiry,
                    onValueChange = { insuranceExpiry = it },
                    label = stringResource(R.string.insurance_expiry),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(80.dp))
                Button(
                    onClick = { navController.navigate("service_history/${car.id}") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.service_history))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.delete))
                }
                if (showConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text(stringResource(R.string.delete_car)) },
                        text = { Text(stringResource(R.string.delete_confirm)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.onEvent(CarEvent.Delete(car))
                                navController.popBackStack()
                            }) {
                                Text(stringResource(R.string.yes))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text(stringResource(R.string.no))
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