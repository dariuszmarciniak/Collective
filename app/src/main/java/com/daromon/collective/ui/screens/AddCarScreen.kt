package com.daromon.collective.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daromon.collective.domain.model.Car
import com.daromon.collective.ui.event.CarEvent
import com.daromon.collective.viewmodel.CarViewModel

@Composable
fun AddCarScreen(navController: NavController, viewModel: CarViewModel) {
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add New Car", style = MaterialTheme.typography.displayMedium)

        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model") })
        OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Year") })

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            if (model.isNotBlank() && year.isNotBlank()) {
                viewModel.onEvent(CarEvent.Add(Car(model = model, year = year.toInt())))
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }
    }
}