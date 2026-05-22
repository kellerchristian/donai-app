package com.donai.app.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.components.AuthErrorBanner
import com.donai.app.components.AuthOrDivider
import com.donai.app.components.AuthTextField
import com.donai.app.components.PasswordVisibilityToggle
import com.donai.app.theme.*

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    events: LoginEvents,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // ── Brand header ──
            LoginBrandHeader()

            Spacer(Modifier.height(32.dp))

            // ── Form card ──
            LoginFormCard(
                uiState = uiState,
                onEmailChange = events.onEmailChange,
                onPasswordChange = events.onPasswordChange,
                onPasswordVisibilityToggle = events.onPasswordVisibilityToggle,
                onForgotPasswordClick = events.onForgotPasswordClick,
                onLoginClick = events.onLoginClick,
                onCreateAccountClick = events.onCreateAccountClick,
                onImeNext = { focusManager.moveFocus(FocusDirection.Down) },
                onImeDone = {
                    focusManager.clearFocus()
                    if (uiState.canSubmit) events.onLoginClick()
                },
            )

            Spacer(Modifier.height(20.dp))

            // ── Legal note ──
            LegalNote(
                onTermsClick = events.onTermsClick,
                onPrivacyClick = events.onPrivacyClick,
            )

            Spacer(Modifier.height(28.dp))

            // ── Emergency pill ──
            EmergencyNetworkPill(onClick = events.onEmergencyNetworkClick)
        }
    }
}

// ─── Brand Header ─────────────────────────────────────────────────────────────

@Composable
private fun LoginBrandHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppLogoMark()
        Text(
            text = "DonAI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Precision Intelligence for Blood Donation",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AppLogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(72.dp)
            .background(DonAIRed.copy(alpha = 0.10f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = "DonAI logo",
            tint = DonAIRed,
            modifier = Modifier.size(36.dp),
        )
        // Notification dot — decorative, matches the design
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(DonAIRed, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                .align(Alignment.TopEnd)
                .offset(x = (-6).dp, y = 6.dp),
        )
    }
}

// ─── Form Card ────────────────────────────────────────────────────────────────

@Composable
private fun LoginFormCard(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onImeNext: () -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // General error banner
            uiState.generalError?.let { msg ->
                AuthErrorBanner(message = msg)
            }

            // Email
            AuthTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = "Email",
                placeholder = "name@example.com",
                leadingIcon = Icons.Outlined.Email,
                error = uiState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { onImeNext() }),
            )

            // Password
            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Password",
                placeholder = "Enter your password",
                leadingIcon = Icons.Outlined.Lock,
                error = uiState.passwordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { onImeDone() }),
                visualTransformation = if (uiState.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    PasswordVisibilityToggle(
                        isVisible = uiState.isPasswordVisible,
                        onToggle = onPasswordVisibilityToggle,
                    )
                },
            )

            // Forgot password — right-aligned
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(
                    onClick = onForgotPasswordClick,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.labelMedium,
                        color = DonAIRed,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            // Log in button
            LoginButton(
                isLoading = uiState.isLoading,
                enabled = uiState.canSubmit && !uiState.isLoading,
                onClick = onLoginClick,
            )

            // Divider OR
            AuthOrDivider(label = "OR")

            // Create account
            CreateAccountButton(onClick = onCreateAccountClick)
        }
    }
}

// AuthTextField, PasswordVisibilityToggle → com.donai.ui.components.AuthComponents

// ─── Buttons ──────────────────────────────────────────────────────────────────

@Composable
private fun LoginButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DonAIRed,
            contentColor = Color.White,
            disabledContainerColor = DonAIRed.copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.6f),
        ),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.Login,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun CreateAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                MaterialTheme.colorScheme.outline
            )
        ),
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Outlined.PersonAdd,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ─── OR Divider ───────────────────────────────────────────────────────────────

// OrDivider → com.donai.ui.components.AuthOrDivider (shared)

// ErrorBanner → com.donai.ui.components.AuthErrorBanner (shared)

// ─── Legal Note ───────────────────────────────────────────────────────────────

@Composable
private fun LegalNote(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
    val linkColor  = DonAIRed

    val text = buildAnnotatedString {
        withStyle(SpanStyle(color = baseColor)) { append("By logging in, you agree to DonAI's ") }
        pushStringAnnotation("TERMS", "terms")
        withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold)) {
            append("Terms of Service")
        }
        pop()
        withStyle(SpanStyle(color = baseColor)) { append(" and ") }
        pushStringAnnotation("PRIVACY", "privacy")
        withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold)) {
            append("Privacy Policy")
        }
        pop()
        withStyle(SpanStyle(color = baseColor)) { append(".") }
    }

    androidx.compose.foundation.text.ClickableText(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
        onClick = { offset ->
            text.getStringAnnotations("TERMS", offset, offset)
                .firstOrNull()?.let { onTermsClick() }
            text.getStringAnnotations("PRIVACY", offset, offset)
                .firstOrNull()?.let { onPrivacyClick() }
        },
        modifier = modifier.padding(horizontal = 8.dp),
    )
}

// ─── Emergency Network Pill ───────────────────────────────────────────────────

@Composable
private fun EmergencyNetworkPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = modifier,
    ) {
        Text(
            text = "EMERGENCY RESPONSE NETWORK",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            letterSpacing = 1.5.sp,
        )
    }
}
