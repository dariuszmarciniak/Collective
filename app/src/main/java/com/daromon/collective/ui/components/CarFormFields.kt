package com.daromon.collective.ui.components

import DatePickerField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.daromon.collective.R
import com.daromon.collective.domain.model.FuelType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarFormFields(
    state: CarFormState,
    onFieldChange: (String, String) -> Unit,
    onFuelTypeChange: (FuelType?) -> Unit,
    showFuelTypeMenu: Boolean,
    onShowFuelTypeMenuChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = state.brand,
        onValueChange = { onFieldChange("brand", it) },
        label = { Text(stringResource(R.string.brand)) },
        placeholder = { Text(stringResource(R.string.brand_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        isError = state.errors.containsKey("brand"),
        supportingText = {
            state.errors["brand"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    OutlinedTextField(
        value = state.model,
        onValueChange = { onFieldChange("model", it) },
        label = { Text(stringResource(R.string.model)) },
        placeholder = { Text(stringResource(R.string.model_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        isError = state.errors.containsKey("model"),
        supportingText = {
            state.errors["model"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    OutlinedTextField(
        value = state.year,
        onValueChange = {
            if (it.length <= 4) onFieldChange(
                "year",
                it.filter { c -> c.isDigit() })
        },
        label = { Text(stringResource(R.string.year)) },
        placeholder = { Text(stringResource(R.string.year_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.errors.containsKey("year"),
        supportingText = {
            state.errors["year"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    OutlinedTextField(
        value = state.vin,
        onValueChange = { onFieldChange("vin", it) },
        label = { Text(stringResource(R.string.vin)) },
        placeholder = { Text(stringResource(R.string.vin_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.registrationNumber,
        onValueChange = { onFieldChange("registrationNumber", it) },
        label = { Text(stringResource(R.string.registration_number)) },
        placeholder = { Text(stringResource(R.string.registration_number_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.mileage,
        onValueChange = { onFieldChange("mileage", it.filter { c -> c.isDigit() }) },
        label = { Text(stringResource(R.string.mileage)) },
        placeholder = { Text(stringResource(R.string.mileage_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.errors.containsKey("mileage"),
        supportingText = {
            state.errors["mileage"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    ExposedDropdownMenuBox(
        expanded = showFuelTypeMenu,
        onExpandedChange = { onShowFuelTypeMenuChange(!showFuelTypeMenu) }) {
        OutlinedTextField(
            value = state.fuelType?.let {
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
            modifier = modifier.fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showFuelTypeMenu) })
        ExposedDropdownMenu(
            expanded = showFuelTypeMenu, onDismissRequest = { onShowFuelTypeMenuChange(false) }) {
            FuelType.entries.forEach { type ->
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
                    onFuelTypeChange(type)
                    onShowFuelTypeMenuChange(false)
                })
            }
        }
    }
    OutlinedTextField(
        value = state.engineCapacity,
        onValueChange = { onFieldChange("engineCapacity", it) },
        label = { Text(stringResource(R.string.engine_capacity)) },
        placeholder = { Text(stringResource(R.string.engine_capacity_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.errors.containsKey("engineCapacity"),
        supportingText = {
            state.errors["engineCapacity"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    OutlinedTextField(
        value = state.power,
        onValueChange = { onFieldChange("power", it) },
        label = { Text(stringResource(R.string.power)) },
        placeholder = { Text(stringResource(R.string.power_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = state.errors.containsKey("power"),
        supportingText = {
            state.errors["power"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        })
    OutlinedTextField(
        value = state.color,
        onValueChange = { onFieldChange("color", it) },
        label = { Text(stringResource(R.string.color)) },
        placeholder = { Text(stringResource(R.string.color_placeholder)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.notes,
        onValueChange = { onFieldChange("notes", it) },
        label = { Text(stringResource(R.string.notes)) },
        placeholder = { Text(stringResource(R.string.notes_placeholder)) },
        modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    DatePickerField(
        label = stringResource(R.string.inspection_date),
        value = state.inspectionDate,
        onValueChange = { onFieldChange("inspectionDate", it) },
        modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    DatePickerField(
        label = stringResource(R.string.insurance_expiry),
        value = state.insuranceExpiry,
        onValueChange = { onFieldChange("insuranceExpiry", it) },
        modifier = modifier.fillMaxWidth()
    )
}
