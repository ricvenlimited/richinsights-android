package com.ricven.richinsights.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ricven.richinsights.R
import com.ricven.richinsights.ui.theme.RichInsightsDivider

private data class NavigationItem(
    val destination: RichInsightsDestination,
    val label: String,
    val outlineIconRes: Int,
    val filledIconRes: Int,
)

private val primaryNavigationItems = listOf(
    NavigationItem(RichInsightsDestination.Home, "For You", R.drawable.ic_nav_for_you_outline, R.drawable.ic_nav_for_you_filled),
    NavigationItem(RichInsightsDestination.Learn, "Learn", R.drawable.ic_nav_learn_outline, R.drawable.ic_nav_learn_filled),
    NavigationItem(RichInsightsDestination.Quiz, "Quiz", R.drawable.ic_nav_quiz_outline, R.drawable.ic_nav_quiz_filled),
    NavigationItem(RichInsightsDestination.Bible, "Bible", R.drawable.ic_nav_bible_outline, R.drawable.ic_nav_bible_filled),
    NavigationItem(RichInsightsDestination.News, "News", R.drawable.ic_nav_news_outline, R.drawable.ic_nav_news_filled),
)

@Composable
fun RichInsightsNavigation() {
    val backStack = rememberNavBackStack(RichInsightsDestination.Home)
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.fillMaxWidth().background(colors.surface).statusBarsPadding(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val title = primaryNavigationItems.firstOrNull { it.destination == backStack.lastOrNull() }?.label
                        ?: "Profile & Settings"
                    Text(
                        text = title,
                        modifier = Modifier.weight(1f).padding(start = 16.dp),
                        color = colors.onSurface,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(
                        onClick = { backStack.add(RichInsightsDestination.Profile) },
                        modifier = Modifier.padding(end = 4.dp).size(48.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_nav_profile_outline),
                            contentDescription = "Profile and Settings",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(RichInsightsDivider))
            }
        },
        bottomBar = {
            androidx.compose.foundation.layout.Column {
                Box(Modifier.fillMaxWidth().height(1.dp).background(RichInsightsDivider))
                NavigationBar(containerColor = colors.surface) {
                    primaryNavigationItems.forEach { item ->
                        val selected = backStack.lastOrNull() == item.destination
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    while (backStack.size > 1) backStack.removeLastOrNull()
                                    if (backStack.lastOrNull() != item.destination) backStack.add(item.destination)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(if (selected) item.filledIconRes else item.outlineIconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colors.primary,
                                selectedTextColor = colors.primary,
                                unselectedIconColor = colors.onSurfaceVariant,
                                unselectedTextColor = colors.onSurfaceVariant,
                                indicatorColor = Color.Transparent,
                            ),
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is RichInsightsDestination -> NavEntry(key) { RichInsightsDestinationPlaceholder(destination = key) }
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
