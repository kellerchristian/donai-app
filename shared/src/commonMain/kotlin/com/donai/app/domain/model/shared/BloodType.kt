package com.donai.app.domain.model.shared

data class BloodType(
    val bloodGroup: BloodGroup,
    val rhFactor: RhFactor
) {

    val displayName: String
        get() {
            val rh = if (rhFactor == RhFactor.POSITIVE) "+" else "-"
            return "${bloodGroup.name}$rh"
        }
}