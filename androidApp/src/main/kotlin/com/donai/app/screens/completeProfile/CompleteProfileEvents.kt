package com.donai.app.screens.completeProfile

data class CompleteProfileEvents(
    val onFullNameChange: (String) -> Unit,
    val onBloodGroupSelected: (BloodGroup) -> Unit,
    val onLocationChange: (String) -> Unit,
    val onLastDonationSelected: (LastDonationTime) -> Unit,
    val onFinishClick: () -> Unit,
)