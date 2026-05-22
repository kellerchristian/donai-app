package com.donai.app.screens.completeProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.components.DonAIDropdown
import com.donai.app.components.DonAIOutlinedTextField
import com.donai.app.components.LabeledFormField
import com.donai.app.theme.DonAIRed
import com.donai.app.theme.DonAITheme

// ─── Model ────────────────────────────────────────────────────────────────────

/**
 * All ABO + Rh blood group combinations.
 * Shared with other screens via the app module — not duplicated from CreateRequestScreen's
 * BloodType enum which belongs to the ui module.
 */
enum class BloodGroup(val label: String) {
    A_POS("A+"),  A_NEG("A-"),
    B_POS("B+"),  B_NEG("B-"),
    AB_POS("AB+"), AB_NEG("AB-"),
    O_POS("O+"),  O_NEG("O-"),
}

enum class LastDonationTime(val label: String) {
    MORE_THAN_1_MONTH("More than 1 month ago"),
    MORE_THAN_2_MONTHS("More than 2 months ago"),
    MORE_THAN_3_MONTHS("More than 3 months ago"),
    NEVER("I have never donated"),
}


@Composable
fun CompleteProfileScreen(
    uiState: CompleteProfileUiState,
    events: CompleteProfileEvents,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        // No top bar — this is a full-screen onboarding step
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(40.dp))

            ProfileSetupHeader()

            Spacer(Modifier.height(32.dp))

            ProfileForm(
                uiState = uiState,
                events = events,
                onImeNext = { focusManager.moveFocus(FocusDirection.Down) },
                onImeDone = {
                    focusManager.clearFocus()
                    if (uiState.canFinish) events.onFinishClick()
                },
            )

            Spacer(Modifier.height(28.dp))

            FinishButton(
                isSubmitting = uiState.isSubmitting,
                enabled = uiState.canFinish && !uiState.isSubmitting,
                onClick = events.onFinishClick,
            )

            Spacer(Modifier.height(16.dp))

            StepIndicator(currentStep = 2, totalSteps = 2)

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun ProfileSetupHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HeaderIconMark()

        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Help us match you with the right donation opportunities by telling us a little more about you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HeaderIconMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(DonAIRed.copy(alpha = 0.10f)),
        contentAlignment = Alignment.Center,
    ) {
        // Outer ring
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(DonAIRed.copy(alpha = 0.08f)),
        )
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = null,
            tint = DonAIRed,
            modifier = Modifier.size(40.dp),
        )
        // Tiny badge
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(DonAIRed)
                .align(Alignment.BottomEnd)
                .offset(x = (-4).dp, y = (-4).dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp),
            )
        }
    }
}

// ─── Form ─────────────────────────────────────────────────────────────────────

@Composable
private fun ProfileForm(
    uiState: CompleteProfileUiState,
    events: CompleteProfileEvents,
    onImeNext: () -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Full name
        LabeledFormField(label = "FULL NAME") {
            DonAIOutlinedTextField(
                value = uiState.fullName,
                onValueChange = events.onFullNameChange,
                placeholder = "e.g. Alex Johnson",
                leadingIcon = Icons.Outlined.Person,
                leadingIconDescription = "Full name",
                error = uiState.fullNameError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { onImeNext() }),
            )
        }

        // Blood group
        LabeledFormField(label = "BLOOD GROUP & RH FACTOR") {
            DonAIDropdown(
                items = BloodGroup.entries,
                selected = uiState.selectedBloodGroup,
                onSelected = events.onBloodGroupSelected,
                itemLabel = { it.label },
                placeholder = "Select blood group",
                leadingIcon = Icons.Outlined.Bloodtype,
                error = uiState.bloodGroupError,
            )
        }

        // Location
        LabeledFormField(label = "LOCATION") {
            DonAIOutlinedTextField(
                value = uiState.location,
                onValueChange = events.onLocationChange,
                placeholder = "e.g. San Francisco, CA",
                leadingIcon = Icons.Outlined.LocationOn,
                leadingIconDescription = "Location",
                error = uiState.locationError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { onImeDone() }),
            )
        }

        // Last donation
        LabeledFormField(label = "WHEN DID YOU LAST DONATE?") {
            DonAIDropdown(
                items = LastDonationTime.entries,
                selected = uiState.lastDonation,
                onSelected = events.onLastDonationSelected,
                itemLabel = { it.label },
                placeholder = "Select an option",
                leadingIcon = Icons.Outlined.CalendarMonth,
                error = uiState.lastDonationError,
            )
        }

        // Info note
        ProfileInfoNote()
    }
}

// ─── Info Note ────────────────────────────────────────────────────────────────

@Composable
private fun ProfileInfoNote(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DonAIRed.copy(alpha = 0.05f),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = DonAIRed.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 1.dp),
            )
            Text(
                text = "This information helps DonAI find urgent requests near you and verify your eligibility before each donation.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

// ─── Finish Button ────────────────────────────────────────────────────────────

@Composable
private fun FinishButton(
    isSubmitting: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DonAIRed,
            contentColor = Color.White,
            disabledContainerColor = DonAIRed.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.5f),
        ),
        contentPadding = PaddingValues(vertical = 18.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Finish & Go to Dashboard",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp,
            )
        }
    }
}

// ─── Step Indicator ───────────────────────────────────────────────────────────

/**
 * Minimal dot-based step indicator placed below the CTA.
 * Communicates to the user that this is the last onboarding step.
 */
@Composable
private fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalSteps) { index ->
            val isActive = index + 1 == currentStep
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(
                        width = if (isActive) 24.dp else 8.dp,
                        height = 8.dp,
                    )
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isActive) DonAIRed
                        else DonAIRed.copy(alpha = 0.25f)
                    ),
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewEvents = CompleteProfileEvents(
    onFullNameChange = {},
    onBloodGroupSelected = {},
    onLocationChange = {},
    onLastDonationSelected = {},
    onFinishClick = {},
)

@Preview(showBackground = true, name = "Light — empty")
@Composable
private fun CompleteProfileLightEmptyPreview() {
    DonAITheme(darkTheme = false) {
        CompleteProfileScreen(
            uiState = CompleteProfileUiState(),
            events = previewEvents,
        )
    }
}

@Preview(showBackground = true, name = "Light — filled")
@Composable
private fun CompleteProfileLightFilledPreview() {
    DonAITheme(darkTheme = false) {
        CompleteProfileScreen(
            uiState = CompleteProfileUiState(
                fullName = "Alex Johnson",
                selectedBloodGroup = BloodGroup.O_POS,
                location = "San Francisco, CA",
                lastDonation = LastDonationTime.MORE_THAN_2_MONTHS,
                canFinish = true,
            ),
            events = previewEvents,
        )
    }
}

@Preview(showBackground = true, name = "Light — validation errors")
@Composable
private fun CompleteProfileValidationPreview() {
    DonAITheme(darkTheme = false) {
        CompleteProfileScreen(
            uiState = CompleteProfileUiState(
                fullNameError = "Please enter your full name",
                bloodGroupError = "Please select a blood group",
                locationError = "Location is required",
                lastDonationError = "Please select when you last donated",
            ),
            events = previewEvents,
        )
    }
}

@Preview(showBackground = true, name = "Light — submitting")
@Composable
private fun CompleteProfileSubmittingPreview() {
    DonAITheme(darkTheme = false) {
        CompleteProfileScreen(
            uiState = CompleteProfileUiState(
                fullName = "Alex Johnson",
                selectedBloodGroup = BloodGroup.O_POS,
                location = "San Francisco, CA",
                lastDonation = LastDonationTime.MORE_THAN_1_MONTH,
                isSubmitting = true,
            ),
            events = previewEvents,
        )
    }
}

@Preview(showBackground = true, name = "Dark — filled")
@Composable
private fun CompleteProfileDarkPreview() {
    DonAITheme(darkTheme = true) {
        CompleteProfileScreen(
            uiState = CompleteProfileUiState(
                fullName = "Alex Johnson",
                selectedBloodGroup = BloodGroup.AB_NEG,
                location = "New York, NY",
                lastDonation = LastDonationTime.MORE_THAN_3_MONTHS,
                canFinish = true,
            ),
            events = previewEvents,
        )
    }
}