package com.donai.app.domain.usecase

import com.donai.app.domain.repository.AuthRepository

class RegisterAuthUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ) = authRepository.register(email, password)
}