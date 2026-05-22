package com.donai.app.domain.usecase

import com.donai.app.domain.model.user.UpdateProfileRequest
import com.donai.app.domain.repository.UserRepository

class UpdateProfileUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        request: UpdateProfileRequest
    ) {
        repository.updateProfile(request)
    }
}