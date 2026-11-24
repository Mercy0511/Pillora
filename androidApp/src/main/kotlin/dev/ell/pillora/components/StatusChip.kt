package dev.ell.pillora.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusChip(status: String) {

    val backgroundColor = when (status.lowercase()) {
        "taken" -> Color(0xFFE2F6ED)
        "skipped" -> Color(0xFFFFF0DD)
        "missed" -> Color(0xFFFFE6E8)
        "pending" -> Color(0xFFECE9FA)
        else -> Color(0xFFECEFF1)
    }

    val textColor = when (status.lowercase()) {
        "taken" -> Color(0xFF23845F)
        "skipped" -> Color(0xFFB87822)
        "missed" -> Color(0xFFC74755)
        "pending" -> Color(0xFF7564B8)
        else -> Color.DarkGray
    }

    Box(
        modifier = Modifier
            .background(
                backgroundColor,
                RoundedCornerShape(50.dp),
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            color = textColor,
            fontSize = 11.sp,
        )
    }
}
