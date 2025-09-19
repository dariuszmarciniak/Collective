package com.daromon.collective.ui.features.car

import DatePickerField
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.daromon.collective.R
import com.daromon.collective.domain.model.ServiceRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHistoryScreen(
    carId: Int, viewModel: ServiceRecordViewModel, openDrawer: () -> Unit
) {
    val records by viewModel.records.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editRecord by remember { mutableStateOf<ServiceRecord?>(null) }

    LaunchedEffect(carId) { viewModel.loadRecords(carId) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.service_history)) }, navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { showAddDialog = true }) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_service))
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
                        Text("${stringResource(R.string.date)} ${record.date}")
                        Text("${stringResource(R.string.description)} ${record.description}")
                        Text("${stringResource(R.string.cost)} ${record.cost}")
                        Text("${stringResource(R.string.type)} ${record.type}")
                        Row {
                            Button(onClick = { editRecord = record }) {
                                Text(stringResource(R.string.edit))
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.delete(record) },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                            ) {
                                Text(stringResource(R.string.delete))
                            }
                        }
                    }
                }
            }
        }
        if (showAddDialog) {
            ServiceRecordDialog(
                title = stringResource(R.string.add_service),
                initialRecord = ServiceRecord(
                    carId = carId, date = "", type = "", description = "", cost = 0.0
                ),
                onDismiss = { showAddDialog = false },
                onConfirm = { record ->
                    viewModel.add(record)
                    showAddDialog = false
                },
                isEdit = false
            )
        }
        if (editRecord != null) {
            ServiceRecordDialog(
                title = stringResource(R.string.edit_record),
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
        }) { Text(if (isEdit) stringResource(R.string.save) else stringResource(R.string.add)) }
    }, dismissButton = { Button(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } })
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
            label = stringResource(R.string.date),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text(stringResource(R.string.type)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cost,
            onValueChange = onCostChange,
            label = { Text(stringResource(R.string.cost)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.all_fields_required),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
