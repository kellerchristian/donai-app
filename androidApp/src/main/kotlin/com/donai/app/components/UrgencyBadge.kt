package com.donai.app.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.screens.dashboard.RequestUrgency
import com.donai.app.theme.EligibleGreenDark
import com.donai.app.theme.HighOrange
import com.donai.app.theme.MediumYellow
import com.donai.app.theme.UrgentRed

@Composable
fun UrgencyBadge(
    urgency: RequestUrgency,
    modifier: Modifier = Modifier,
) {
    val (label, color) = when (urgency) {
        RequestUrgency.URGENT -> "URGENT" to UrgentRed
        RequestUrgency.HIGH   -> "HIGH"   to HighOrange
        RequestUrgency.MEDIUM -> "MEDIUM" to MediumYellow
        RequestUrgency.LOW    -> "LOW"    to EligibleGreenDark
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            letterSpacing = 0.5.sp,
        )
    }
}