package com.donai.app.screens.activeRequest

data class ActiveRequestsUiState(
    val selectedTab: RequestTab = RequestTab.URGENT,
    val requests: List<ActiveBloodRequest> = emptyList(),
    val isLoading: Boolean = false,
)