package com.donai.app.domain.usecase

import com.donai.app.domain.repository.UserRepository
import com.donai.app.domain.model.user.User

class GetCurrentUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(): User {
        return repository.getMe()
    }
}