package com.donai.app.data.repository

import com.donai.app.data.remote.api.UserApi
import com.donai.app.data.remote.mapper.toDomain
import com.donai.app.data.remote.mapper.toDto
import com.donai.app.domain.model.user.CreateUserRequest
import com.donai.app.domain.model.user.UpdateProfileRequest
import com.donai.app.domain.model.user.User
import com.donai.app.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    override suspend fun register(request: CreateUserRequest) {
        api.createUser(request.toDto())
    }

    override suspend fun updateProfile(request: UpdateProfileRequest) {
        api.updateUser(request.toDto())
    }

    override suspend fun getMe(): User {
        return api.getMe().toDomain()
    }
}