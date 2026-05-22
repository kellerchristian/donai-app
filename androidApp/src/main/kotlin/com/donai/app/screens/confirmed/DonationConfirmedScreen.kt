package com.donai.app.screens.confirmed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.donai.app.theme.*

// ─── Model ────────────────────────────────────────────────────────────────────

data class DonationSummary(
    val receiverName: String,
    val receiverAvatarUrl: String? = null,
    val hospital: String,
    val appointmentLabel: String,         // e.g. "Oct 24, 10:30 AM"
    val mapImageUrl: String? = null,      // static map snapshot
)


@Composable
fun DonationConfirmedScreen(
    uiState: DonationConfirmedUiState,
    events: DonationConfirmedEvents,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Minimal header ──
            ConfirmationTopRow(onCloseClick = events.onCloseClick)

            Spacer(Modifier.height(32.dp))

            // ── Success icon ──
            SuccessCircle()

            Spacer(Modifier.height(24.dp))

            // ── Title + subtitle ──
            Text(
                text = "Donation Confirmed!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Thank you for your life-saving contribution.\nYou will receive a notification with further instructions shortly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(28.dp))

            // ── Donation Summary card ──
            DonationSummaryCard(summary = uiState.summary)

            Spacer(Modifier.height(28.dp))
        }

        // ── Sticky bottom CTA ──
        BackToDashboardButton(
            onClick = events.onBackToDashboardClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        )
    }
}

// ─── Top Row (CONFIRMATION label + close) ─────────────────────────────────────

@Composable
private fun ConfirmationTopRow(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = "CONFIRMATION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            letterSpacing = 1.5.sp,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

// ─── Success Circle ───────────────────────────────────────────────────────────

@Composable
private fun SuccessCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(EligibleGreen),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Confirmed",
            tint = Color.White,
            modifier = Modifier.size(48.dp),
        )
    }
}

// ─── Donation Summary Card ────────────────────────────────────────────────────

@Composable
private fun DonationSummaryCard(
    summary: DonationSummary,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = DonAIRed,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = "Donation Summary",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
            )

            // Receiver row
            ReceiverRow(
                name = summary.receiverName,
                avatarUrl = summary.receiverAvatarUrl,
            )

            Spacer(Modifier.height(16.dp))

            // Hospital + Appointment
            HospitalAppointmentRow(
                hospital = summary.hospital,
                appointment = summary.appointmentLabel,
            )

            Spacer(Modifier.height(12.dp))

            // Map snapshot
            MapSnapshot(imageUrl = summary.mapImageUrl)
        }
    }
}

@Composable
private fun ReceiverRow(
    name: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "RECEIVER",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                letterSpacing = 0.8.sp,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun HospitalAppointmentRow(
    hospital: String,
    appointment: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        SummaryField(label = "HOSPITAL", value = hospital)
        SummaryField(label = "APPOINTMENT", value = appointment, textAlign = TextAlign.End)
    }
}

@Composable
private fun SummaryField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            letterSpacing = 0.8.sp,
            textAlign = textAlign,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun MapSnapshot(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1B3A34)),          // dark map tint fallback
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Location map",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            // Placeholder pin icon
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

// ─── Back to Dashboard Button ─────────────────────────────────────────────────

@Composable
private fun BackToDashboardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DonAIRed,
            contentColor = Color.White,
        ),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier,
    ) {
        Text(
            text = "Back to Dashboard",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Outlined.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val sampleSummary = DonationSummary(
    receiverName = "Johnathan Doe",
    hospital = "City General Hospital",
    appointmentLabel = "Oct 24, 10:30 AM",
)
private val sampleEvents = DonationConfirmedEvents(
    onCloseClick = {},
    onBackToDashboardClick = {},
)

@Preview(showBackground = true, name = "Light")
@Composable
private fun DonationConfirmedLightPreview() {
    DonAITheme(darkTheme = false) {
        DonationConfirmedScreen(
            uiState = DonationConfirmedUiState(summary = sampleSummary),
            events = sampleEvents,
        )
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
private fun DonationConfirmedDarkPreview() {
    DonAITheme(darkTheme = true) {
        DonationConfirmedScreen(
            uiState = DonationConfirmedUiState(summary = sampleSummary),
            events = sampleEvents,
        )
    }
}
