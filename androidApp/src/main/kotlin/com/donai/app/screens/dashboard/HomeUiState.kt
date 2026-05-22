package com.donai.app.screens.dashboard

data class HomeUiState(
    val userName: String = "Alex",
    val avatarUrl: String? = null,
    val isEligible: Boolean = true,
    val lastDonationDaysAgo: Int = 94,
    val healthReadinessPercent: Float = 1.0f,
    val nextMilestone: String = "Gold Badge",
    val activeRequests: List<BloodRequest> = emptyList(),
    val hasUnreadNotifications: Boolean = true,
)