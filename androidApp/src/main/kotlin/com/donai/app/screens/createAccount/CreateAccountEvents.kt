package com.donai.app.screens.createAccount

data class CreateAccountEvents(
    val onBackClick: () -> Unit,
    val onFullNameChange: (String) -> Unit,
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onPasswordVisibilityToggle: () -> Unit,
    val onTermsAcceptedChange: (Boolean) -> Unit,
    val onRegisterClick: () -> Unit,
    val onGoogleSignInClick: () -> Unit,
    val onAppleSignInClick: () -> Unit,
    val onBackToLoginClick: () -> Unit,
    val onTermsClick: () -> Unit,
    val onPrivacyClick: () -> Unit,
)