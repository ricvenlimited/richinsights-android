package com.ricven.richinsights

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.ricven.richinsights.navigation.RichInsightsNavigation
import com.ricven.richinsights.ui.splash.BrandIntroOverlay
import com.ricven.richinsights.ui.theme.RichInsightsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.richInsightsDataStore by preferencesDataStore(
    name = "richinsights_settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)
private val brandIntroSeenKey = booleanPreferencesKey("brand_intro_seen")

private enum class StartupState {
    DeterminingLaunchMode,
    FirstLaunchBrand,
    Home,
}

private val RoyalNavy = Color(0xFF0B2970)

class MainActivity : ComponentActivity() {
    private val startupState = mutableStateOf(StartupState.DeterminingLaunchMode)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            startupState.value == StartupState.DeterminingLaunchMode
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Do not replay the first-launch brand sequence for an Activity recreation.
        if (savedInstanceState != null) {
            startupState.value = StartupState.Home
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val launchState = try {
                    val seen = richInsightsDataStore.data.first()[brandIntroSeenKey] ?: false
                    if (seen) StartupState.Home else StartupState.FirstLaunchBrand
                } catch (_: Exception) {
                    // This preference is non-critical: a read failure must not block Home.
                    StartupState.Home
                }

                withContext(Dispatchers.Main.immediate) {
                    startupState.value = launchState
                }
            }
        }

        setContent {
            RichInsightsTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Compose the app shell before revealing it, so the first-launch
                    // fade reveals real content instead of an empty placeholder.
                    RichInsightsNavigation()

                    when (startupState.value) {
                        StartupState.DeterminingLaunchMode -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(RoyalNavy),
                            )
                        }

                        StartupState.FirstLaunchBrand -> {
                            BrandIntroOverlay(
                                showMark = true,
                                onSequenceCompleted = { persistBrandIntroSeen() },
                                onFinished = { startupState.value = StartupState.Home },
                            )
                        }

                        StartupState.Home -> Unit
                    }
                }
            }
        }
    }

    private fun persistBrandIntroSeen() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeat(3) { attempt ->
                try {
                    richInsightsDataStore.edit { preferences ->
                        preferences[brandIntroSeenKey] = true
                    }
                    return@launch
                } catch (_: Exception) {
                    if (attempt < 2) delay(1_000L)
                }
            }
        }
    }
}
