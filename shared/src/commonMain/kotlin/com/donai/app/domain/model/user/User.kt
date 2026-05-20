package com.donai.app.domain.model.user

import com.donai.app.domain.model.shared.BloodType
import com.donai.app.domain.model.shared.GeoLocation
import kotlinx.datetime.Instant

data class User(
    val id: String,
    val firebaseUid: String,
    val name: String,
    val email: String,

    val bloodType: BloodType,

    val location: GeoLocation,
    val locationDisplay: String,

    val availableToDonate: Boolean,

    val lastDonationAt: Instant?,
    val nextEligibleAt: Instant?,

    val gdprAccepted: Boolean,
    val gdprAcceptedAt: Instant?,

    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
)