package com.ricven.richinsights

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.ricven.richinsights.navigation.RichInsightsNavigation
import com.ricven.richinsights.ui.splash.BrandIntroOverlay
import com.ricven.richinsights.ui.theme.RichInsightsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

private val Context.richInsightsDataStore by preferencesDataStore(name = "richinsights_settings")
private val brandIntroSeenKey = booleanPreferencesKey("brand_intro_seen")

class MainActivity : ComponentActivity() {
    private val splashStateReady = mutableStateOf(false)
    private val showBrandIntro = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !splashStateReady.value }
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val introSeen = withTimeoutOrNull(450L) {
                withContext(Dispatchers.IO) {
                    richInsightsDataStore.data.first()[brandIntroSeenKey] ?: false
                }
            }

            if (introSeen != null) {
                val animationsDisabled = Settings.Global.getFloat(
                    contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1f,
                ) == 0f

                showBrandIntro.value = !introSeen && !animationsDisabled

                if (!introSeen && animationsDisabled) {
                    runCatching {
                        richInsightsDataStore.edit { preferences ->
                            preferences[brandIntroSeenKey] = true
                        }
                    }
                }
            }

            splashStateReady.value = true
        }

        setContent {
            RichInsightsTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    RichInsightsNavigation()
                    if (showBrandIntro.value) {
                        BrandIntroOverlay(
                            onFinished = {
                                showBrandIntro.value = false
                                lifecycleScope.launch {
                                    runCatching {
                                        richInsightsDataStore.edit { preferences ->
                                            preferences[brandIntroSeenKey] = true
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
