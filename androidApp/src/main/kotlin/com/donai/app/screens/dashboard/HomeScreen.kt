package com.donai.app.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donai.app.components.*
import com.donai.app.theme.DonAITheme


data class BloodRequest(
    val id: String,
    val requesterName: String,
    val hospital: String,
    val distanceMiles: Double,
    val bloodType: String,
    val urgency: RequestUrgency,
)

enum class RequestUrgency { URGENT, HIGH, MEDIUM, LOW }

/**
 * HomeContent renders the main dashboard.
 * It does NOT contain a Scaffold; it relies on the caller (MainScaffold) to provide
 * the layout shell and padding.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNotificationClick: () -> Unit,
    onSeeAllRequestsClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Spacer(Modifier.height(8.dp))

        DonorProfileCard(
            isEligible = uiState.isEligible,
            lastDonationDaysAgo = uiState.lastDonationDaysAgo,
            avatarUrl = uiState.avatarUrl,
        )

        HealthEligibilityCard(
            readinessPercent = uiState.healthReadinessPercent,
            nextMilestone = uiState.nextMilestone,
        )

        ActiveRequestsSection(
            requests = uiState.activeRequests,
            onSeeAllClick = onSeeAllRequestsClick,
            onRequestClick = onRequestClick,
        )

        Spacer(Modifier.height(8.dp))
    }
}
