package com.donai.app.data.remote.dto

@kotlinx.serialization.Serializable
data class UserDto(
    val id: String,
    val firebaseUid: String,
    val name: String,
    val email: String,
    val bloodGroup: String,
    val rhFactor: String,
    val latitude: Double,
    val longitude: Double,
    val locationDisplay: String,
    val availableToDonate: Boolean
)