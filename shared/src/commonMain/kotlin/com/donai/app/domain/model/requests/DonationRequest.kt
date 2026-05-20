package com.donai.app.domain.model.requests

import com.donai.app.domain.model.shared.BloodGroup
import com.donai.app.domain.model.shared.BloodType
import com.donai.app.domain.model.shared.GeoLocation
import com.donai.app.domain.model.shared.RhFactor

data class DonationRequest(
    val id: String,
    val requesterId: String,

    val requiredBloodGroup: BloodGroup,
    val requiredRhFactor: RhFactor,

    val quantityNeeded: Int,
    val confirmedDonors: Int,

    val locationLat: Double,
    val locationLng: Double,

    val description: String,

    val status: RequestStatus
) {

    val bloodType: BloodType
        get() = BloodType(
            bloodGroup = requiredBloodGroup,
            rhFactor = requiredRhFactor
        )

    val location: GeoLocation
        get() = GeoLocation(
            latitude = locationLat,
            longitude = locationLng
        )

    val remainingDonorsNeeded: Int
        get() = (quantityNeeded - confirmedDonors).coerceAtLeast(0)

    val isCompleted: Boolean
        get() = confirmedDonors >= quantityNeeded
}