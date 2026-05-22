package com.donai.app.screens.createAccount

data class CreateAccountUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,

    // Validation — owned by ViewModel, never computed in UI
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val termsError: String? = null,
    val generalError: String? = null,

    val canSubmit: Boolean = false,
)