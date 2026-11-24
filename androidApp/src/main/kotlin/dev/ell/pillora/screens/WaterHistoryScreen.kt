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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ell.pillora.water.WaterViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val WaterTeal = Color(0xFF16A085)
private val LightWaterBg = Color(0xFFF4F9F8)

@Composable
fun WaterHistoryScreen(
    viewModel: WaterViewModel,
    onBack: (() -> Unit)? = null,
) {
    val allEntries by viewModel.allWaterEntries.collectAsStateWithLifecycle()
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val groupedEntries = remember(allEntries) {
        allEntries.groupBy { it.dateKey }
    }

    Scaffold(
        containerColor = LightWaterBg,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Column {
                    Text(
                        text = "Water Intake History 💧",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF263238),
                    )
                    Text(
                        text = "Complete log of past hydration entries",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (groupedEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💧", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No water history found",
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                        )
                        Text(
                            text = "Recorded water logs will appear here.",
                            color = Color.Gray,
                            fontSize = 13.sp,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    groupedEntries.forEach { (dateKey, entriesForDate) ->
                        val dayTotal = entriesForDate.sumOf { it.amountMl }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = dateKey,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF263238),
                                        )

                                        Text(
                                            text = "$dayTotal ml",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = WaterTeal,
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    entriesForDate.forEach { entry ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .background(
                                                            Color(0xFFE1F1EC),
                                                            RoundedCornerShape(8.dp),
                                                        ),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text("💧", fontSize = 14.sp)
                                                }

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Column {
                                                    Text(
                                                        text = "${entry.amountMl} ml",
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 14.sp,
                                                    )
                                                    Text(
                                                        text = timeFormat.format(Date(entry.timestamp)),
                                                        fontSize = 11.sp,
                                                        color = Color.Gray,
                                                    )
                                                }
                                            }

                                            IconButton(
                                                onClick = { viewModel.deleteWater(entry) },
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete entry",
                                                    tint = Color(0xFFE53935),
                                                    modifier = Modifier.size(20.dp),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
