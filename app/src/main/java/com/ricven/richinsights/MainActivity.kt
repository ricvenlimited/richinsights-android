package com.ricven.richinsights

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.launch

private val Context.richInsightsDataStore by preferencesDataStore(
    name = "richinsights_settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)
private val brandIntroSeenKey = booleanPreferencesKey("brand_intro_seen")

private enum class StartupState {
    Intro,
    Home,
}

class MainActivity : ComponentActivity() {
    private val startupState = mutableStateOf(StartupState.Intro)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            false
        }
        super.onCreate(savedInstanceState)

        setContent {
            RichInsightsTheme {
                when (startupState.value) {
                    StartupState.Intro -> {
                        // Back is consumed while the recurring brand intro is playing.
                        BackHandler(enabled = true) {}

                        BrandIntroOverlay(
                            onSequenceCompleted = {
                                // Persistence is best-effort only. It must never block Home.
                                lifecycleScope.launch(Dispatchers.IO) {
                                    repeat(3) { attempt ->
                                        try {
                                            richInsightsDataStore.edit { preferences ->
                                                preferences[brandIntroSeenKey] = true
                                            }
                                            return@launch
                                        } catch (_: Exception) {
                                            if (attempt < 2) {
                                                delay(1_000L)
                                            }
                                        }
                                    }
                                }
                            },
                            onFinished = {
                                startupState.value = StartupState.Home
                            },
                        )
                    }

                    StartupState.Home -> RichInsightsNavigation()
                }
            }
        }
    }
}
