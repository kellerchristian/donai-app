package com.donai.app.screens.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
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
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}