package com.donai.app.navigation

///**
// * Sealed hierarchy that represents every navigation destination in the app.
// *
// * Convention:
// *  - [route] is the stable string key used by NavController / NavHost.
// *  - Path parameters are embedded with {param} syntax.
// *  - Companion helpers (e.g. [ActiveRequests.buildRoute]) construct the
// *    concrete route string when arguments are needed, keeping the call-site
// *    readable and free of string concatenation.
// *
// * Two root graphs:
// *  ├── Auth graph  (Login, CreateAccount)            → not nested in bottom-nav
// *  └── Main graph  (Home, Requests, Create, History, Profile, sub-screens)
// */
sealed class Screen(val route: String) {

    // ── Auth ──────────────────────────────────────────────────────────────────

    data object Login : Screen("login")
    data object CreateAccount : Screen("create_account")

    /**
     * Shown immediately after [CreateAccount].
     * Part of [NavGraph.AUTH] so the entire onboarding stack is popped
     * in a single call when the user reaches [NavGraph.MAIN].
     */
    data object CompleteProfile : Screen("complete_profile")

    // ── Main – bottom-nav roots ───────────────────────────────────────────────

    data object Home : Screen("home")
    data object ActiveRequests : Screen("active_requests")
    data object CreateRequest : Screen("create_request")
    data object DonationHistory : Screen("donation_history")
    data object Profile : Screen("profile")

    // ── Main – sub-screens ────────────────────────────────────────────────────

    data object Eligibility : Screen("eligibility")
    data object DonationConfirmed : Screen("donation_confirmed")
}

// ─── Named Nav Graphs ─────────────────────────────────────────────────────────

object NavGraph {
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}