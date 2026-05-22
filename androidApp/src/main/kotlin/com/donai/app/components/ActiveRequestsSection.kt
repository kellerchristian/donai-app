package com.donai.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.donai.app.screens.dashboard.BloodRequest
import kotlin.collections.forEach

@Composable
fun ActiveRequestsSection(
    requests: List<BloodRequest>,
    onSeeAllClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeader(
            title = "Active Requests Near You",
            actionLabel = "See all",
            onActionClick = onSeeAllClick,
        )

        if (requests.isEmpty()) {
            EmptyRequestsPlaceholder()
        } else {
            requests.forEach { request ->
                BloodRequestItem(
                    request = request,
                    onClick = { onRequestClick(request.id) },
                )
            }
        }
    }
}