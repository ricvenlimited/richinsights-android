package com.ricven.richinsights

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

private val DeepNavy = Color(0xFF102A43)
private val Context.richInsightsDataStore by preferencesDataStore(
    name = "richinsights_settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)
private val brandIntroSeenKey = booleanPreferencesKey("brand_intro_seen")

private enum class StartupState {
    Loading,
    Intro,
    Home,
}

class MainActivity : ComponentActivity() {
    private val startupState = mutableStateOf(StartupState.Loading)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            startupState.value == StartupState.Loading
        }
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val introSeen = try {
                withTimeoutOrNull(1_000L) {
                    withContext(Dispatchers.IO) {
                        richInsightsDataStore.data.first()[brandIntroSeenKey] ?: false
                    }
                } ?: false
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                // Unknown state must fail safe: show the intro, never bypass it.
                false
            }

            startupState.value = if (introSeen) StartupState.Home else StartupState.Intro
        }

        setContent {
            RichInsightsTheme {
                when (startupState.value) {
                    StartupState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DeepNavy),
                        )
                    }

                    StartupState.Intro -> {
                        // Back is consumed while the compulsory intro is playing.
                        BackHandler(enabled = true) {}

                        BrandIntroOverlay(
                            onSequenceCompleted = {
                                // Do not unlock Home until completion is durably saved.
                                while (true) {
                                    try {
                                        richInsightsDataStore.edit { preferences ->
                                            preferences[brandIntroSeenKey] = true
                                        }
                                        break
                                    } catch (cancelled: CancellationException) {
                                        throw cancelled
                                    } catch (_: Exception) {
                                        // Keep the completed intro visible and retry local persistence.
                                        delay(1_000L)
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
