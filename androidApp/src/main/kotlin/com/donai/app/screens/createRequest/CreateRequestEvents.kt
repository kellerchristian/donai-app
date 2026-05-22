package com.donai.app.screens.createRequest

data class CreateRequestEvents(
    val onBackClick: () -> Unit,
    val onReceiverNameChange: (String) -> Unit,
    val onHospitalChange: (String) -> Unit,
    val onBloodTypeSelected: (BloodType?) -> Unit,
    val onDonorsIncrement: () -> Unit,
    val onDonorsDecrement: () -> Unit,
    val onUrgentToggle: (Boolean) -> Unit,
    val onSubmit: () -> Unit,
    val onPrivacyPolicyClick: () -> Unit,
)