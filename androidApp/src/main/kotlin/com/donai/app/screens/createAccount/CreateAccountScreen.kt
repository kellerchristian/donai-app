package com.donai.app.screens.createAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.components.AuthErrorBanner
import com.donai.app.components.AuthOrDivider
import com.donai.app.components.AuthTextField
import com.donai.app.components.PasswordVisibilityToggle
import com.donai.app.theme.*

@Composable
fun CreateAccountScreen(
    uiState: CreateAccountUiState,
    events: CreateAccountEvents,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { CreateAccountTopBar(onBackClick = events.onBackClick) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            RegisterHeader()

            uiState.generalError?.let { AuthErrorBanner(message = it) }

            RegisterForm(
                uiState = uiState,
                onFullNameChange = events.onFullNameChange,
                onEmailChange = events.onEmailChange,
                onPasswordChange = events.onPasswordChange,
                onPasswordVisibilityToggle = events.onPasswordVisibilityToggle,
                onTermsAcceptedChange = events.onTermsAcceptedChange,
                onTermsClick = events.onTermsClick,
                onPrivacyClick = events.onPrivacyClick,
                onImeNext = { focusManager.moveFocus(FocusDirection.Down) },
                onImeDone = {
                    focusManager.clearFocus()
                    if (uiState.canSubmit) events.onRegisterClick()
                },
            )

            RegisterButton(
                isLoading = uiState.isLoading,
                enabled = uiState.canSubmit && !uiState.isLoading,
                onClick = events.onRegisterClick,
            )

            AuthOrDivider(label = "OR SIGN UP WITH")

            SocialSignInRow(
                onGoogleClick = events.onGoogleSignInClick,
                onAppleClick = events.onAppleSignInClick,
            )

            BackToLoginLink(onClick = events.onBackToLoginClick)

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAccountTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Create Account",
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

// ─── Register Header ──────────────────────────────────────────────────────────

@Composable
private fun RegisterHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RegisterLogoMark()
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Join DonAI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Start your journey as a life saver today. Register to connect with those who need you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun RegisterLogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(52.dp)
            .background(DonAIRed, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.HealthAndSafety,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

// ─── Register Form ────────────────────────────────────────────────────────────

@Composable
private fun RegisterForm(
    uiState: CreateAccountUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onImeNext: () -> Unit,
    onImeDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Full Name
        AuthTextField(
            value = uiState.fullName,
            onValueChange = onFullNameChange,
            label = "Full Name",
            placeholder = "Enter your full name",
            leadingIcon = Icons.Outlined.Person,
            error = uiState.fullNameError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { onImeNext() }),
        )

        // Email
        AuthTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email Address",
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
            placeholder = "Create a strong password",
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

        // Terms checkbox
        TermsCheckbox(
            accepted = uiState.termsAccepted,
            error = uiState.termsError,
            onAcceptedChange = onTermsAcceptedChange,
            onTermsClick = onTermsClick,
            onPrivacyClick = onPrivacyClick,
        )
    }
}

// ─── Terms Checkbox ───────────────────────────────────────────────────────────

@Composable
private fun TermsCheckbox(
    accepted: Boolean,
    error: String?,
    onAcceptedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Checkbox(
                checked = accepted,
                onCheckedChange = onAcceptedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = DonAIRed,
                    uncheckedColor = if (error != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline,
                    checkmarkColor = Color.White,
                ),
                modifier = Modifier.size(20.dp),
            )

            Spacer(Modifier.width(4.dp))

            val text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                ) { append("I agree to the ") }
                pushStringAnnotation("TERMS", "terms")
                withStyle(
                    SpanStyle(
                        color = DonAIRed,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                    )
                ) { append("Terms of Service") }
                pop()
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                ) { append(" and ") }
                pushStringAnnotation("PRIVACY", "privacy")
                withStyle(
                    SpanStyle(
                        color = DonAIRed,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                    )
                ) { append("Privacy Policy") }
                pop()
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                ) { append(".") }
            }

            androidx.compose.foundation.text.ClickableText(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                onClick = { offset ->
                    text.getStringAnnotations("TERMS", offset, offset)
                        .firstOrNull()?.let { onTermsClick() }
                    text.getStringAnnotations("PRIVACY", offset, offset)
                        .firstOrNull()?.let { onPrivacyClick() }
                },
            )
        }

        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 28.dp),
            )
        }
    }
}

// ─── Register Button ──────────────────────────────────────────────────────────

@Composable
private fun RegisterButton(
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
                text = "Register Account",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

// ─── Social Sign-In Row ───────────────────────────────────────────────────────

@Composable
private fun SocialSignInRow(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SocialSignInButton(
            label = "Google",
            icon = Icons.Outlined.Language,    // placeholder — swap with painterResource(R.drawable.ic_google)
            onClick = onGoogleClick,
            modifier = Modifier.weight(1f),
        )
        SocialSignInButton(
            label = "Apple",
            icon = Icons.Outlined.PhoneIphone, // placeholder — swap with painterResource(R.drawable.ic_apple)
            onClick = onAppleClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SocialSignInButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = SolidColor(MaterialTheme.colorScheme.outline),
        ),
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ─── Back to Login ────────────────────────────────────────────────────────────

@Composable
private fun BackToLoginLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = "Already have an account?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = DonAIRed,
            )
        }
    }
}
