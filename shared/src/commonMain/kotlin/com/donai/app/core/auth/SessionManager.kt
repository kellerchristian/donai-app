package com.donai.app.core.auth

import com.donai.app.domain.repository.AuthRepository

class SessionManager(
    private val authRepository: AuthRepository
)