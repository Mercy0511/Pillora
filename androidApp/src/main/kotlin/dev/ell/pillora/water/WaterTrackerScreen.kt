package dev.ell.pillora.water

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ell.pillora.screens.WaterHistoryScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val MintGreen = Color(0xFF16A085)
private val LightMintBg = Color(0xFFF4F9F8)
private val CardBg = Color.White

@Composable
fun WaterTrackerScreen(
    viewModel: WaterViewModel,
) {
    val intake by viewModel.todayWaterIntake.collectAsStateWithLifecycle()
    val entries by viewModel.todayWaterEntries.collectAsStateWithLifecycle()

    var showCustomDialog by remember { mutableStateOf(value = false) }
    var showFullHistoryScreen by remember { mutableStateOf(value = false) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val goal = viewModel.dailyGoal
    val progress = (intake.toFloat() / goal).coerceIn(0f, 1f)
    val remaining = (goal - intake).coerceAtLeast(0)

    if (showFullHistoryScreen) {
        WaterHistoryScreen(
            viewModel = viewModel,
            onBack = { showFullHistoryScreen = false },
        )
        return
    }

    Scaffold(
        containerColor = LightMintBg,
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "Water Tracker 💧",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF263238),
                        )
                        Text(
                            text = "Track your daily hydration intake",
                            fontSize = 13.sp,
                            color = Color.Gray,
                        )
                    }

                    IconButton(
                        onClick = { showFullHistoryScreen = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Full Water History",
                            tint = MintGreen,
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "TODAY'S HYDRATION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(180.dp),
                        ) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 14.dp,
                                color = Color(0xFFE1F1EC),
                            )

                            CircularProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 14.dp,
                                color = MintGreen,
                                trackColor = Color.Transparent,
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "$intake ml",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238),
                                )

                                Text(
                                    text = "Goal: $goal ml",
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    color = MintGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = if (remaining == 0) {
                                "Daily goal completed! 🎉"
                            } else {
                                "$remaining ml remaining for today"
                            },
                            color = MintGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        )
                    }
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Add Water Intake",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF263238),
                        )

                        TextButton(onClick = { showCustomDialog = true }) {
                            Text("+ Custom", color = MintGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf(
                            150 to "Glass 🥛",
                            250 to "Cup 🍵",
                            350 to "Bottle 🍾",
                            500 to "Large 💧",
                        ).forEach { (amount, label) ->
                            Button(
                                onClick = { viewModel.addWater(amount) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MintGreen),
                                shape = RoundedCornerShape(14.dp),
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "+$amount ml", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text(text = label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.9f), maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F6F3)),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("💡", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Hydration Tip",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20),
                            )
                            Text(
                                text = "Drinking a glass of water right after waking up helps jumpstart your metabolism and energy.",
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Today's Log",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF263238),
                    )

                    Text(
                        text = "${entries.size} entries",
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                }
            }

            if (entries.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("💧", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No water logged today",
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                            )
                            Text(
                                text = "Tap any quick add button above to log your water.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                            )
                        }
                    }
                }
            } else {
                items(
                    items = entries,
                    key = { it.id },
                ) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFFE1F1EC), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("💧", fontSize = 16.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "${entry.amountMl} ml",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
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

    if (showCustomDialog) {
        CustomWaterDialog(
            onDismiss = { showCustomDialog = false },
            onAdd = { amount ->
                viewModel.addWater(amount)
                showCustomDialog = false
            },
        )
    }
}

@Composable
private fun CustomWaterDialog(
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit,
) {
    var amountText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(value = false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Custom Water Amount 💧") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        error = false
                    },
                    label = { Text("Amount in ml (e.g. 300)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                if (error) {
                    Text("Enter a valid water amount in ml.", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toIntOrNull()
                    if (amount == null || amount <= 0) {
                        error = true
                    } else {
                        onAdd(amount)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintGreen),
            ) {
                Text("Add Water")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
