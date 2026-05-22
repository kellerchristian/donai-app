package com.donai.app.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─── Brand Colors ────────────────────────────────────────────────────────────

val DonAIRed        = Color(0xFFCC1B1B)
val UrgentRed       = Color(0xFFD32F2F)
val HighOrange      = Color(0xFFE65100)
val MediumYellow    = Color(0xFFF9A825)
val EligibleGreen   = Color(0xFF4CAF50)
val EligibleGreenDark = Color(0xFF2E7D32)

// ─── Light palette ───────────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary          = DonAIRed,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),

    secondary        = Color(0xFF9C4146),
    onSecondary      = Color.White,
    secondaryContainer = Color(0xFFFFDAD9),
    onSecondaryContainer = Color(0xFF40000A),

    surface          = Color(0xFFFFFFFF),
    onSurface        = Color(0xFF1A1A1A),
    surfaceVariant   = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF5A5A5A),

    background       = Color(0xFFF8F8F8),
    onBackground     = Color(0xFF1A1A1A),

    error            = Color(0xFFBA1A1A),
    errorContainer   = Color(0xFFFFDAD6),
    onError          = Color.White,
    onErrorContainer = Color(0xFF410002),

    outline          = Color(0xFFDEDEDE),
)

// ─── Dark palette ────────────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary          = Color(0xFFFFB4AB),
    onPrimary        = Color(0xFF690005),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),

    secondary        = Color(0xFFFFB3B4),
    onSecondary      = Color(0xFF5F1118),
    secondaryContainer = Color(0xFF7B292F),
    onSecondaryContainer = Color(0xFFFFDAD9),

    surface          = Color(0xFF1C1C1E),
    onSurface        = Color(0xFFECECEC),
    surfaceVariant   = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFAAAAAA),

    background       = Color(0xFF121212),
    onBackground     = Color(0xFFECECEC),

    error            = Color(0xFFFFB4AB),
    errorContainer   = Color(0xFF93000A),
    onError          = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),

    outline          = Color(0xFF3A3A3C),
)

// ─── Typography ──────────────────────────────────────────────────────────────

private val DonAITypography = Typography()  // extend with custom fonts as needed

// ─── Theme entry point ───────────────────────────────────────────────────────

@Composable
fun DonAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,          // opt-out of Material You for brand consistency
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // Dynamic color available on Android 12+
            // val context = LocalContext.current
            // if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = DonAITypography,
        content     = content,
    )
}
