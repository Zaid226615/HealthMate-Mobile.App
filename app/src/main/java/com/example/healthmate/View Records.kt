
package com.example.healthmate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ViewRecords(
    records: List<HealthRecord>,
    onDelete: (Int) -> Unit,
    onEdit: (HealthRecord) -> Unit,
    onBack: () -> Unit
) {

    var searchDate by remember {
        mutableStateOf("")
    }

    val filteredRecords = records.filter {
        it.date.contains(searchDate.trim(), ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Health Records",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = searchDate,
            onValueChange = { searchDate = it },
            label = { Text("Search by Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Total Records: ${filteredRecords.size}")

        if (filteredRecords.isEmpty()) {

            Text("No health records found.")

        } else {

            filteredRecords.forEach { record ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "Date: ${record.date}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Steps: ${record.steps}")
                        Text("Calories: ${record.calories} kcal")
                        Text("Water: ${record.water} ml")

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = { onEdit(record) }
                            ) {
                                Text("Edit")
                            }

                            OutlinedButton(
                                onClick = { onDelete(record.id) }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Dashboard")
        }
    }
}
