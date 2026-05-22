package com.donai.app.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class EligibilityBadgeData(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector,
    val label: String,
)