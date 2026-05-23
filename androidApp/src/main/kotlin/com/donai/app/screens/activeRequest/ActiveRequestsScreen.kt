package com.donai.app.screens.activeRequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.donai.app.screens.dashboard.BloodRequest
import com.donai.app.screens.dashboard.RequestUrgency
import com.donai.app.theme.*

// ─── Model extensions ────────────────────────────────────────────────────────

data class ActiveBloodRequest(
    val request: BloodRequest,
    val imageUrl: String?,
    val distanceKm: Double,
)

enum class RequestTab { URGENT, NEARBY, SCHEDULED, ALL }

// ─── UI State (Moved to shared normally, but kept here for reference if local)

// ─── Root Screen Content ─────────────────────────────────────────────────────

/**
 * ActiveRequestsScreen content.
 * Does NOT contain a Scaffold; managed by MainScaffold in NavHost.
 */
@Composable
fun ActiveRequestsScreen(
    uiState: ActiveRequestsUiState,
    onTabSelected: (RequestTab) -> Unit,
    onDonateClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        RequestTabRow(
            selectedTab = uiState.selectedTab,
            onTabSelected = onTabSelected,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

        RequestList(
            requests = uiState.requests,
            isLoading = uiState.isLoading,
            onDonateClick = onDonateClick,
            listState = listState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

// ─── Top App Bar (Accessible for DonAINavHost) ───────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveRequestsTopBar(
    //onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Active Requests",
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
//                    tint = MaterialTheme.colorScheme.onSurface,
//                )
//            }
//        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
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

// ─── Internal Components ─────────────────────────────────────────────────────

private val ALL_TABS = RequestTab.entries

@Composable
private fun RequestTabRow(
    selectedTab: RequestTab,
    onTabSelected: (RequestTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScrollableTabRow(
        selectedTabIndex = ALL_TABS.indexOf(selectedTab),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = DonAIRed,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            val index = ALL_TABS.indexOf(selectedTab)
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                color = DonAIRed,
            )
        },
        divider = {},
        modifier = modifier,
    ) {
        ALL_TABS.forEach { tab ->
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
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            )
        }
    }
}

private val RequestTab.displayName: String
    get() = when (this) {
        RequestTab.URGENT    -> "Urgent"
        RequestTab.NEARBY    -> "Nearby"
        RequestTab.SCHEDULED -> "Scheduled"
        RequestTab.ALL       -> "All"
    }

@Composable
private fun RequestList(
    requests: List<ActiveBloodRequest>,
    isLoading: Boolean,
    onDonateClick: (String) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    when {
        isLoading -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DonAIRed)
            }
        }
        requests.isEmpty() -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Text(
                    text = "No requests in this category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }
        else -> {
            LazyColumn(
                state = listState,
                modifier = modifier,
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(
                    items = requests,
                    key = { it.request.id },
                ) { activeRequest ->
                    ActiveRequestCard(
                        activeRequest = activeRequest,
                        onDonateClick = { onDonateClick(activeRequest.request.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveRequestCard(
    activeRequest: ActiveBloodRequest,
    onDonateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val request = activeRequest.request

    Column(modifier = modifier.fillMaxWidth()) {
        RequestCardHeader(
            name = request.requesterName,
            hospital = request.hospital,
            bloodType = request.bloodType,
        )

        Spacer(Modifier.height(10.dp))

        RequestHeroImage(
            imageUrl = activeRequest.imageUrl,
            contentDescription = "${request.requesterName} at ${request.hospital}",
        )

        Spacer(Modifier.height(10.dp))

        RequestCardFooter(
            distanceKm = activeRequest.distanceKm,
            onDonateClick = onDonateClick,
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun RequestCardHeader(
    name: String,
    hospital: String,
    bloodType: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
                Text(
                    text = hospital,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        BloodTypePill(bloodType = bloodType)
    }
}

@Composable
private fun BloodTypePill(
    bloodType: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = DonAIRed,
        modifier = modifier,
    ) {
        Text(
            text = "TYPE $bloodType",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}

@Composable
private fun RequestHeroImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
        }
    }
}

@Composable
private fun RequestCardFooter(
    distanceKm: Double,
    onDonateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "DISTANCE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                letterSpacing = 0.8.sp,
            )
            Text(
                text = "$distanceKm km away",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        DonateButton(onClick = onDonateClick)
    }
}

@Composable
private fun DonateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = DonAIRed,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        modifier = modifier,
    ) {
        Text(
            text = "Donate",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
