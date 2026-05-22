package com.donai.app.screens.completeProfile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CompleteProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteProfileUiState())
    val uiState: StateFlow<CompleteProfileUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) =
        _uiState.update { it.copy(fullName = value, fullNameError = null).recalcCanFinish() }

    fun onBloodGroupSelected(group: BloodGroup) =
        _uiState.update { it.copy(selectedBloodGroup = group, bloodGroupError = null).recalcCanFinish() }

    fun onLocationChange(value: String) =
        _uiState.update { it.copy(location = value, locationError = null).recalcCanFinish() }

    fun onLastDonationSelected(option: LastDonationTime) =
        _uiState.update { it.copy(lastDonation = option, lastDonationError = null).recalcCanFinish() }

    fun onFinish(onSuccess: () -> Unit) {
        val state = _uiState.value
        // Client-side guard — ViewModel validates before delegating to repo
        if (!state.canFinish) {
            _uiState.update {
                it.copy(
                    fullNameError = if (it.fullName.isBlank()) "Please enter your full name" else null,
                    bloodGroupError = if (it.selectedBloodGroup == null) "Please select a blood group" else null,
                    locationError = if (it.location.isBlank()) "Location is required" else null,
                    lastDonationError = if (it.lastDonation == null) "Please select when you last donated" else null,
                )
            }
            return
        }
        // TODO: inject UserRepository and persist profile
        // userRepository.saveProfile(state.toProfile())
        onSuccess()
    }

    private fun CompleteProfileUiState.recalcCanFinish() = copy(
        canFinish = fullName.isNotBlank() &&
                selectedBloodGroup != null &&
                location.isNotBlank() &&
                lastDonation != null,
    )
}