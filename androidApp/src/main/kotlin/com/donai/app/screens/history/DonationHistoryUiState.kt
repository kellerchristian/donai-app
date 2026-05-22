package com.donai.app.screens.history

data class DonationHistoryUiState(
    val searchQuery: String = "",
    val selectedTab: HistoryTab = HistoryTab.PAST_DONATIONS,
    val groups: List<DonationHistoryGroup> = emptyList(),
    val isLoading: Boolean = false,
)