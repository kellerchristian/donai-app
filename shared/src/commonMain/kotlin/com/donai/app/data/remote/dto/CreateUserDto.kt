package com.donai.app.data.remote.dto

@kotlinx.serialization.Serializable
data class CreateUserDto(
    val name: String,
    val email: String,
    val bloodGroup: String,
    val rhFactor: String,
    val latitude: Double,
    val longitude: Double,
    val locationDisplay: String,
    val gdprAccepted: Boolean
)