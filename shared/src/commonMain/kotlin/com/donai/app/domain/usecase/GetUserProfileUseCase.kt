package com.donai.app.domain.usecase

import com.donai.app.domain.model.user.User
import com.donai.app.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): User {
        return repository.getMe()
    }
}