package com.donai.app.screens.elegibility

data class EligibilityUiState(
    val currentStep: Int = 3,
    val totalSteps: Int = 4,
    val stepLabel: String = "Medical Screening",
    val questions: List<EligibilityQuestion> = emptyList(),
    val selectedDonationOption: LastDonationOption? = null,
    val isSubmitting: Boolean = false,
    val canConfirm: Boolean = false,
)