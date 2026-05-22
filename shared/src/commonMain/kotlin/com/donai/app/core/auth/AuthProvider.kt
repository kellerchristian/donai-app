package com.donai.app.core.auth

interface AuthProvider {

    suspend fun login(
        email: String,
        password: String
    ): AuthenticatedSession

    suspend fun register(
        email: String,
        password: String
    ): AuthenticatedSession

    suspend fun logout()

    suspend fun getCurrentSession(): AuthenticatedSession?

    suspend fun getIdToken(): String?
}