
package com.example.healthmate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            .background(Color(0xFFF6F8FC))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Screen Heading
        Text(
            text = "Health Records",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF173B67)
        )

        Text(
            text = "View and manage your daily health activities.",
            color = Color(0xFF64748B),
            style = MaterialTheme.typography.bodyMedium
        )

        // Search Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "🔍 Search Records",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF173B67)
                )

                OutlinedTextField(
                    value = searchDate,
                    onValueChange = { searchDate = it },
                    label = { Text("Search by Date") },
                    placeholder = {
                        Text("Example: 2026-11-26")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                TextButton(
                    onClick = { searchDate = "" }
                ) {
                    Text("Clear Search")
                }
            }
        }

        // Record Count
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEAF2FF)
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Total Health Records",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF173B67)
                )

                Text(
                    text = filteredRecords.size.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                )
            }
        }

        Text(
            text = "Your Records",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF173B67)
        )

        // Display Records
        if (filteredRecords.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📋",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "No Health Records Found",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF173B67)
                    )

                    Text(
                        text = "Add a health record or try another date.",
                        color = Color(0xFF64748B)
                    )
                }
            }

        } else {

            filteredRecords.forEach { record ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Record Date
                        Text(
                            text = "📅 ${record.date}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF173B67)
                        )

                        HorizontalDivider(
                            color = Color(0xFFE2E8F0)
                        )

                        // Steps
                        Text(
                            text = "👣 Steps: ${record.steps}",
                            color = Color(0xFF2563EB),
                            fontWeight = FontWeight.SemiBold
                        )

                        // Calories
                        Text(
                            text = "🔥 Calories: ${record.calories} kcal",
                            color = Color(0xFFDC4C4C),
                            fontWeight = FontWeight.SemiBold
                        )

                        // Water
                        Text(
                            text = "💧 Water: ${record.water} ml",
                            color = Color(0xFF0891B2),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Edit and Delete Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Button(
                                onClick = {
                                    onEdit(record)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2563EB)
                                )
                            ) {

                                Text("Edit")
                            }

                            OutlinedButton(
                                onClick = {
                                    onDelete(record.id)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Text(
                                    text = "Delete",
                                    color = Color(0xFFDC4C4C)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Back Button
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {

            Text("Back to Dashboard")
        }

        Text(
            text = "💚 Good health is the best Foundation!",
            color = Color(0xFF173B67),
            fontWeight = FontWeight.SemiBold
        )
    }
}
