package com.donai.app.screens.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonationHistoryViewModel : ViewModel() {

    private val allGroups = listOf(
        DonationHistoryGroup(
            monthYear = "OCTOBER 2023",
            records = listOf(
                DonationRecord(
                    id = "1", dateLabel = "OCT 12, 2023",
                    hospitalName = "City General Hospital",
                    receiverOrNote = "Receiver: Sarah Jenkins",
                    bloodType = "B+",
                    testimonialText = "Thank you for your life-saving gift.",
                ),
                DonationRecord(
                    id = "2", dateLabel = "OCT 02, 2023",
                    hospitalName = "St. Mary's Medical Center",
                    receiverOrNote = "Receiver: Robert Wilson",
                    bloodType = "B+",
                ),
            ),
        ),
        DonationHistoryGroup(
            monthYear = "AUGUST 2023",
            records = listOf(
                DonationRecord(
                    id = "3", dateLabel = "AUG 24, 2023",
                    hospitalName = "Red Cross Donor Center",
                    receiverOrNote = "Emergency Reserve",
                    bloodType = "B+",
                    testimonialText = "Donated to community blood bank.",
                ),
            ),
        ),
    )

    private val _uiState = MutableStateFlow(
        DonationHistoryUiState(groups = allGroups)
    )
    val uiState: StateFlow<DonationHistoryUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: HistoryTab) =
        _uiState.update { it.copy(selectedTab = tab) }

    fun onSearchQueryChange(query: String) =
        _uiState.update { state ->
            val filtered = if (query.isBlank()) allGroups
            else allGroups.mapNotNull { group ->
                val matching = group.records.filter { record ->
                    record.hospitalName.contains(query, ignoreCase = true) ||
                            record.receiverOrNote.contains(query, ignoreCase = true)
                }
                if (matching.isEmpty()) null else group.copy(records = matching)
            }
            state.copy(searchQuery = query, groups = filtered)
        }
}