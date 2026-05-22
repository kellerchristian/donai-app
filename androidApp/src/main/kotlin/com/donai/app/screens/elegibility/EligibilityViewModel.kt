package com.donai.app.screens.elegibility

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EligibilityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        EligibilityUiState(
            questions = listOf(
                EligibilityQuestion("q1", "I feel healthy and well today"),
                EligibilityQuestion("q2", "No tattoos or piercings in 6 months"),
                EligibilityQuestion("q3", "No recent travel to restricted areas"),
            ),
        )
    )
    val uiState: StateFlow<EligibilityUiState> = _uiState.asStateFlow()

    fun onQuestionChecked(id: String, checked: Boolean) {
        _uiState.update { state ->
            val updated = state.questions.map {
                if (it.id == id) it.copy(checked = checked) else it
            }
            state.copy(questions = updated, canConfirm = canConfirm(state, updated))
        }
    }

    fun onDonationOptionSelected(option: LastDonationOption) {
        _uiState.update { state ->
            state.copy(
                selectedDonationOption = option,
                canConfirm = canConfirm(state.copy(selectedDonationOption = option), state.questions),
            )
        }
    }

    fun onConfirm(onSuccess: () -> Unit) {
        // TODO: submit to repository
        onSuccess()
    }

    private fun canConfirm(
        state: EligibilityUiState,
        questions: List<EligibilityQuestion> = state.questions,
    ) = questions.all { it.checked } && state.selectedDonationOption != null
}