
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment

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

                val todayDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

                val todayRecords = records.filter {
                    it.date == todayDate
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

                            },

                            todaySteps = todayRecords.sumOf { it.steps },

                            todayCalories = todayRecords.sumOf { it.calories },

                            todayWater = todayRecords.sumOf { it.water }
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
    onViewRecordsClick: () -> Unit,
    todaySteps: Int,
    todayCalories: Int,
    todayWater: Int
) {

    val today = SimpleDateFormat(
        "dd MMMM yyyy",
        Locale.ENGLISH
    ).format(Date())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF6F8FC)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // App Header
            Text(
                text = "💚 HealthMate",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF173B67)
            )

            Text(
                text = "Personal Health & Wellness Tracker",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )

            // Date Overview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEAF2FF)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "TODAY'S OVERVIEW",
                        color = Color(0xFF2563EB),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = today,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1E3A5F)
                    )

                    Text(
                        text = "Track your daily activities and stay healthy.",
                        color = Color(0xFF475569)
                    )
                }
            }

            Text(
                text = "Your Daily Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Steps Card
            HealthSummaryCard(
                title = "Steps Walked",
                value = todaySteps.toString(),
                unit = "steps",
                icon = "👣",
                cardColor = Color(0xFFE9F2FF),
                valueColor = Color(0xFF2563EB)
            )

            // Calories Card
            HealthSummaryCard(
                title = "Calories Burned",
                value = todayCalories.toString(),
                unit = "kcal",
                icon = "🔥",
                cardColor = Color(0xFFFDECEC),
                valueColor = Color(0xFF945151)
            )

            // Water Card
            HealthSummaryCard(
                title = "Water Intake",
                value = todayWater.toString(),
                unit = "ml",
                icon = "💧",
                cardColor = Color(0xFFE5F7FC),
                valueColor = Color(0xFF0891B2)
            )

            // Add Record Button
            Button(
                onClick = onAddRecordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text(
                    text = "＋ Add Health Record",
                    fontWeight = FontWeight.Bold
                )
            }

            // View Records Button
            OutlinedButton(
                onClick = onViewRecordsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = "View Health Records",
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "💚 Your Health Matters!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF173B67)
            )

            Text(
                text = "Keep tracking your daily activities " +
                        "to maintain a healthy lifestyle.",
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun HealthSummaryCard(
    title: String,
    value: String,
    unit: String,
    icon: String,
    cardColor: Color,
    valueColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = icon,
                style = MaterialTheme.typography.headlineLarge
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = valueColor
                    )

                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

