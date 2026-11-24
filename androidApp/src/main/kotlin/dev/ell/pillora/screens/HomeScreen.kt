package dev.ell.pillora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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

private val Mint = Color(0xFF269B72)
private val LightMint = Color(0xFFE5F5EC)
private val Lavender = Color(0xFFF0ECFA)

private enum class DoseStatus {
    PENDING, TAKEN, SKIPPED
}

private data class HomeMedicine(
    val id: Int,
    val name: String,
    val dosage: String,
    val time: String,
    val status: DoseStatus = DoseStatus.PENDING,
)

@Composable
fun HomeScreen() {

    var medicines by remember {
        mutableStateOf(
            listOf(
                HomeMedicine(
                    1, "Vitamin D", "1 Tablet", "08:00 AM",
                    DoseStatus.TAKEN,
                ),
                HomeMedicine(2, "Vitamin C", "1 Tablet", "02:00 PM"),
                HomeMedicine(3, "Calcium", "1 Tablet", "08:00 PM"),
            ),
        )
    }

    fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance()
            .get(java.util.Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Good Morning ☀️"
            in 12..16 -> "Good Afternoon 🌤️"
            else -> "Good Evening 🌙"
        }
    }


    val taken = medicines.count { it.status == DoseStatus.TAKEN }
    val total = medicines.size
    val progress = if (total > 0) taken.toFloat() / total else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {

        Text(
            text = "PILLORA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Mint,
            letterSpacing = 2.sp,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = getGreeting(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Text(
            text = "Track your daily medicine schedule",
            fontSize = 14.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LightMint),
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                Text(
                    text = "TODAY'S PROGRESS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Mint,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$taken of $total doses marked taken",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Mint,
                    trackColor = Color.White,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${(progress * 100).toInt()}% Completed",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Mint,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Next Dose",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Lavender),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("⏰", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Vitamin C",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "1 Tablet · After Food",
                        color = Color.Gray,
                        fontSize = 13.sp,
                    )
                }

                Text(
                    text = "02:00 PM",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6750A4),
                    fontSize = 14.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Today's Schedule",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238),
            )

            Text(
                text = "$total Medicines",
                fontSize = 13.sp,
                color = Color.Gray,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        medicines.forEach { medicine ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                when (medicine.status) {
                                    DoseStatus.TAKEN -> LightMint
                                    DoseStatus.SKIPPED -> Color(0xFFFFF0F0)
                                    DoseStatus.PENDING -> Color(0xFFF0F4F8)
                                },
                                RoundedCornerShape(12.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = when (medicine.status) {
                                DoseStatus.TAKEN -> "✓"
                                DoseStatus.SKIPPED -> "✕"
                                DoseStatus.PENDING -> "💊"
                            },
                            fontSize = 18.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = medicine.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                        Text(
                            text = "${medicine.dosage} · ${medicine.time}",
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                    }

                    when (medicine.status) {

                        DoseStatus.PENDING -> {
                            Row {

                                IconButton(
                                    onClick = {
                                        medicines = medicines.map {
                                            if (it.id == medicine.id) {
                                                it.copy(status = DoseStatus.TAKEN)
                                            } else {
                                                it
                                            }
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Take",
                                        tint = Mint,
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        medicines = medicines.map {
                                            if (it.id == medicine.id) {
                                                it.copy(status = DoseStatus.SKIPPED)
                                            } else {
                                                it
                                            }
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Skip",
                                        tint = Color.Red,
                                    )
                                }
                            }
                        }

                        DoseStatus.TAKEN -> {
                            Text(
                                text = "Taken",
                                color = Mint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                            )
                        }

                        DoseStatus.SKIPPED -> {
                            Text(
                                text = "Skipped",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
