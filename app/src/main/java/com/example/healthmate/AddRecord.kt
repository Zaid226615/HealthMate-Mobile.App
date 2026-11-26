package com.example.healthmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddRecord(
    onSave: (HealthRecord) -> Unit,
    onCancel: () -> Unit,
    recordToEdit: HealthRecord? = null
) {

    var date by remember(recordToEdit?.id) {
        mutableStateOf(
            recordToEdit?.date
                ?: SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())
        )
    }

    var steps by remember(recordToEdit?.id) {
        mutableStateOf(recordToEdit?.steps?.toString() ?: "")
    }

    var calories by remember(recordToEdit?.id) {
        mutableStateOf(recordToEdit?.calories?.toString() ?: "")
    }

    var water by remember(recordToEdit?.id) {
        mutableStateOf(recordToEdit?.water?.toString() ?: "")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = if (recordToEdit == null)
                "Add Health Record"
            else
                "Update Health Record",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = steps,
            onValueChange = { steps = it },
            label = { Text("Steps Walked") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = water,
            onValueChange = { water = it },
            label = { Text("Water Intake (ml)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                val stepValue = steps.toIntOrNull()
                val calorieValue = calories.toIntOrNull()
                val waterValue = water.toIntOrNull()

                if (!date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    errorMessage = "Enter date as yyyy-MM-dd"

                } else if (
                    stepValue == null || stepValue < 0 ||
                    calorieValue == null || calorieValue < 0 ||
                    waterValue == null || waterValue < 0
                ) {
                    errorMessage = "Enter valid non-negative numbers"

                } else {
                    onSave(
                        HealthRecord(
                            id = recordToEdit?.id ?: 0,
                            date = date,
                            steps = stepValue,
                            calories = calorieValue,
                            water = waterValue
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (recordToEdit == null)
                    "Save Record"
                else
                    "Update Record"
            )
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}