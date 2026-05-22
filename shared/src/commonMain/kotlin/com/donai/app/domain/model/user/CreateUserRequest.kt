package com.donai.app.domain.model.user

data class CreateUserRequest(
    val name: String,
    val email: String,
    val bloodGroup: String,
    val rhFactor: String,
    val latitude: Double,
    val longitude: Double,
    val locationDisplay: String,
    val gdprAccepted: Boolean
)