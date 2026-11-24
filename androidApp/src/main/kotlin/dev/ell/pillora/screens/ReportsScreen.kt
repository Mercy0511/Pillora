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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ell.pillora.data.PilloraDatabase
import dev.ell.pillora.utils.ReportExporter

private val ReportMint = Color(0xFF35B89A)
private val ReportBackground = Color(0xFFF7F8FC)
private val ReportPurple = Color(0xFF8D80D8)

private data class DayReport(
    val day: String,
    val taken: Int,
    val total: Int,
)

@Composable
fun ReportsScreen() {
    val context = LocalContext.current
    val database = remember(context) { PilloraDatabase.getDatabase(context) }
    val activeMedicines by database.medicineDao().getActiveMedicines().collectAsStateWithLifecycle(initialValue = emptyList())
    val waterEntries by database.waterDao().getAllWaterEntries().collectAsStateWithLifecycle(initialValue = emptyList())

    val taken = 42
    val skipped = 5
    val pending = 3
    val missed = 4
    val total = taken + skipped + pending + missed

    val adherence = if ((total - pending) > 0) {
        taken.toFloat() / (total - pending)
    } else {
        0f
    }

    val weeklyData = listOf(
        DayReport("Mon", 5, 6),
        DayReport("Tue", 6, 6),
        DayReport("Wed", 4, 5),
        DayReport("Thu", 5, 6),
        DayReport("Fri", 6, 6),
        DayReport("Sat", 4, 6),
        DayReport("Sun", 5, 5),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ReportBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Analytics & Reports 📊",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238),
                )

                Text(
                    text = "Adherence summary and dose history insights",
                    fontSize = 13.sp,
                    color = Color.Gray,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                ReportExporter.shareReport(context, activeMedicines, waterEntries)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ReportMint),
        ) {
            Text(
                text = "Export Doctor Report 📄",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "WEEKLY ADHERENCE SCORE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${(adherence * 100).toInt()}%",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReportMint,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Overall Health Progress",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { adherence },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = ReportMint,
                    trackColor = Color(0xFFE8EDF0),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ReportStatCard("Taken", "$taken", Color(0xFFE2F6ED), Color(0xFF23845F), Modifier.weight(1f))
            ReportStatCard("Skipped", "$skipped", Color(0xFFFFF0DD), Color(0xFFB87822), Modifier.weight(1f))
            ReportStatCard("Missed", "$missed", Color(0xFFFFE6E8), Color(0xFFC74755), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Weekly Activity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    weeklyData.forEach { item ->
                        val barHeight = if (item.total > 0) {
                            (item.taken.toFloat() / item.total * 100).dp
                        } else {
                            0.dp
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${item.taken}",
                                fontSize = 10.sp,
                                color = Color.Gray,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(110.dp)
                                    .background(
                                        Color(0xFFE8EDF0),
                                        RoundedCornerShape(8.dp),
                                    ),
                                contentAlignment = Alignment.BottomCenter,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(barHeight)
                                        .background(
                                            ReportPurple,
                                            RoundedCornerShape(8.dp),
                                        ),
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = item.day,
                                fontSize = 11.sp,
                                color = Color.DarkGray,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Smart Recommendation 💡",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF263238),
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You take medicines more consistently in the morning (92%) compared to evening (78%). Try setting an alarm for evening doses.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
private fun ReportStatCard(
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(title, fontSize = 11.sp, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )
        }
    }
}
