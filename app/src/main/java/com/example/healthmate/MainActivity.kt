
package com.example.healthmate

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthmate.ui.theme.HealthMateTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthMateTheme {

                val databaseHelper = remember {
                    HealthDatabaseHelper(applicationContext)
                }

                var currentScreen by remember {
                    mutableStateOf("dashboard")
                }

                var records by remember {
                    mutableStateOf(databaseHelper.getAllRecords())
                }

                var selectedRecord by remember {
                    mutableStateOf<HealthRecord?>(null)
                }

                when (currentScreen) {

                    "add" -> {

                        AddRecord(
                            recordToEdit = selectedRecord,

                            onSave = { record ->

                                val isEditing = record.id != 0

                                val success = if (isEditing) {

                                    databaseHelper.updateRecord(record) > 0

                                } else {

                                    databaseHelper.insertRecord(record) != -1L
                                }

                                if (success) {

                                    records = databaseHelper.getAllRecords()

                                    Toast.makeText(
                                        this@MainActivity,
                                        if (isEditing)
                                            "Health record updated successfully!"
                                        else
                                            "Health record saved successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    selectedRecord = null

                                    currentScreen = if (isEditing) {
                                        "records"
                                    } else {
                                        "dashboard"
                                    }

                                } else {

                                    Toast.makeText(
                                        this@MainActivity,
                                        "Unable to save health record",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },

                            onCancel = {

                                val wasEditing = selectedRecord != null

                                selectedRecord = null

                                currentScreen = if (wasEditing) {
                                    "records"
                                } else {
                                    "dashboard"
                                }
                            }
                        )
                    }

                    "records" -> {

                        ViewRecords(
                            records = records,

                            onDelete = { id ->

                                databaseHelper.deleteRecord(id)

                                records = databaseHelper.getAllRecords()
                            },

                            onEdit = { record ->

                                selectedRecord = record

                                currentScreen = "add"
                            },

                            onBack = {

                                currentScreen = "dashboard"
                            }
                        )
                    }

                    else -> {

                        HealthMateDashboard(

                            onAddRecordClick = {

                                selectedRecord = null

                                currentScreen = "add"
                            },

                            onViewRecordsClick = {

                                records = databaseHelper.getAllRecords()

                                currentScreen = "records"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthMateDashboard(
    onAddRecordClick: () -> Unit,
    onViewRecordsClick: () -> Unit
) {

    val today = SimpleDateFormat(
        "dd MMMM yyyy",
        Locale.ENGLISH
    ).format(Date())

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "HealthMate",
                style = MaterialTheme.typography.headlineLarge
            )

            Text("Personal Health & Wellness Tracker")

            Text(
                text = "Today's Health Overview",
                style = MaterialTheme.typography.titleLarge
            )

            Text(today)

            HealthSummaryCard(
                title = "Steps Walked",
                value = "7,850",
                unit = "steps"
            )

            HealthSummaryCard(
                title = "Calories Burned",
                value = "420",
                unit = "kcal"
            )

            HealthSummaryCard(
                title = "Water Intake",
                value = "2,000",
                unit = "ml"
            )

            Button(
                onClick = onAddRecordClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Health Record")
            }

            OutlinedButton(
                onClick = onViewRecordsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Health Records")
            }

            Text(
                text = "Your Health Matters!",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Keep tracking your daily activities " +
                        "to maintain a healthy lifestyle."
            )
        }
    }
}

@Composable
fun HealthSummaryCard(
    title: String,
    value: String,
    unit: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$value $unit",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
