package com.donai.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.donai.app.navigation.DonAINavHost
import com.donai.app.theme.DonAITheme

/**
 * Root composable.
 *
 * Owns:
 *  - The [DonAITheme] wrapper (light/dark resolved from system + optional user override)
 *  - The [NavHostController] that lives as long as the composition
 *  - The single [DonAINavHost] entry point
 *
 * Nothing else lives here — screen composition, business logic and state
 * all belong to the NavHost / ViewModels.
 */
@Composable
fun App(
    // Allow the host activity (or tests) to force a theme mode.
    // null → follow system setting.
    forceDarkTheme: Boolean? = null,
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = forceDarkTheme ?: systemDark

    DonAITheme(darkTheme = darkTheme) {
        val navController = rememberNavController()
        DonAINavHost(navController = navController)
    }
}