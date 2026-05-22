package com.donai.app.screens.createAccount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAccountUiState())
    val uiState: StateFlow<CreateAccountUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) =
        _uiState.update { it.copy(fullName = value, fullNameError = null).withCanSubmit() }

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, emailError = null).withCanSubmit() }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null).withCanSubmit() }

    fun togglePasswordVisibility() =
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

    fun onTermsAcceptedChange(accepted: Boolean) =
        _uiState.update { it.copy(termsAccepted = accepted).withCanSubmit() }

    fun onGoogleSignIn() { /* TODO */ }
    fun onAppleSignIn()  { /* TODO */ }

    fun onRegister(onSuccess: () -> Unit) {
        // TODO: inject AuthRepository
        onSuccess()
    }

    private fun CreateAccountUiState.withCanSubmit() = copy(
        canSubmit = fullName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                termsAccepted,
    )
}