package com.ricven.richinsights.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ricven.richinsights.R

private data class NavigationItem(
    val destination: RichInsightsDestination,
    val label: String,
    val iconRes: Int,
)

private val primaryNavigationItems = listOf(
    NavigationItem(RichInsightsDestination.Home, "For You", R.drawable.ic_nav_for_you),
    NavigationItem(RichInsightsDestination.Learn, "Learn", R.drawable.ic_nav_learn),
    NavigationItem(RichInsightsDestination.Quiz, "Quiz", R.drawable.ic_nav_quiz),
    NavigationItem(RichInsightsDestination.Bible, "Bible", R.drawable.ic_nav_bible),
    NavigationItem(RichInsightsDestination.News, "News", R.drawable.ic_nav_news),
)

@Composable
fun RichInsightsNavigation() {
    val backStack = rememberNavBackStack(RichInsightsDestination.Home)
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface)
                    .statusBarsPadding(),
            ) {
                IconButton(
                    onClick = { backStack.add(RichInsightsDestination.Profile) },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_profile),
                        contentDescription = "Profile and Settings",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = colors.surface,
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                primaryNavigationItems.forEach { item ->
                    val selected = backStack.lastOrNull() == item.destination
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                while (backStack.size > 1) backStack.removeLastOrNull()
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
                                        color = if (selected) colors.secondaryContainer else Color.Transparent,
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
                            selectedIconColor = colors.onSurface,
                            selectedTextColor = colors.primary,
                            unselectedIconColor = colors.onSurfaceVariant,
                            unselectedTextColor = colors.onSurfaceVariant,
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
                if (backStack.size > 1) backStack.removeLastOrNull()
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
private fun RichInsightsDestinationPlaceholder(destination: RichInsightsDestination) {
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
    Text(text = title, color = MaterialTheme.colorScheme.onBackground)
}
