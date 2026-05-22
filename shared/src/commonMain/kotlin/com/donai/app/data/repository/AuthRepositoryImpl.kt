package com.donai.app.data.repository

import com.donai.app.core.auth.AuthProvider
import com.donai.app.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authProvider: AuthProvider
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ) = authProvider.login(email, password)

    override suspend fun register(
        email: String,
        password: String
    ) = authProvider.register(email, password)

    override suspend fun logout() {
        authProvider.logout()
    }

    override suspend fun getCurrentSession() =
        authProvider.getCurrentSession()

    override suspend fun getIdToken() =
        authProvider.getIdToken()
}