package com.donai.app.screens.createRequest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.theme.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

// ─── Model ───────────────────────────────────────────────────────────────────

enum class BloodType(val label: String) {
    A_POS("A+"), A_NEG("A-"),
    B_POS("B+"), B_NEG("B-"),
    AB_POS("AB+"), AB_NEG("AB-"),
    O_POS("O+"), O_NEG("O-"),
}

/**
 * CreateRequestScreen content.
 * Managed by MainScaffold in DonAINavHost.
 */
@Composable
fun CreateRequestScreen(
    uiState: CreateRequestUiState,
    events: CreateRequestEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ReceiverNameField(
            value = uiState.receiverName,
            error = uiState.receiverNameError,
            onValueChange = events.onReceiverNameChange,
        )

        HospitalField(
            value = uiState.hospital,
            error = uiState.hospitalError,
            onValueChange = events.onHospitalChange,
        )

        BloodTypeAndDonorsRow(
            selectedBloodType = uiState.selectedBloodType,
            bloodTypeError = uiState.bloodTypeError,
            donorsNeeded = uiState.donorsNeeded,
            onBloodTypeSelected = events.onBloodTypeSelected,
            onIncrement = events.onDonorsIncrement,
            onDecrement = events.onDonorsDecrement,
        )

        UrgentRequestToggle(
            isUrgent = uiState.isUrgent,
            onToggle = events.onUrgentToggle,
        )

        Spacer(Modifier.height(4.dp))

        SubmitSection(
            isSubmitting = uiState.isSubmitting,
            canSubmit = uiState.canSubmit,
            onSubmit = events.onSubmit,
            onPrivacyPolicyClick = events.onPrivacyPolicyClick,
        )
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRequestTopBar(
    //onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "New Blood Request",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
//        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Back",
//                    tint = DonAIRed,
//                )
//            }
//        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier,
    )
}

// ─── Form Fields ──────────────────────────────────────────────────────────────

@Composable
private fun ReceiverNameField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LabeledFormField(label = "RECEIVER FULL NAME", modifier = modifier) {
        DonAIOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "e.g. Johnathan Doe",
            leadingIcon = Icons.Outlined.Person,
            leadingIconDescription = "Person",
            error = error,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            ),
        )
    }
}

@Composable
private fun HospitalField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LabeledFormField(label = "HOSPITAL / MEDICAL CENTER", modifier = modifier) {
        DonAIOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Enter hospital name",
            leadingIcon = Icons.Outlined.Search,
            leadingIconDescription = "Hospital",
            error = error,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            ),
        )
    }
}

@Composable
private fun BloodTypeAndDonorsRow(
    selectedBloodType: BloodType?,
    bloodTypeError: String?,
    donorsNeeded: Int,
    onBloodTypeSelected: (BloodType?) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        LabeledFormField(
            label = "BLOOD TYPE",
            modifier = Modifier.weight(1f),
        ) {
            BloodTypeDropdown(
                selected = selectedBloodType,
                error = bloodTypeError,
                onSelected = onBloodTypeSelected,
            )
        }

        LabeledFormField(
            label = "DONORS NEEDED",
            modifier = Modifier.weight(1f),
        ) {
            DonorCountStepper(
                count = donorsNeeded,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BloodTypeDropdown(
    selected: BloodType?,
    error: String?,
    onSelected: (BloodType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = when {
        error != null -> MaterialTheme.colorScheme.error
        else          -> MaterialTheme.colorScheme.outline
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = DonAIRed,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = selected?.label ?: "Select",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected != null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f),
                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            BloodType.entries.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = type.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (type == selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (type == selected) DonAIRed
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    },
                    trailingIcon = if (type == selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = DonAIRed,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    } else null,
                )
            }
        }
    }

    if (error != null) {
        Text(
            text = error,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
        )
    }
}

@Composable
private fun DonorCountStepper(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(10.dp),
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StepperButton(
                icon = Icons.Filled.Clear,
                contentDescription = "Decrease donors",
                enabled = count > 1,
                onClick = onDecrement,
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 32.dp),
            )

            StepperButton(
                icon = Icons.Filled.Add,
                contentDescription = "Increase donors",
                enabled = count < 99,
                onClick = onIncrement,
            )
        }
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(36.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun UrgentRequestToggle(
    isUrgent: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isUrgent) DonAIRed.copy(alpha = 0.08f)
    else MaterialTheme.colorScheme.surface

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isUrgent) DonAIRed.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(DonAIRed, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Urgent Request",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Mark this as a priority case",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }

            Switch(
                checked = isUrgent,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = DonAIRed,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                ),
            )
        }
    }
}

@Composable
private fun SubmitSection(
    isSubmitting: Boolean,
    canSubmit: Boolean,
    onSubmit: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = onSubmit,
            enabled = canSubmit && !isSubmitting,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DonAIRed,
                contentColor = Color.White,
                disabledContainerColor = DonAIRed.copy(alpha = 0.4f),
                disabledContentColor = Color.White.copy(alpha = 0.6f),
            ),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Create Blood Request",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        PrivacyPolicyNote(onPrivacyPolicyClick = onPrivacyPolicyClick)
    }
}

@Composable
private fun PrivacyPolicyNote(
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
    )
    ClickableText(
        text = buildAnnotatedString {
            append("By submitting this request, you agree to DonAI's medical data ")
            pushStringAnnotation(tag = "POLICY", annotation = "privacy_policy")
            withStyle(
                SpanStyle(
                    color = DonAIRed,
                    fontWeight = FontWeight.SemiBold,
                )
            ) { append("privacy policy") }
            pop()
            append(".")
        },
        style = baseStyle,
        onClick = { onPrivacyPolicyClick() },
        modifier = modifier.padding(horizontal = 8.dp),
    )
}

@Composable
private fun LabeledFormField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            letterSpacing = 1.sp,
        )
        content()
    }
}

@Composable
private fun DonAIOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    leadingIconDescription: String,
    error: String?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingIconDescription,
                tint = if (error != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier.size(20.dp),
            )
        },
        isError = error != null,
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DonAIRed,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Light — empty form")
@Composable
private fun CreateRequestLightEmptyPreview() {
    DonAITheme(darkTheme = false) {
        CreateRequestScreen(
            uiState = CreateRequestUiState(),
            events = CreateRequestEvents(
                onBackClick = {},
                onReceiverNameChange = {},
                onHospitalChange = {},
                onBloodTypeSelected = {},
                onDonorsIncrement = {},
                onDonorsDecrement = {},
                onUrgentToggle = {},
                onSubmit = {},
                onPrivacyPolicyClick = {},
            ),
        )
    }
}
