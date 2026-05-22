package com.donai.app.data.remote.api

import com.donai.app.data.remote.dto.CreateUserDto
import com.donai.app.data.remote.dto.UpdateUserDto
import com.donai.app.data.remote.dto.UserDto

interface UserApi {

    suspend fun createUser(dto: CreateUserDto)

    suspend fun updateUser(dto: UpdateUserDto)

    suspend fun getMe(): UserDto
}