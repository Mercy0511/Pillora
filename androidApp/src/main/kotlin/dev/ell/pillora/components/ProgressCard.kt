package dev.ell.pillora.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressCard(
    title: String,
    completed: Int,
    total: Int,
) {
    val progress = if (total > 0) {
        (completed.toFloat() / total).coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "$completed / $total doses completed",
                color = Color.Gray,
                fontSize = 13.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp),
                color = Color(0xFF35B89A),
                trackColor = Color(0xFFE8EDF0),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "${(progress * 100).toInt()}% completed",
                color = Color(0xFF35B89A),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
        }
    }
}
