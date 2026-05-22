package com.donai.app.screens.profile

data class ProfileUiState(
    val profile: ProfileInfo,
    val isLoggingOut: Boolean = false,
)