package com.donai.app.screens.elegibility

data class EligibilityEvents(
    val onBackClick: () -> Unit,
    val onQuestionChecked: (id: String, checked: Boolean) -> Unit,
    val onDonationOptionSelected: (LastDonationOption) -> Unit,
    val onConfirmClick: () -> Unit,
)