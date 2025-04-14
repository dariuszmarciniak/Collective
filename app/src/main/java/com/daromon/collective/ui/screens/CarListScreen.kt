package com.daromon.collective.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daromon.collective.ui.navigation.Screen
import com.daromon.collective.ui.state.CarUiState
import com.daromon.collective.viewmodel.CarViewModel

@Composable
fun CarListScreen(viewModel: CarViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is CarUiState.Loading -> CircularProgressIndicator()
        is CarUiState.Error -> Text("Error: ${currentState.message}")
        is CarUiState.Success -> {
            val cars = currentState.cars
            Column(modifier = Modifier.padding(16.dp)) {
                LazyColumn {
                    itemsIndexed(cars) { index, car ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    navController.navigate(Screen.CarDetail.passId(car.id))
                                }) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(car.model)
                                    Text("${car.year}")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    navController.navigate(Screen.AddCar.route)
                }) {
                    Text("Add Car")
                }
            }
        }
    }
}