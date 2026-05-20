package com.donai.app.domain.model.commitment

import kotlinx.datetime.Instant

data class DonationCommitment(
    val id: String,
    val requestId: String,
    val donorId: String,

    val status: CommitmentStatus,

    val aptitudeResponses: String?,

    val acceptedAt: Instant,
    val confirmedAt: Instant?
)