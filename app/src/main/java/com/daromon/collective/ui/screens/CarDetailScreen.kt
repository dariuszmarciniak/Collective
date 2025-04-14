package com.daromon.collective.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.ui.state.CarUiState
import com.daromon.collective.viewmodel.CarViewModel

@Composable
fun CarDetailScreen(
    carId: Int, navController: NavController, viewModel: CarViewModel
) {
    val state by viewModel.state.collectAsState()

    val car = (state as? CarUiState.Success)?.cars?.find { it.id == carId }

    if (car != null) {
        var model by remember { mutableStateOf(car.model) }
        var year by remember { mutableStateOf(car.year.toString()) }
        var showConfirmDialog by remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Edit Car Info", style = MaterialTheme.typography.bodyMedium)

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Model") })
            OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Year") })

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                viewModel.onEvent(
                    CarEvent.Add(
                        Car(
                            id = car.id, model = model, year = year.toInt()
                        )
                    )
                )
                navController.popBackStack()
            }) {
                Text("Update")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showConfirmDialog = true },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
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
    } else {
        CircularProgressIndicator()
    }
}