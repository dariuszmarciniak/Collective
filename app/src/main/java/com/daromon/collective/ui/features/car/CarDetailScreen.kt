package com.daromon.collective.ui.features.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.R
import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.components.CarFormFields
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.state.CarUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    carId: Int, navController: NavController, viewModel: CarViewModel
) {
    val state by viewModel.state.collectAsState()
    val car = (state as? CarUiState.Success)?.cars?.find { it.id == carId }
    val carFormState by viewModel.formState.collectAsState()
    var showImageDialog by remember { mutableStateOf(false) }
    var showFuelTypeMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val path = viewModel.copyImageToInternalStorage(context, uri)
            viewModel.onPhotoChange(path?.toUri())
        }
    }
    val scrollState = rememberScrollState()
    fun onShowFuelTypeMenuChange(show: Boolean) {
        showFuelTypeMenu = show
    }
    if (car != null) {
        viewModel.setForm(car)
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
                        if (viewModel.validate(context)) {
                            viewModel.onEvent(
                                CarEvent.Update(
                                    Car(
                                        id = car.id,
                                        model = carFormState.model,
                                        brand = carFormState.brand,
                                        year = carFormState.year.toIntOrNull(),
                                        photoUri = carFormState.photoUri?.toString(),
                                        vin = carFormState.vin.ifBlank { null },
                                        registrationNumber = carFormState.registrationNumber.ifBlank { null },
                                        mileage = carFormState.mileage.toIntOrNull(),
                                        fuelType = carFormState.fuelType?.displayName,
                                        engineCapacity = carFormState.engineCapacity.toDoubleOrNull(),
                                        power = carFormState.power.toIntOrNull(),
                                        color = carFormState.color.ifBlank { null },
                                        notes = carFormState.notes.ifBlank { null },
                                        inspectionDate = carFormState.inspectionDate.ifBlank { null },
                                        insuranceExpiry = carFormState.insuranceExpiry.ifBlank { null })
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
                if (carFormState.photoUri != null) {
                    AsyncImage(
                        model = carFormState.photoUri,
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
                if (showImageDialog && carFormState.photoUri != null) {
                    AlertDialog(
                        onDismissRequest = { showImageDialog = false },
                        confirmButton = {},
                        title = { Text(stringResource(R.string.preview)) },
                        text = {
                            AsyncImage(
                                model = carFormState.photoUri,
                                contentDescription = stringResource(R.string.select_photo),
                                modifier = Modifier.size(300.dp)
                            )
                        })
                }
                CarFormFields(
                    state = carFormState,
                    onFieldChange = viewModel::onFieldChange,
                    onFuelTypeChange = viewModel::onFuelTypeChange,
                    showFuelTypeMenu = showFuelTypeMenu,
                    onShowFuelTypeMenuChange = ::onShowFuelTypeMenuChange,
                    modifier = Modifier.fillMaxWidth()
                )
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