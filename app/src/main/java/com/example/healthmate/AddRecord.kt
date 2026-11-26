
package com.example.healthmate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding

@Composable
fun AddRecord(
    onSave: (HealthRecord) -> Unit,
    onCancel: () -> Unit,
    recordToEdit: HealthRecord? = null
) {

    var date by remember(recordToEdit?.id) {
        mutableStateOf(
            recordToEdit?.date ?: SimpleDateFormat(
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

    val blueColor = Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Screen Heading
        Text(
            text = if (recordToEdit == null)
                "Add Health Record"
            else
                "Update Health Record",

            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF173B67)
        )

        Text(
            text = if (recordToEdit == null)
                "Record your daily health activities."
            else
                "Update your health activity details.",

            color = Color(0xFF64748B),
            style = MaterialTheme.typography.bodyMedium
        )

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Daily Health Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF223C52)
                )

                // Date Field
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (yyyy-MM-dd)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Steps Field
                OutlinedTextField(
                    value = steps,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            steps = it
                        }
                    },
                    label = { Text("👣 Steps Walked") },
                    placeholder = { Text("Enter steps") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Calories Field
                OutlinedTextField(
                    value = calories,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            calories = it
                        }
                    },
                    label = { Text("🔥 Calories Burned (kcal)") },
                    placeholder = { Text("Enter calories") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Water Field
                OutlinedTextField(
                    value = water,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            water = it
                        }
                    },
                    label = { Text("💧 Water Intake (ml)") },
                    placeholder = { Text("Enter water amount") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Error Message
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Save / Update Button
                Button(
                    onClick = {

                        val stepValue = steps.toIntOrNull()
                        val calorieValue = calories.toIntOrNull()
                        val waterValue = water.toIntOrNull()

                        if (!date.matches(
                                Regex("\\d{4}-\\d{2}-\\d{2}")
                            )
                        ) {
                            errorMessage =
                                "Enter date as yyyy-MM-dd"

                        } else if (
                            stepValue == null ||
                            calorieValue == null ||
                            waterValue == null
                        ) {
                            errorMessage =
                                "Please fill in all fields correctly."

                        } else {

                            errorMessage = ""

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

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    shape = RoundedCornerShape(15.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = blueColor
                    )
                ) {

                    Text(
                        text = if (recordToEdit == null)
                            "Save Health Record"
                        else
                            "Update Health Record",

                        fontWeight = FontWeight.Bold
                    )
                }

                // Cancel Button
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),

                    shape = RoundedCornerShape(15.dp)
                ) {

                    Text("Cancel")
                }
            }
        }

        Text(
            text = "💚 Your health matters!",
            color = Color(0xFF173B67),
            fontWeight = FontWeight.SemiBold
        )
    }
}
