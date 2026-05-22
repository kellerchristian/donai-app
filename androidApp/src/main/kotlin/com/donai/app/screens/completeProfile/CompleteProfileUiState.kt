package com.donai.app.screens.completeProfile

data class CompleteProfileUiState(
    val fullName: String = "",
    val selectedBloodGroup: BloodGroup? = null,
    val location: String = "",
    val lastDonation: LastDonationTime? = null,

    // Validation — owned by ViewModel, zero logic in UI
    val fullNameError: String? = null,
    val bloodGroupError: String? = null,
    val locationError: String? = null,
    val lastDonationError: String? = null,

    val isSubmitting: Boolean = false,
    val canFinish: Boolean = false,
)