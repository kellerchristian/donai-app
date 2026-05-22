package com.donai.app.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

//@Composable
//fun LoginRoute(
//    viewModel: LoginViewModel = viewModel()
//) {
//
//    val uiState by viewModel.uiState.collectAsState()
//
//    LoginScreen(
//        uiState = uiState,
//        events = LoginEvents(
//            onEmailChange = viewModel::onEmailChange,
//            onPasswordChange = viewModel::onPasswordChange,
//            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
//            onLoginClick = viewModel::onLogin,
//            onForgotPasswordClick = {},
//            onCreateAccountClick = {},
//            onTermsClick = {},
//            onPrivacyClick = {},
//            onEmergencyNetworkClick = {},
//        )
//    )
//}