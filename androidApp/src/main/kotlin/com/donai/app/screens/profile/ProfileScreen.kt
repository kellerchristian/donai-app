package com.donai.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.donai.app.theme.*

// ─── Model ────────────────────────────────────────────────────────────────────

data class ProfileInfo(
    val fullName: String,
    val email: String,
    val bloodType: String,
    val location: String,
    val avatarUrl: String? = null,
)

enum class ProfileField { FULL_NAME, EMAIL, BLOOD_TYPE, LOCATION }

// ─── Root Content ─────────────────────────────────────────────────────────────

/**
 * ProfileScreen content.
 * Managed by MainScaffold in DonAINavHost.
 */
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    events: ProfileEvents,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ProfileAvatarSection(
            profile = uiState.profile,
            onEditAvatarClick = events.onEditAvatarClick,
        )

        EditProfileButton(onClick = events.onEditProfileClick)

        AccountInfoCard(
            profile = uiState.profile,
            onEditFieldClick = events.onEditFieldClick,
        )

        LogoutButton(
            isLoading = uiState.isLoggingOut,
            onClick = events.onLogoutClick,
        )

        Spacer(Modifier.height(8.dp))
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    //onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
//        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//            }
//        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier,
    )
}

// ─── Avatar Section ───────────────────────────────────────────────────────────

@Composable
private fun ProfileAvatarSection(
    profile: ProfileInfo,
    onEditAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Avatar with edit badge
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                if (profile.avatarUrl != null) {
                    AsyncImage(
                        model = profile.avatarUrl,
                        contentDescription = profile.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp),
                    )
                }
            }

            // Edit badge
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(DonAIRed),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = onEditAvatarClick,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit photo",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        // Name
        Text(
            text = profile.fullName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        // Blood type badge
        BloodDonorBadge(bloodType = profile.bloodType)
    }
}

@Composable
private fun BloodDonorBadge(
    bloodType: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = DonAIRed.copy(alpha = 0.10f),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = DonAIRed,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "$bloodType Blood Donor",
                style = MaterialTheme.typography.labelMedium,
                color = DonAIRed,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ─── Edit Profile Button ──────────────────────────────────────────────────────

@Composable
private fun EditProfileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(DonAIRed),
        ),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = DonAIRed,
        )
    }
}

// ─── Account Info Card ────────────────────────────────────────────────────────

@Composable
private fun AccountInfoCard(
    profile: ProfileInfo,
    onEditFieldClick: (ProfileField) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "ACCOUNT INFORMATION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
            letterSpacing = 1.sp,
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                ProfileInfoRow(
                    icon = Icons.Outlined.Person,
                    label = "Full Name",
                    value = profile.fullName,
                    editableField = ProfileField.FULL_NAME,
                    onEditClick = onEditFieldClick,
                )
                RowDivider()
                ProfileInfoRow(
                    icon = Icons.Outlined.Email,
                    label = "Email Address",
                    value = profile.email,
                    editableField = null,         // email not editable inline
                    onEditClick = onEditFieldClick,
                )
                RowDivider()
                ProfileInfoRow(
                    icon = Icons.Outlined.Bloodtype,
                    label = "Blood Type",
                    value = profile.bloodType,
                    editableField = null,
                    onEditClick = onEditFieldClick,
                )
                RowDivider()
                ProfileInfoRow(
                    icon = Icons.Outlined.LocationOn,
                    label = "Location",
                    value = profile.location,
                    editableField = ProfileField.LOCATION,
                    onEditClick = onEditFieldClick,
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    editableField: ProfileField?,
    onEditClick: (ProfileField) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(DonAIRed.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DonAIRed,
                modifier = Modifier.size(18.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                letterSpacing = 0.3.sp,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        if (editableField != null) {
            IconButton(
                onClick = { onEditClick(editableField) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit $label",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                    modifier = Modifier.size(16.dp),
                )
            }
        } else {
            // Map pin icon for location-style fields (non-editable inline)
            if (label == "Location") {
                Icon(
                    imageVector = Icons.Outlined.Map,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
    )
}

// ─── Logout Button ────────────────────────────────────────────────────────────

@Composable
private fun LogoutButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = DonAIRed,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                tint = DonAIRed,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = DonAIRed,
            )
        }
    }
}
