package com.donai.app.domain.model.user

data class UpdateProfileRequest(
    val name: String,
    val bloodGroup: String,
    val rhFactor: String,
    val latitude: Double,
    val longitude: Double,
    val locationDisplay: String,
    val availableToDonate: Boolean
)