package com.donai.app.core.auth

import com.donai.app.domain.model.AuthUser

data class AuthenticatedSession(
    val user: AuthUser,
    val idToken: String
)