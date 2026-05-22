package com.donai.app.screens.elegibility

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.theme.*

// ─── Model ────────────────────────────────────────────────────────────────────

data class EligibilityQuestion(
    val id: String,
    val text: String,
    val checked: Boolean = false,
)

enum class LastDonationOption(val label: String) {
    OVER_8_WEEKS("Over 8 weeks ago"),
    FIRST_TIME("First time donor"),
}

@Composable
fun EligibilityScreen(
    uiState: EligibilityUiState,
    events: EligibilityEvents,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { EligibilityTopBar(onBackClick = events.onBackClick) },
        bottomBar = {
            ConfirmDonationBar(
                enabled = uiState.canConfirm && !uiState.isSubmitting,
                isSubmitting = uiState.isSubmitting,
                onClick = events.onConfirmClick,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            StepProgressHeader(
                currentStep = uiState.currentStep,
                totalSteps = uiState.totalSteps,
                stepLabel = uiState.stepLabel,
                progressFraction = uiState.currentStep.toFloat() / uiState.totalSteps,
            )

            EligibilityFormCard(
                questions = uiState.questions,
                onQuestionChecked = events.onQuestionChecked,
            )

            LastDonationSection(
                selected = uiState.selectedDonationOption,
                onSelected = events.onDonationOptionSelected,
            )

            DisclaimerNote()

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EligibilityTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Eligibility",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier,
    )
}

// ─── Step Progress Header ─────────────────────────────────────────────────────

@Composable
private fun StepProgressHeader(
    currentStep: Int,
    totalSteps: Int,
    stepLabel: String,
    progressFraction: Float,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "STEP $currentStep OF $totalSteps",
            style = MaterialTheme.typography.labelSmall,
            color = DonAIRed,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stepLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "${(progressFraction * 100).toInt()}% Complete",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        }
        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50)),
            color = DonAIRed,
            trackColor = DonAIRed.copy(alpha = 0.15f),
        )
    }
}

// ─── Eligibility Form Card ────────────────────────────────────────────────────

@Composable
private fun EligibilityFormCard(
    questions: List<EligibilityQuestion>,
    onQuestionChecked: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Donor Eligibility Form",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Your safety is our priority. Please accurately confirm your current health status before proceeding with the donation.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DonAIRed.copy(alpha = 0.04f),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                questions.forEachIndexed { index, question ->
                    EligibilityCheckRow(
                        question = question,
                        onChecked = { checked -> onQuestionChecked(question.id, checked) },
                    )
                    if (index < questions.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            modifier = Modifier.padding(horizontal = 12.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EligibilityCheckRow(
    question: EligibilityQuestion,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onChecked(!question.checked) }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Checkbox(
            checked = question.checked,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = DonAIRed,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = Color.White,
            ),
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ─── Last Donation Section ────────────────────────────────────────────────────

@Composable
private fun LastDonationSection(
    selected: LastDonationOption?,
    onSelected: (LastDonationOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "When was your last blood donation?",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LastDonationOption.entries.forEach { option ->
                DonationOptionChip(
                    option = option,
                    isSelected = selected == option,
                    onClick = { onSelected(option) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DonationOptionChip(
    option: LastDonationOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) DonAIRed else MaterialTheme.colorScheme.outline,
        animationSpec = tween(200), label = "chipBorder",
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) DonAIRed.copy(alpha = 0.06f)
        else MaterialTheme.colorScheme.surface,
        animationSpec = tween(200), label = "chipBg",
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) DonAIRed else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(200), label = "chipText",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = option.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

// ─── Disclaimer Note ──────────────────────────────────────────────────────────

@Composable
private fun DisclaimerNote(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
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
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 2.dp),
            )
            Text(
                text = "By confirming, you acknowledge that all information provided is truthful. Misinformation may lead to immediate disqualification from the donation program.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

// ─── Confirm Donation Bottom Bar ──────────────────────────────────────────────

@Composable
private fun ConfirmDonationBar(
    enabled: Boolean,
    isSubmitting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                } else {
                    Text(
                        text = "Confirm Donation",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Text(
                text = "Powered by DonAI Medical Systems",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val sampleQuestions = listOf(
    EligibilityQuestion("q1", "I feel healthy and well today", checked = true),
    EligibilityQuestion("q2", "No tattoos or piercings in 6 months"),
    EligibilityQuestion("q3", "No recent travel to restricted areas"),
)
private val sampleEvents = EligibilityEvents(
    onBackClick = {}, onQuestionChecked = { _, _ -> },
    onDonationOptionSelected = {}, onConfirmClick = {},
)

@Preview(showBackground = true, name = "Light — partial")
@Composable
private fun EligibilityLightPreview() {
    DonAITheme(darkTheme = false) {
        EligibilityScreen(
            uiState = EligibilityUiState(
                questions = sampleQuestions,
                selectedDonationOption = LastDonationOption.OVER_8_WEEKS,
            ),
            events = sampleEvents,
        )
    }
}

@Preview(showBackground = true, name = "Light — all ready")
@Composable
private fun EligibilityReadyPreview() {
    DonAITheme(darkTheme = false) {
        EligibilityScreen(
            uiState = EligibilityUiState(
                questions = sampleQuestions.map { it.copy(checked = true) },
                selectedDonationOption = LastDonationOption.OVER_8_WEEKS,
                canConfirm = true,
            ),
            events = sampleEvents,
        )
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
private fun EligibilityDarkPreview() {
    DonAITheme(darkTheme = true) {
        EligibilityScreen(
            uiState = EligibilityUiState(questions = sampleQuestions),
            events = sampleEvents,
        )
    }
}
