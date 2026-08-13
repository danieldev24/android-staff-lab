package com.krahs.androidstafflab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.krahs.androidstafflab.feature.library.TopicLibraryScreen
import com.krahs.androidstafflab.feature.library.Topics
import com.krahs.androidstafflab.feature.startup.ui.ApplicationStartupScreen
import kotlinx.serialization.Serializable

@Serializable
data object TopicLibraryRoute

@Serializable
data class TopicDetailRoute(val topicId: String)

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = TopicLibraryRoute,
        modifier = modifier,
    ) {
        composable<TopicLibraryRoute> {
            TopicLibraryScreen(
                topic = Topics.applicationStartup,
                onTopicClick = { topic ->
                    navController.navigate(TopicDetailRoute(topic.id))
                },
            )
        }
        composable<TopicDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<TopicDetailRoute>()
            if (route.topicId == Topics.applicationStartup.id) {
                ApplicationStartupScreen()
            }
        }
    }
}
