package dev.ell.pillora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val HistoryGreen = Color(0xFF269B72)

private enum class HistoryStatus {
    TAKEN, SKIPPED, PENDING, MISSED
}

private data class HistoryItem(
    val id: Int,
    val medicine: String,
    val dosage: String,
    val scheduledTime: String,
    val actionTime: String,
    val date: LocalDate,
    val status: HistoryStatus,
    val note: String,
)

@Composable
fun HistoryScreen() {

    val today = LocalDate.now()

    var selectedDate by remember {
        mutableStateOf(today)
    }

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val history = remember {
        listOf(
            HistoryItem(
                1, "Vitamin D", "1 Tablet", "08:00 AM", "08:05 AM",
                today, HistoryStatus.TAKEN, "Taken with breakfast",
            ),
            HistoryItem(
                2, "Vitamin C", "1 Tablet", "02:00 PM", "02:30 PM",
                today, HistoryStatus.TAKEN, "Slight delay",
            ),
            HistoryItem(
                3, "Calcium", "1 Tablet", "08:00 PM", "--",
                today, HistoryStatus.SKIPPED, "Felt unwell",
            ),
            HistoryItem(
                4, "Paracetamol", "500 mg", "08:00 AM", "08:10 AM",
                today.minusDays(1), HistoryStatus.TAKEN, "On time",
            ),
            HistoryItem(
                5, "Cough Syrup", "10 ml", "10:00 PM", "--",
                today.minusDays(1), HistoryStatus.MISSED, "Forgot dose",
            ),
        )
    }

    val dateRecords = history.filter { it.date == selectedDate }

    val filteredRecords = dateRecords.filter {
        val matchesFilter = if (selectedFilter == "All") {
            true
        } else {
            it.status.name.equals(selectedFilter, ignoreCase = true)
        }
        val matchesSearch = it.medicine.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    val taken = dateRecords.count { it.status == HistoryStatus.TAKEN }
    val skipped = dateRecords.count { it.status == HistoryStatus.SKIPPED }
    val missed = dateRecords.count { it.status == HistoryStatus.MISSED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(16.dp),
    ) {

        Text(
            text = "Dose History",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Text(
            text = "Track logs, skipped doses and past adherence",
            fontSize = 13.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Button(
                onClick = { selectedDate = selectedDate.minusDays(1) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("‹ Prev", color = Color.DarkGray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = HistoryGreen,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedDate == today) {
                        "Today (${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM"))})"
                    } else {
                        selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF263238),
                )
            }

            Button(
                onClick = {
                    if (selectedDate.isBefore(today)) {
                        selectedDate = selectedDate.plusDays(1)
                    }
                },
                enabled = selectedDate.isBefore(today),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Next ›", color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HistorySummary("Taken", taken, Color(0xFF269B72), Modifier.weight(1f))
            HistorySummary("Skipped", skipped, Color(0xFFE29578), Modifier.weight(1f))
            HistorySummary("Missed", missed, Color(0xFFE53935), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search logs...") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                )
            },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val filters = listOf("All", "Taken", "Skipped", "Missed")

            filters.forEach { filter ->
                val isSelected = selectedFilter == filter

                Button(
                    onClick = { selectedFilter = filter },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) HistoryGreen else Color.White,
                        contentColor = if (isSelected) Color.White else Color.DarkGray,
                    ),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                ) {
                    Text(filter, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📜", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No history records",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                    )
                    Text(
                        "No dose records match your selected date and filter.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(filteredRecords) { item ->
                    HistoryCardItem(item)
                }
            }
        }
    }
}

@Composable
private fun HistorySummary(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(
                text = "$count",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}

@Composable
private fun HistoryCardItem(item: HistoryItem) {
    val statusColor = when (item.status) {
        HistoryStatus.TAKEN -> Color(0xFF269B72)
        HistoryStatus.SKIPPED -> Color(0xFFE29578)
        HistoryStatus.MISSED -> Color(0xFFE53935)
        HistoryStatus.PENDING -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
            ) {
                Text(
                    text = when (item.status) {
                        HistoryStatus.TAKEN -> "✓"
                        HistoryStatus.SKIPPED -> "✕"
                        HistoryStatus.MISSED -> "!"
                        HistoryStatus.PENDING -> "⌛"
                    },
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.medicine,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )

                Text(
                    text = "${item.dosage} · Scheduled: ${item.scheduledTime}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                )

                if (item.note.isNotBlank()) {
                    Text(
                        text = "Note: ${item.note}",
                        color = Color.DarkGray,
                        fontSize = 11.sp,
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.status.name,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )

                Text(
                    text = item.actionTime,
                    color = Color.Gray,
                    fontSize = 11.sp,
                )
            }
        }
    }
}
