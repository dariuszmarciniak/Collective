package com.daromon.collective.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.daromon.collective.domain.model.ServiceRecord
import com.daromon.collective.viewmodel.ServiceRecordViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHistoryScreen(carId: Int, viewModel: ServiceRecordViewModel) {
    val records by viewModel.records.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editRecord by remember { mutableStateOf<ServiceRecord?>(null) }

    LaunchedEffect(carId) { viewModel.loadRecords(carId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Service History") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { showAddDialog = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add Service Record")
        }
    }) { padding ->
        LazyColumn(contentPadding = padding) {
            items(records) { record ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Date: ${record.date}")
                        Text("Description: ${record.description}")
                        Text("Cost: ${record.cost}")
                        Text("Type: ${record.type}")
                        Row {
                            Button(onClick = { editRecord = record }) {
                                Text("Edit")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.delete(record) },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
        if (showAddDialog) {
            ServiceRecordDialog(
                title = "Add service", initialRecord = ServiceRecord(
                    carId = carId, date = "", type = "", description = "", cost = 0.0
                ), onDismiss = { showAddDialog = false }, onConfirm = { record ->
                    viewModel.add(record)
                showAddDialog = false
                }, isEdit = false
            )
        }
        if (editRecord != null) {
            ServiceRecordDialog(
                title = "Edit record",
                initialRecord = editRecord!!,
                onDismiss = { editRecord = null },
                onConfirm = { record ->
                    viewModel.update(record)
                    editRecord = null
                },
                isEdit = true
            )
        }
    }
}

@Composable
fun ServiceRecordDialog(
    title: String,
    initialRecord: ServiceRecord,
    onDismiss: () -> Unit,
    onConfirm: (ServiceRecord) -> Unit,
    isEdit: Boolean
) {
    var date by remember { mutableStateOf(initialRecord.date) }
    var description by remember { mutableStateOf(initialRecord.description) }
    var cost by remember { mutableStateOf(if (initialRecord.cost == 0.0) "" else initialRecord.cost.toString()) }
    var type by remember { mutableStateOf(initialRecord.type) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = {
        ServiceRecordForm(
            date = date,
            onDateChange = { date = it; showError = false },
            type = type,
            onTypeChange = { type = it; showError = false },
            description = description,
            onDescriptionChange = { description = it; showError = false },
            cost = cost,
            onCostChange = { cost = it; showError = false },
            showError = showError
        )
    }, confirmButton = {
        Button(onClick = {
            val costValue = cost.toDoubleOrNull()
            if (date.isBlank() || type.isBlank() || description.isBlank() || costValue == null || costValue <= 0.0) {
                showError = true
            } else {
                onConfirm(
                    initialRecord.copy(
                        date = date, type = type, description = description, cost = costValue
                    )
                )
            }
        }) { Text(if (isEdit) "Save" else "Add") }
    }, dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } })
}

@Composable
fun ServiceRecordForm(
    date: String,
    onDateChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    cost: String,
    onCostChange: (String) -> Unit,
    showError: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        DatePickerField(
            value = date,
            onValueChange = onDateChange,
            label = "Date*",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text("Type*") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description*") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cost,
            onValueChange = onCostChange,
            label = { Text("Cost*") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "All fields are required, cost must be a positive number.",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, "Select date")
            }
        })

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        if (value.isNotEmpty()) {
            try {
                calendar.time = dateFormatter.parse(value) ?: Date()
            } catch (e: Exception) {
                calendar.time = Date()
            }
        }

        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            Button(onClick = {
                val selectedDate = dateFormatter.format(calendar.time)
                onValueChange(selectedDate)
                showDatePicker = false
            }) {
                Text("OK")
            }
        }, dismissButton = {
            Button(onClick = { showDatePicker = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = calendar.timeInMillis
                )
            )
        }
    }
}