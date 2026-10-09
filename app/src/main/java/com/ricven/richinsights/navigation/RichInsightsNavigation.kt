package com.ricven.richinsights.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ricven.richinsights.R
import com.ricven.richinsights.ui.theme.RichInsightsCyan
import com.ricven.richinsights.ui.theme.RichInsightsDeepNavy
import com.ricven.richinsights.ui.theme.RichInsightsSecondaryText
import com.ricven.richinsights.ui.theme.RichInsightsSurfaceAccent

private data class NavigationItem(
    val destination: RichInsightsDestination,
    val label: String,
    val iconRes: Int,
    val preserveBrandColors: Boolean = false,
)

private val primaryNavigationItems = listOf(
    NavigationItem(RichInsightsDestination.Home, "For You", R.drawable.ic_nav_for_you, preserveBrandColors = true),
    NavigationItem(RichInsightsDestination.Learn, "Learn", R.drawable.ic_nav_learn),
    NavigationItem(RichInsightsDestination.Quiz, "Quiz", R.drawable.ic_nav_quiz),
    NavigationItem(RichInsightsDestination.Bible, "Bible", R.drawable.ic_nav_bible),
    NavigationItem(RichInsightsDestination.News, "News", R.drawable.ic_nav_news),
)

@Composable
fun RichInsightsNavigation() {
    val backStack = rememberNavBackStack(RichInsightsDestination.Home)

    Scaffold(
        topBar = {
            IconButton(
                onClick = {
                    backStack.add(RichInsightsDestination.Profile)
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_profile),
                    contentDescription = "Profile and Settings",
                    tint = Color.Unspecified,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                primaryNavigationItems.forEach { item ->
                    val selected = backStack.lastOrNull() == item.destination
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                // Treat bottom destinations as peer top-level screens.
                                // Clear transient destinations before switching tabs.
                                while (backStack.size > 1) {
                                    backStack.removeLastOrNull()
                                }
                                if (backStack.lastOrNull() != item.destination) {
                                    backStack.add(item.destination)
                                }
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp),
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(item.iconRes),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RichInsightsDeepNavy,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = RichInsightsSecondaryText,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent,
                        ),
                    )
                }
            }
        },
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = { key ->
                when (key) {
                    is RichInsightsDestination -> NavEntry(key) {
                        RichInsightsDestinationPlaceholder(destination = key)
                    }
                    else -> error("Unsupported navigation key: $key")
                }
            },
        )
    }
}

@Composable
private fun RichInsightsDestinationPlaceholder(
    destination: RichInsightsDestination,
) {
    val title = remember(destination) {
        when (destination) {
            RichInsightsDestination.Home -> "For You"
            RichInsightsDestination.Learn -> "Learn"
            RichInsightsDestination.Quiz -> "Quiz"
            RichInsightsDestination.Bible -> "Bible"
            RichInsightsDestination.News -> "News"
            RichInsightsDestination.Profile -> "Profile & Settings"
        }
    }

    Text(text = title)
}
