package com.donai.app.domain.usecase

import com.donai.app.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() = repository.logout()
}