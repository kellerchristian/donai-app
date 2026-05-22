package com.donai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.androidx.compose.KoinAndroidContext

/**
 * Single Activity. Enables edge-to-edge rendering so Compose owns
 * the full window including status bar and navigation bar insets
 * (handled via Scaffold's innerPadding in every screen).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                App() // tu composable root
            }
        }
    }
}