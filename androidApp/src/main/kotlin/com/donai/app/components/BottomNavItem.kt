package com.donai.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavDestination(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    HOME("Home", Icons.Outlined.Home, Icons.Filled.Home),
    REQUESTS("Requests", Icons.Outlined.List, Icons.Filled.List),
    CREATE("Create", Icons.Outlined.AddCircle, Icons.Filled.AddCircle),
    HISTORY("History", Icons.Outlined.DateRange, Icons.Filled.DateRange),
    PROFILE("Profile", Icons.Outlined.Person, Icons.Filled.Person),
}

typealias BottomNavItem = BottomNavDestination