package com.donai.app.screens.profile

data class ProfileEvents(
    val onBackClick: () -> Unit,
    val onSettingsClick: () -> Unit,
    val onEditAvatarClick: () -> Unit,
    val onEditProfileClick: () -> Unit,
    val onEditFieldClick: (field: ProfileField) -> Unit,
    val onLogoutClick: () -> Unit,
)