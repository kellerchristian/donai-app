package com.donai.app.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    // Validation — computed externally, never in the UI
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,

    val canSubmit: Boolean = false,
)