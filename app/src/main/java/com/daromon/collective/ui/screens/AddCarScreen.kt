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
    var showError by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

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
                .fillMaxWidth(),
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
                value = model, onValueChange = {
                    model = it
                    showError = false
                }, label = { Text("Model") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = year,
                onValueChange = {
                    year = it
                    showError = false
                },
                label = { Text("Year") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = showError && year.toIntOrNull() == null
            )
            if (showError && (model.isBlank() || year.isBlank() || year.toIntOrNull() == null)) {
                Text(
                    "Please enter a valid model and year.",
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
                                    photoUri = photoUri?.toString()
                                )
                            )
                        )
                        navController.popBackStack()
                    } else {
                        showError = true
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}