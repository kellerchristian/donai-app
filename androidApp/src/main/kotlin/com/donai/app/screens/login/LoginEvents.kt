package com.donai.app.screens.login

data class LoginEvents(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onPasswordVisibilityToggle: () -> Unit,
    val onLoginClick: () -> Unit,
    val onForgotPasswordClick: () -> Unit,
    val onCreateAccountClick: () -> Unit,
    val onTermsClick: () -> Unit,
    val onPrivacyClick: () -> Unit,
    val onEmergencyNetworkClick: () -> Unit,
)