package com.donai.app.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * MainScaffold is the UI shell for primary Tab screens.
 * It provides the functional [DonAIBottomBar] and an optional topBar slot.
 */
@Composable
fun MainScaffold(
    selectedBottomItem: BottomNavItem,
    onBottomItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topBar,
        bottomBar = {
            DonAIBottomBar(
                selectedItem = selectedBottomItem,
                onItemSelected = onBottomItemSelected,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
        content = content,
    )
}
