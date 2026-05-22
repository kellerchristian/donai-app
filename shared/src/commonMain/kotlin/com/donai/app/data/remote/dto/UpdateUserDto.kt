package com.donai.app.data.remote.dto

@kotlinx.serialization.Serializable
data class UpdateUserDto(
    val name: String,
    val bloodGroup: String,
    val rhFactor: String,
    val latitude: Double,
    val longitude: Double,
    val locationDisplay: String,
    val availableToDonate: Boolean
)