package com.donai.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.donai.app.screens.activeRequest.ActiveRequestsScreen
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
import com.donai.app.screens.dashboard.HomeScreen
import com.donai.app.screens.dashboard.HomeViewModel
import com.donai.app.screens.elegibility.EligibilityEvents
import com.donai.app.screens.elegibility.EligibilityScreen
import com.donai.app.screens.elegibility.EligibilityViewModel
import com.donai.app.screens.history.DonationHistoryEvents
import com.donai.app.screens.history.DonationHistoryScreen
import com.donai.app.screens.history.DonationHistoryViewModel
import com.donai.app.screens.login.LoginEvents
import com.donai.app.screens.login.LoginScreen
import com.donai.app.screens.login.LoginViewModel
import com.donai.app.screens.profile.ProfileEvents
import com.donai.app.screens.profile.ProfileScreen
import com.donai.app.screens.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Single [NavHost] that owns the complete navigation graph.
 *
 * All navigation decisions (popBackStack, navigate, clearBackStack) live here —
 * screens receive only the lambdas they need and are never aware of [NavHostController].
 *
 * Start destination is [NavGraph.AUTH] so unauthenticated users always land on Login.
 * After a successful login the auth graph is cleared and replaced by [NavGraph.MAIN].
 */
@Composable
fun DonAINavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavGraph.AUTH,
        modifier = modifier,
    ) {

        // ── Auth Graph ────────────────────────────────────────────────────────
        navigation(
            startDestination = Screen.Login.route,
            route = NavGraph.AUTH,
        ) {

            // Login
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
                            vm.onLogin(
                                onSuccess = {
                                    navController.navigate(NavGraph.MAIN) {
                                        popUpTo(NavGraph.AUTH) { inclusive = true }
                                    }
                                }
                            )
                        },
                        onForgotPasswordClick = { /* TODO: forgot-password screen */ },
                        onCreateAccountClick = {
                            navController.navigate(Screen.CreateAccount.route)
                        },
                        onTermsClick = { /* TODO: open WebView/browser */ },
                        onPrivacyClick = { /* TODO: open WebView/browser */ },
                        onEmergencyNetworkClick = { /* TODO: emergency flow */ },
                    ),
                )
            }

            // Create Account
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
                            vm.onRegister(
                                onSuccess = {
                                    navController.navigate(NavGraph.MAIN) {
                                        popUpTo(NavGraph.AUTH) { inclusive = true }
                                    }
                                }
                            )
                        },
                        onGoogleSignInClick = vm::onGoogleSignIn,
                        onAppleSignInClick = vm::onAppleSignIn,
                        onBackToLoginClick = { navController.popBackStack() },
                        onTermsClick = { /* TODO */ },
                        onPrivacyClick = { /* TODO */ },
                    ),
                )
            }

            // ── Complete Profile (onboarding step 2) ──────────────────────────
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
                            vm.onFinish(
                                onSuccess = {
                                    // Clear the entire auth/onboarding stack before entering Main
                                    navController.navigate(NavGraph.MAIN) {
                                        popUpTo(NavGraph.AUTH) { inclusive = true }
                                    }
                                }
                            )
                        },
                    ),
                )
            }
        }

        // ── Main Graph ────────────────────────────────────────────────────────
        navigation(
            startDestination = Screen.Home.route,
            route = NavGraph.MAIN,
        ) {

            // Home
            composable(Screen.Home.route) {
                val vm: HomeViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                HomeScreen(
                    uiState = uiState,
                    onNotificationClick = { /* TODO: notifications screen */ },
                    onSeeAllRequestsClick = {
                        navController.navigate(Screen.ActiveRequests.route)
                    },
                    onRequestClick = {
                        navController.navigate(Screen.Eligibility.route)
                    },
                )
            }

            // Active Requests
            composable(Screen.ActiveRequests.route) {
                val vm: ActiveRequestsViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                ActiveRequestsScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onSearchClick = { /* TODO */ },
                    onTabSelected = vm::onTabSelected,
                    onDonateClick = {
                        navController.navigate(Screen.Eligibility.route)
                    },
                )
            }

            // Eligibility (Medical Screening)
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
                            vm.onConfirm(
                                onSuccess = {
                                    navController.navigate(Screen.DonationConfirmed.route) {
                                        // Remove eligibility from back-stack: confirmed → back = requests
                                        popUpTo(Screen.Eligibility.route) { inclusive = true }
                                    }
                                }
                            )
                        },
                    ),
                )
            }

            // Donation Confirmed
            composable(Screen.DonationConfirmed.route) {
                val vm: DonationConfirmedViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                DonationConfirmedScreen(
                    uiState = uiState,
                    events = DonationConfirmedEvents(
                        onCloseClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(NavGraph.MAIN) { inclusive = false }
                            }
                        },
                        onBackToDashboardClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(NavGraph.MAIN) { inclusive = false }
                            }
                        },
                    ),
                )
            }

            // Create Request
            composable(Screen.CreateRequest.route) {
                val vm: CreateRequestViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

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
                            vm.onSubmit(
                                onSuccess = {
                                    navController.navigate(Screen.DonationConfirmed.route)
                                }
                            )
                        },
                        onPrivacyPolicyClick = { /* TODO */ },
                    ),
                )
            }

            // Donation History
            composable(Screen.DonationHistory.route) {
                val vm: DonationHistoryViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                DonationHistoryScreen(
                    uiState = uiState,
                    events = DonationHistoryEvents(
                        onBackClick = { navController.popBackStack() },
                        onCalendarClick = { /* TODO */ },
                        onSearchQueryChange = vm::onSearchQueryChange,
                        onFilterClick = { /* TODO */ },
                        onTabSelected = vm::onTabSelected,
                        onRecordClick = { /* TODO: detail screen */ },
                    ),
                )
            }

            // Profile
            composable(Screen.Profile.route) {
                val vm: ProfileViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                ProfileScreen(
                    uiState = uiState,
                    events = ProfileEvents(
                        onBackClick = { navController.popBackStack() },
                        onSettingsClick = { /* TODO */ },
                        onEditAvatarClick = vm::onEditAvatarClick,
                        onEditProfileClick = { /* TODO */ },
                        onEditFieldClick = vm::onEditFieldClick,
                        onLogoutClick = {
                            vm.onLogout(
                                onSuccess = {
                                    navController.navigate(NavGraph.AUTH) {
                                        popUpTo(NavGraph.MAIN) { inclusive = true }
                                    }
                                }
                            )
                        },
                    ),
                )
            }
        }
    }
}