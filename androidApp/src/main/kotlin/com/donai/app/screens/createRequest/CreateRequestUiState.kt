package com.donai.app.screens.createRequest

data class CreateRequestUiState(
    val receiverName: String = "",
    val hospital: String = "",
    val selectedBloodType: BloodType? = null,
    val donorsNeeded: Int = 1,
    val isUrgent: Boolean = false,
    val isSubmitting: Boolean = false,

    // Validation feedback owned by the ViewModel
    val receiverNameError: String? = null,
    val hospitalError: String? = null,
    val bloodTypeError: String? = null,

    // Derived: true when the form can be submitted
    val canSubmit: Boolean = false,
)