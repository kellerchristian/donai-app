package com.donai.app.domain.usecase


import com.donai.app.domain.model.user.CreateUserRequest
import com.donai.app.domain.repository.UserRepository

class CompleteProfileUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        request: CreateUserRequest
    ) {
        userRepository.register(request)
    }
}