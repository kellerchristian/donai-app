package com.donai.app.screens.confirmed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DonationConfirmedViewModel : ViewModel() {

    // In a real app, inject the summary from a SavedStateHandle argument
    // passed after the eligibility form submits successfully.
    private val _uiState = MutableStateFlow(
        DonationConfirmedUiState(
            summary = DonationSummary(
                receiverName = "Johnathan Doe",
                hospital = "City General Hospital",
                appointmentLabel = "Oct 24, 10:30 AM",
            )
        )
    )
    val uiState: StateFlow<DonationConfirmedUiState> = _uiState.asStateFlow()
}