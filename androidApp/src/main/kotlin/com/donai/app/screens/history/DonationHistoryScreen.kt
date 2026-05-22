package com.donai.app.screens.history

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import coil3.compose.AsyncImage
import com.donai.app.components.BottomNavDestination
import com.donai.app.components.DonAIBottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.theme.*

// ─── Model ────────────────────────────────────────────────────────────────────

data class DonationRecord(
    val id: String,
    val dateLabel: String,          // e.g. "OCT 12, 2023"
    val hospitalName: String,
    val receiverOrNote: String,     // e.g. "Receiver: Sarah Jenkins"
    val bloodType: String,          // e.g. "B+"
    val isVerified: Boolean = true,
    val testimonialText: String? = null,
    val testimonialAvatarUrl: String? = null,
)

data class DonationHistoryGroup(
    val monthYear: String,          // e.g. "OCTOBER 2023"
    val records: List<DonationRecord>,
)

enum class HistoryTab { SCHEDULED, PAST_DONATIONS }

@Composable
fun DonationHistoryScreen(
    uiState: DonationHistoryUiState,
    events: DonationHistoryEvents,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            HistoryTopBar(
                onBackClick = events.onBackClick,
                onCalendarClick = events.onCalendarClick,
            )
        },
        bottomBar = {
            DonAIBottomBar(selectedItem = BottomNavDestination.HISTORY)
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            // Search bar
            HistorySearchBar(
                query = uiState.searchQuery,
                onQueryChange = events.onSearchQueryChange,
                onFilterClick = events.onFilterClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )

            // Tabs
            HistoryTabRow(
                selectedTab = uiState.selectedTab,
                onTabSelected = events.onTabSelected,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            // Content
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DonAIRed)
                    }
                }
                uiState.groups.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No donations found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        )
                    }
                }
                else -> {
                    DonationGroupList(
                        groups = uiState.groups,
                        onRecordClick = events.onRecordClick,
                    )
                }
            }
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryTopBar(
    onBackClick: () -> Unit,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Donation History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onCalendarClick) {
                Icon(Icons.Outlined.CalendarMonth, contentDescription = "Calendar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier,
    )
}

// ─── Search Bar ───────────────────────────────────────────────────────────────

@Composable
private fun HistorySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Search hospital or receiver",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    modifier = Modifier.size(20.dp),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DonAIRed,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Icon(
                imageVector = Icons.Filled.Tune,
                contentDescription = "Filters",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

// ─── Tab Row ──────────────────────────────────────────────────────────────────

private val ALL_HISTORY_TABS = HistoryTab.entries

@Composable
private fun HistoryTabRow(
    selectedTab: HistoryTab,
    onTabSelected: (HistoryTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTabIndex = ALL_HISTORY_TABS.indexOf(selectedTab),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = DonAIRed,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[ALL_HISTORY_TABS.indexOf(selectedTab)]
                ),
                color = DonAIRed,
            )
        },
        divider = {},
        modifier = modifier,
    ) {
        ALL_HISTORY_TABS.forEach { tab ->
            val selected = tab == selectedTab
            Tab(
                selected = selected,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                selectedContentColor = DonAIRed,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

private val HistoryTab.displayName: String
    get() = when (this) {
        HistoryTab.SCHEDULED      -> "Scheduled"
        HistoryTab.PAST_DONATIONS -> "Past Donations"
    }

// ─── Grouped List ─────────────────────────────────────────────────────────────

@Composable
private fun DonationGroupList(
    groups: List<DonationHistoryGroup>,
    onRecordClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        groups.forEach { group ->
            // Month-year header
            item(key = "header_${group.monthYear}") {
                Text(
                    text = group.monthYear,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DonAIRed,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
                )
            }
            // Records
            items(group.records, key = { it.id }) { record ->
                DonationRecordItem(
                    record = record,
                    onClick = { onRecordClick(record.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        }
    }
}

// ─── Donation Record Item ─────────────────────────────────────────────────────

@Composable
private fun DonationRecordItem(
    record: DonationRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Date
        Text(
            text = record.dateLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        // Main row: hospital + blood type + verified badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.hospitalName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = record.receiverOrNote,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                BloodTypeBadge(bloodType = record.bloodType)
                if (record.isVerified) VerifiedBadge()
            }
        }

        // Testimonial (optional)
        record.testimonialText?.let { text ->
            Spacer(Modifier.height(8.dp))
            TestimonialNote(
                text = text,
                avatarUrl = record.testimonialAvatarUrl,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        )
    }
}

@Composable
private fun BloodTypeBadge(
    bloodType: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = DonAIRed,
        modifier = modifier,
    ) {
        Text(
            text = bloodType,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

@Composable
private fun VerifiedBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = EligibleGreenDark,
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = "Verified",
            style = MaterialTheme.typography.labelSmall,
            color = EligibleGreenDark,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun TestimonialNote(
    text: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Small avatar thumbnail
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Text(
            text = "\u201C$text\u201D",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f),
        )
    }
}