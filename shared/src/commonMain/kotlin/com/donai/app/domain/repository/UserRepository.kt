package com.donai.app.domain.repository

import com.donai.app.domain.model.user.User
import com.donai.app.domain.model.user.CreateUserRequest
import com.donai.app.domain.model.user.UpdateProfileRequest

interface UserRepository {

    suspend fun register(
        request: CreateUserRequest
    )

    suspend fun updateProfile(
        request: UpdateProfileRequest
    )

    suspend fun getMe(): User
}