
package com.example.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthmate.ui.theme.HealthMateTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthMateTheme {
                HealthMateDashboard()
            }
        }
    }
}

@Composable
fun HealthMateDashboard() {

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

            Text(
                text = "Personal Health & Wellness Tracker",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Today's Health Overview",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = today,
                style = MaterialTheme.typography.bodyMedium
            )

            HealthSummaryCard(
                title = "Steps Walked",
                value = "7,850",
                unit = "steps",
                description = "Daily walking activity"
            )

            HealthSummaryCard(
                title = "Calories Burned",
                value = "420",
                unit = "kcal",
                description = "Daily calories burned"
            )

            HealthSummaryCard(
                title = "Water Intake",
                value = "2,000",
                unit = "ml",
                description = "Daily water consumption"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Health Matters!",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Keep tracking your daily activities " +
                        "to maintain a healthy lifestyle.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HealthSummaryCard(
    title: String,
    value: String,
    unit: String,
    description: String
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

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthMatePreview() {
    HealthMateTheme {
        HealthMateDashboard()
    }
}
