package com.donai.app

import com.donai.app.core.auth.AuthProvider
import com.donai.app.core.auth.AuthenticatedSession
import com.donai.app.domain.model.AuthUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthProvider(
    private val firebaseAuth: FirebaseAuth
) : AuthProvider {

    override suspend fun login(
        email: String,
        password: String
    ): AuthenticatedSession {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Login failed: User is null")
        val token = user.getIdToken(true).await().token ?: throw Exception("Login failed: Token is null")

        return AuthenticatedSession(
            user = AuthUser(
                uid = user.uid,
                email = user.email
            ),
            idToken = token
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): AuthenticatedSession {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Registration failed: User is null")
        val token = user.getIdToken(true).await().token ?: throw Exception("Registration failed: Token is null")

        return AuthenticatedSession(
            user = AuthUser(
                uid = user.uid,
                email = user.email
            ),
            idToken = token
        )
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentSession(): AuthenticatedSession? {
        val user = firebaseAuth.currentUser ?: return null
        val token = user.getIdToken(false).await().token ?: return null

        return AuthenticatedSession(
            user = AuthUser(
                uid = user.uid,
                email = user.email
            ),
            idToken = token
        )
    }

    override suspend fun getIdToken(): String? {
        val user = firebaseAuth.currentUser ?: return null
        // Force refresh if needed, or just get current
        return try {
            user.getIdToken(false).await().token
        } catch (e: Exception) {
            null
        }
    }
}
