package com.donai.app.screens.activeRequest

import androidx.lifecycle.ViewModel
import com.donai.app.screens.dashboard.BloodRequest
import com.donai.app.screens.dashboard.RequestUrgency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActiveRequestsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ActiveRequestsUiState(
            selectedTab = RequestTab.URGENT,
            requests = listOf(
                ActiveBloodRequest(
                    request = BloodRequest(
                        id = "1",
                        requesterName = "Sarah Johnson",
                        hospital = "City General Hospital",
                        distanceMiles = 1.5,
                        bloodType = "O-",
                        urgency = RequestUrgency.URGENT,
                    ),
                    imageUrl = null,
                    distanceKm = 2.4,
                ),
                ActiveBloodRequest(
                    request = BloodRequest(
                        id = "2",
                        requesterName = "Robert Chen",
                        hospital = "St. Mary's Medical Center",
                        distanceMiles = 3.2,
                        bloodType = "A+",
                        urgency = RequestUrgency.HIGH,
                    ),
                    imageUrl = null,
                    distanceKm = 5.1,
                ),
            ),
        )
    )
    val uiState: StateFlow<ActiveRequestsUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: RequestTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}