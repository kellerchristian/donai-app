package com.donai.app.domain.usecase

import com.donai.app.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.login(email, password)
}