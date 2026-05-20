package com.donai.app.domain.model.event

import kotlinx.datetime.Instant

data class DonationEvent(
    val id: String,

    val donorId: String,
    val requestId: String,
    val commitmentId: String,

    val donatedAt: Instant,

    val createdAt: Instant
)