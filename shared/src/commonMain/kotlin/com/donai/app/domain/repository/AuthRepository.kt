package com.donai.app.domain.repository

import com.donai.app.core.auth.AuthenticatedSession

interface AuthRepository {

    suspend fun login(email: String, password: String): AuthenticatedSession

    suspend fun register(email: String, password: String): AuthenticatedSession

    suspend fun logout()

    suspend fun getCurrentSession(): AuthenticatedSession?

    suspend fun getIdToken(): String?
}