package com.donai.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.donai.app.components.BottomNavItem
import com.donai.app.components.DonAITopBar
import com.donai.app.components.MainScaffold
import com.donai.app.screens.activeRequest.ActiveRequestsScreen
import com.donai.app.screens.activeRequest.ActiveRequestsTopBar
import com.donai.app.screens.activeRequest.ActiveRequestsViewModel
import com.donai.app.screens.confirmed.DonationConfirmedEvents
import com.donai.app.screens.confirmed.DonationConfirmedScreen
import com.donai.app.screens.confirmed.DonationConfirmedViewModel
import com.donai.app.screens.createAccount.CreateAccountEvents
import com.donai.app.screens.createAccount.CreateAccountScreen
import com.donai.app.screens.createAccount.CreateAccountViewModel
import com.donai.app.screens.completeProfile.CompleteProfileEvents
import com.donai.app.screens.completeProfile.CompleteProfileScreen
import com.donai.app.screens.completeProfile.CompleteProfileViewModel
import com.donai.app.screens.createRequest.CreateRequestEvents
import com.donai.app.screens.createRequest.CreateRequestScreen
import com.donai.app.screens.createRequest.CreateRequestViewModel
import com.donai.app.screens.createRequest.CreateRequestTopBar
import com.donai.app.screens.dashboard.HomeScreen
import com.donai.app.screens.dashboard.HomeViewModel
import com.donai.app.screens.elegibility.EligibilityEvents
import com.donai.app.screens.elegibility.EligibilityScreen
import com.donai.app.screens.elegibility.EligibilityViewModel
import com.donai.app.screens.history.DonationHistoryEvents
import com.donai.app.screens.history.DonationHistoryScreen
import com.donai.app.screens.history.DonationHistoryViewModel
import com.donai.app.screens.history.HistoryTopBar
import com.donai.app.screens.login.LoginEvents
import com.donai.app.screens.login.LoginScreen
import com.donai.app.screens.login.LoginViewModel
import com.donai.app.screens.profile.ProfileEvents
import com.donai.app.screens.profile.ProfileScreen
import com.donai.app.screens.profile.ProfileTopBar
import com.donai.app.screens.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DonAINavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateToTab = { item: BottomNavItem ->
        val targetRoute = when (item) {
            BottomNavItem.HOME -> Screen.Home.route
            BottomNavItem.REQUESTS -> Screen.ActiveRequests.route
            BottomNavItem.CREATE -> Screen.CreateRequest.route
            BottomNavItem.HISTORY -> Screen.DonationHistory.route
            BottomNavItem.PROFILE -> Screen.Profile.route
        }
        if (currentRoute != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavGraph.AUTH,
        modifier = modifier,
    ) {
        // ─── AUTH GRAPH ──────────────────────────────────────────────────
        navigation(
            startDestination = Screen.Login.route,
            route = NavGraph.AUTH,
        ) {
            composable(Screen.Login.route) {
                val vm: LoginViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                LoginScreen(
                    uiState = uiState,
                    events = LoginEvents(
                        onEmailChange = vm::onEmailChange,
                        onPasswordChange = vm::onPasswordChange,
                        onPasswordVisibilityToggle = vm::togglePasswordVisibility,
                        onLoginClick = {
                            vm.onLogin {
                                navController.navigate(NavGraph.MAIN) {
                                    popUpTo(NavGraph.AUTH) { inclusive = true }
                                }
                            }
                        },
                        onCreateAccountClick = { navController.navigate(Screen.CreateAccount.route) },
                        onForgotPasswordClick = {},
                        onTermsClick = {},
                        onPrivacyClick = {},
                        onEmergencyNetworkClick = {},
                    )
                )
            }

            composable(Screen.CreateAccount.route) {
                val vm: CreateAccountViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                CreateAccountScreen(
                    uiState = uiState,
                    events = CreateAccountEvents(
                        onBackClick = { navController.popBackStack() },
                        onFullNameChange = vm::onFullNameChange,
                        onEmailChange = vm::onEmailChange,
                        onPasswordChange = vm::onPasswordChange,
                        onPasswordVisibilityToggle = vm::togglePasswordVisibility,
                        onTermsAcceptedChange = vm::onTermsAcceptedChange,
                        onRegisterClick = {
                            vm.onRegister {
                                navController.navigate(NavGraph.MAIN) {
                                    popUpTo(NavGraph.AUTH) { inclusive = true }
                                }
                            }
                        },
                        onGoogleSignInClick = vm::onGoogleSignIn,
                        onAppleSignInClick = vm::onAppleSignIn,
                        onBackToLoginClick = { navController.popBackStack() },
                        onTermsClick = {},
                        onPrivacyClick = {},
                    )
                )
            }

            composable(Screen.CompleteProfile.route) {
                val vm: CompleteProfileViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                CompleteProfileScreen(
                    uiState = uiState,
                    events = CompleteProfileEvents(
                        onFullNameChange = vm::onFullNameChange,
                        onBloodGroupSelected = vm::onBloodGroupSelected,
                        onLocationChange = vm::onLocationChange,
                        onLastDonationSelected = vm::onLastDonationSelected,
                        onFinishClick = {
                            vm.onFinish {
                                navController.navigate(NavGraph.MAIN) {
                                    popUpTo(NavGraph.AUTH) { inclusive = true }
                                }
                            }
                        },
                    )
                )
            }
        }

        // ─── MAIN GRAPH ──────────────────────────────────────────────────
        navigation(
            startDestination = Screen.Home.route,
            route = NavGraph.MAIN,
        ) {
            // TAB SCREENS: Each wrapped in MainScaffold
            composable(Screen.Home.route) {
                val vm: HomeViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selectedBottomItem = BottomNavItem.HOME,
                    onBottomItemSelected = navigateToTab,
                    topBar = {
                        DonAITopBar(
                            hasUnreadNotifications = uiState.hasUnreadNotifications,
                            onNotificationClick = {},
                        )
                    }
                ) { padding ->
                    HomeScreen(
                        uiState = uiState,
                        onNotificationClick = {},
                        onSeeAllRequestsClick = { navController.navigate(Screen.ActiveRequests.route) },
                        onRequestClick = { navController.navigate(Screen.Eligibility.route) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            composable(Screen.ActiveRequests.route) {
                val vm: ActiveRequestsViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selectedBottomItem = BottomNavItem.REQUESTS,
                    onBottomItemSelected = navigateToTab,
                    topBar = {
                        ActiveRequestsTopBar(
                            //onBackClick = { navController.popBackStack() },
                            onSearchClick = {},
                        )
                    }
                ) { padding ->
                    ActiveRequestsScreen(
                        uiState = uiState,
                        onTabSelected = vm::onTabSelected,
                        onDonateClick = { navController.navigate(Screen.Eligibility.route) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            composable(Screen.CreateRequest.route) {
                val vm: CreateRequestViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selectedBottomItem = BottomNavItem.CREATE,
                    onBottomItemSelected = navigateToTab,
                    topBar = {
                        CreateRequestTopBar(
                            //onBackClick = { navController.popBackStack() }
                        )
                    }
                ) { padding ->
                    CreateRequestScreen(
                        uiState = uiState,
                        events = CreateRequestEvents(
                            onBackClick = { navController.popBackStack() },
                            onReceiverNameChange = vm::onReceiverNameChange,
                            onHospitalChange = vm::onHospitalChange,
                            onBloodTypeSelected = vm::onBloodTypeSelected,
                            onDonorsIncrement = vm::onDonorsIncrement,
                            onDonorsDecrement = vm::onDonorsDecrement,
                            onUrgentToggle = vm::onUrgentToggle,
                            onSubmit = {
                                vm.onSubmit {
                                    navController.navigate(Screen.DonationConfirmed.route)
                                }
                            },
                            onPrivacyPolicyClick = {},
                        ),
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            composable(Screen.DonationHistory.route) {
                val vm: DonationHistoryViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selectedBottomItem = BottomNavItem.HISTORY,
                    onBottomItemSelected = navigateToTab,
                    topBar = {
                        HistoryTopBar(
                            //onBackClick = { navController.popBackStack() },
                            onCalendarClick = {},
                        )
                    }
                ) { padding ->
                    DonationHistoryScreen(
                        uiState = uiState,
                        events = DonationHistoryEvents(
                            onBackClick = { navController.popBackStack() },
                            onCalendarClick = {},
                            onSearchQueryChange = vm::onSearchQueryChange,
                            onFilterClick = {},
                            onTabSelected = vm::onTabSelected,
                            onRecordClick = {},
                        ),
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            composable(Screen.Profile.route) {
                val vm: ProfileViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selectedBottomItem = BottomNavItem.PROFILE,
                    onBottomItemSelected = navigateToTab,
                    topBar = {
                        ProfileTopBar(
                            //onBackClick = { navController.popBackStack() },
                            onSettingsClick = {},
                        )
                    }
                ) { padding ->
                    ProfileScreen(
                        uiState = uiState,
                        events = ProfileEvents(
                            onBackClick = { navController.popBackStack() },
                            onSettingsClick = {},
                            onEditAvatarClick = vm::onEditAvatarClick,
                            onEditProfileClick = {},
                            onEditFieldClick = vm::onEditFieldClick,
                            onLogoutClick = {
                                vm.onLogout {
                                    navController.navigate(NavGraph.AUTH) {
                                        popUpTo(NavGraph.MAIN) { inclusive = true }
                                    }
                                }
                            },
                        ),
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            // FULLSCREEN SCREENS: Directly rendered without MainScaffold
            composable(Screen.Eligibility.route) {
                val vm: EligibilityViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                EligibilityScreen(
                    uiState = uiState,
                    events = EligibilityEvents(
                        onBackClick = { navController.popBackStack() },
                        onQuestionChecked = vm::onQuestionChecked,
                        onDonationOptionSelected = vm::onDonationOptionSelected,
                        onConfirmClick = {
                            vm.onConfirm {
                                navController.navigate(Screen.DonationConfirmed.route) {
                                    popUpTo(Screen.Eligibility.route) { inclusive = true }
                                }
                            }
                        },
                    )
                )
            }

            composable(Screen.DonationConfirmed.route) {
                val vm: DonationConfirmedViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                DonationConfirmedScreen(
                    uiState = uiState,
                    events = DonationConfirmedEvents(
                        onCloseClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(NavGraph.MAIN)
                            }
                        },
                        onBackToDashboardClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(NavGraph.MAIN)
                            }
                        },
                    )
                )
            }
        }
    }
}
