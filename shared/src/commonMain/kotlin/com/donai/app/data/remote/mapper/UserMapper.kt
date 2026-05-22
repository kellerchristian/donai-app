package com.donai.app.data.remote.mapper

import com.donai.app.data.remote.dto.CreateUserDto
import com.donai.app.data.remote.dto.UpdateUserDto
import com.donai.app.data.remote.dto.UserDto
import com.donai.app.domain.model.shared.BloodGroup
import com.donai.app.domain.model.shared.BloodType
import com.donai.app.domain.model.shared.GeoLocation
import com.donai.app.domain.model.shared.RhFactor
import com.donai.app.domain.model.user.CreateUserRequest
import com.donai.app.domain.model.user.UpdateProfileRequest
import com.donai.app.domain.model.user.User
import kotlinx.datetime.Instant

fun UserDto.toDomain(): User {
    return User(
        id = id,
        firebaseUid = firebaseUid,
        name = name,
        email = email,
        bloodType = BloodType(
            bloodGroup = BloodGroup.valueOf(bloodGroup),
            rhFactor = RhFactor.valueOf(rhFactor)
        ),
        location = GeoLocation(latitude, longitude),
        locationDisplay = locationDisplay,
        availableToDonate = availableToDonate,
        lastDonationAt = null,
        nextEligibleAt = null,
        gdprAccepted = false,
        gdprAcceptedAt = null,
        createdAt = Instant.fromEpochMilliseconds(0),
        updatedAt = Instant.fromEpochMilliseconds(0),
        deletedAt = null
    )
}

fun CreateUserRequest.toDto(): CreateUserDto {
    return CreateUserDto(
        name = name,
        email = email,
        bloodGroup = bloodGroup,
        rhFactor = rhFactor,
        latitude = latitude,
        longitude = longitude,
        locationDisplay = locationDisplay,
        gdprAccepted = gdprAccepted
    )
}

fun UpdateProfileRequest.toDto(): UpdateUserDto {
    return UpdateUserDto(
        name = name,
        bloodGroup = bloodGroup,
        rhFactor = rhFactor,
        latitude = latitude,
        longitude = longitude,
        locationDisplay = locationDisplay,
        availableToDonate = availableToDonate
    )
}