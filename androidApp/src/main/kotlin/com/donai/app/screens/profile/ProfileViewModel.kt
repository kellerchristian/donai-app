package com.donai.app.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            profile = ProfileInfo(
                fullName = "Alex Johnson",
                email = "alex.j@donai.care",
                bloodType = "O Positive (O+)",
                location = "San Francisco, CA",
            )
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onEditAvatarClick() { /* TODO: photo picker */ }

    fun onEditFieldClick(field: ProfileField) { /* TODO: inline edit dialog */ }

    fun onLogout(onSuccess: () -> Unit) {
        // TODO: inject AuthRepository and call logout()
        onSuccess()
    }
}