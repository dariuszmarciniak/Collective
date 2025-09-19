import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.daromon.collective.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
                Icon(Icons.Default.DateRange, stringResource(R.string.date))
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
                Text(stringResource(R.string.ok))
            }
        }, dismissButton = {
            Button(onClick = { showDatePicker = false }) {
                Text(stringResource(R.string.cancel))
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