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

// ─── Root screen ────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNotificationClick: () -> Unit,
    onSeeAllRequestsClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            DonAITopBar(
                hasUnreadNotifications = uiState.hasUnreadNotifications,
                onNotificationClick = onNotificationClick,
            )
        },
        bottomBar = { DonAIBottomBar() },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
}

// ─── Preview ────────────────────────────────────────────────────────────────

private val previewState = HomeUiState(
    activeRequests = listOf(
        BloodRequest(
            id = "1",
            requesterName = "Sarah Williams",
            hospital = "St. Mary's General Hospital",
            distanceMiles = 0.8,
            bloodType = "O+",
            urgency = RequestUrgency.URGENT,
        ),
        BloodRequest(
            id = "2",
            requesterName = "David Chen",
            hospital = "City Medical Center",
            distanceMiles = 2.4,
            bloodType = "A-",
            urgency = RequestUrgency.HIGH,
        ),
    ),
)

@Preview(showBackground = true, name = "Light")
@Composable
private fun HomeScreenLightPreview() {
    DonAITheme(darkTheme = false) {
        HomeScreen(
            uiState = previewState,
            onNotificationClick = {},
            onSeeAllRequestsClick = {},
            onRequestClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
private fun HomeScreenDarkPreview() {
    DonAITheme(darkTheme = true) {
        HomeScreen(
            uiState = previewState,
            onNotificationClick = {},
            onSeeAllRequestsClick = {},
            onRequestClick = {},
        )
    }
}
