package com.donai.app.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donai.app.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                generalError = null,
                canSubmit = validateCanSubmit(email, it.password)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                generalError = null,
                canSubmit = validateCanSubmit(it.email, password)
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun onLogin(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        val emailError =
            if (state.email.isBlank()) "Email is required" else null

        val passwordError =
            if (state.password.length < 6)
                "Password must contain at least 6 characters"
            else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                loginUseCase(
                    email = state.email,
                    password = state.password
                )

                _uiState.update { it.copy(isLoading = false) }

                onSuccess()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = e.message ?: "Login failed"
                    )
                }
            }
        }
    }

    private fun validateCanSubmit(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}