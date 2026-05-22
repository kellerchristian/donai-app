package com.donai.app.screens.history

data class DonationHistoryEvents(
    val onBackClick: () -> Unit,
    val onCalendarClick: () -> Unit,
    val onSearchQueryChange: (String) -> Unit,
    val onFilterClick: () -> Unit,
    val onTabSelected: (HistoryTab) -> Unit,
    val onRecordClick: (String) -> Unit,
)