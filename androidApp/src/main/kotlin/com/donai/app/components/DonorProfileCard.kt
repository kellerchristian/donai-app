package com.donai.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DonorProfileCard(
    isEligible: Boolean,
    lastDonationDaysAgo: Int,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DonorAvatar(avatarUrl = avatarUrl)
        EligibilityBadge(isEligible = isEligible)
        DonorSubtitle(isEligible = isEligible, lastDonationDaysAgo = lastDonationDaysAgo)
    }
}