package com.ricven.richinsights.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

private data class NavigationItem(
    val destination: RichInsightsDestination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val primaryNavigationItems = listOf(
    NavigationItem(RichInsightsDestination.Home, "Home", Icons.Filled.Home),
    NavigationItem(RichInsightsDestination.Learn, "Learn", Icons.Filled.AutoStories),
    NavigationItem(RichInsightsDestination.Quiz, "Quiz", Icons.Filled.Quiz),
    NavigationItem(RichInsightsDestination.Bible, "Bible", Icons.Filled.Book),
    NavigationItem(RichInsightsDestination.News, "News", Icons.Filled.Newspaper),
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
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile and Settings",
                )
            }
        },
        bottomBar = {
            NavigationBar {
                primaryNavigationItems.forEach { item ->
                    NavigationBarItem(
                        selected = backStack.lastOrNull() == item.destination,
                        onClick = {
                            if (backStack.lastOrNull() != item.destination) {
                                backStack.add(item.destination)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                            )
                        },
                        label = { Text(item.label) },
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
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is RichInsightsDestination -> NavEntry(key) {
                        RichInsightsDestinationPlaceholder(
                            destination = key,
                        )
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
            RichInsightsDestination.Home -> "Home"
            RichInsightsDestination.Learn -> "Learn"
            RichInsightsDestination.Quiz -> "Quiz"
            RichInsightsDestination.Bible -> "Bible"
            RichInsightsDestination.News -> "News"
            RichInsightsDestination.Profile -> "Profile & Settings"
        }
    }

    Text(text = title)
}
