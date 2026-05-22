package com.donai.app.screens.createRequest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateRequestViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRequestUiState())
    val uiState: StateFlow<CreateRequestUiState> = _uiState.asStateFlow()

    fun onReceiverNameChange(value: String) =
        _uiState.update { it.copy(receiverName = value, receiverNameError = null).recalcCanSubmit() }

    fun onHospitalChange(value: String) =
        _uiState.update { it.copy(hospital = value, hospitalError = null).recalcCanSubmit() }

    fun onBloodTypeSelected(type: BloodType?) =
        _uiState.update { it.copy(selectedBloodType = type, bloodTypeError = null).recalcCanSubmit() }

    fun onDonorsIncrement() =
        _uiState.update { if (it.donorsNeeded < 99) it.copy(donorsNeeded = it.donorsNeeded + 1) else it }

    fun onDonorsDecrement() =
        _uiState.update { if (it.donorsNeeded > 1) it.copy(donorsNeeded = it.donorsNeeded - 1) else it }

    fun onUrgentToggle(urgent: Boolean) =
        _uiState.update { it.copy(isUrgent = urgent) }

    fun onSubmit(onSuccess: () -> Unit) {
        // TODO: inject Repository and call createRequest()
        onSuccess()
    }

    private fun CreateRequestUiState.recalcCanSubmit() = copy(
        canSubmit = receiverName.isNotBlank() &&
                hospital.isNotBlank() &&
                selectedBloodType != null,
    )
}