package com.daromon.collective.ui.features.person

import DatePickerField
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daromon.collective.R
import com.daromon.collective.domain.model.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    personId: Int, navController: NavController, viewModel: PersonViewModel
) {
    LocalContext.current
    val isNew = personId == 0
    val person by viewModel.selectedPerson.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showImageDialog by remember { mutableStateOf(false) }
    var dateOfBirth by remember { mutableStateOf("") }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

    LaunchedEffect(personId) {
        if (!isNew) {
            viewModel.loadPerson(personId)
        }
    }

    LaunchedEffect(person) {
        person?.let {
            firstName = it.firstName
            lastName = it.lastName
            photoUri = it.photoUri
            phone = it.phone.orEmpty()
            email = it.email.orEmpty()
            address = it.address.orEmpty()
            note = it.note.orEmpty()
            dateOfBirth = it.dateOfBirth.orEmpty()
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(if (isNew) stringResource(R.string.add) else stringResource(R.string.edit)) },
            navigationIcon = {
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
                    val newPerson = Person(
                        id = if (isNew) 0 else personId,
                        firstName = firstName,
                        lastName = lastName,
                        photoUri = photoUri,
                        phone = phone.ifBlank { null },
                        email = email.ifBlank { null },
                        address = address.ifBlank { null },
                        note = note.ifBlank { null },
                        dateOfBirth = dateOfBirth
                    )
                    if (isNew) viewModel.add(newPerson) else viewModel.update(newPerson)
                    navController.popBackStack()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        }
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
                    contentDescription = stringResource(R.string.select_photo),
                    modifier = Modifier
                        .size(96.dp)
                        .clickable { showImageDialog = true })
            }
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.select_photo))
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
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.first_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.last_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            DatePickerField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = stringResource(R.string.date_of_birth),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(R.string.phone)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.address)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.note)) },
                singleLine = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}