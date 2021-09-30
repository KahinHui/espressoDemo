package com.kahin.espressodemo.ui.main.activity.compose

import android.content.Context
import android.content.Intent
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kahin.espressodemo.ui.main.activity.compose.calendar.launchCalendarActivity
import com.kahin.espressodemo.ui.main.activity.compose.conversation.ConversationScreen
import com.kahin.espressodemo.ui.main.activity.compose.crane.CraneHome
import com.kahin.espressodemo.ui.main.activity.compose.podcast.PodcastCategory
import com.kahin.espressodemo.ui.main.activity.compose.profile.ProfileScreen
import com.kahin.espressodemo.ui.main.activity.compose.profile.meProfile
import com.kahin.espressodemo.ui.main.fragment.FragmentActivity
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val LOG_IN_ROUTE = "logIn"
    const val CONVERSATION = "conversation"
    const val PROFILE = "profile"
    const val PLACE = "place"
    const val PODCAST = "podcast"
}

@Composable
fun ComposeNavGraph(
    context: Context?,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.LOG_IN_ROUTE
) {
    val actions = remember(navController) { MainActions(context, navController) }
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() }}

    NavHost(navController = navController, startDestination = startDestination) {
        composable(MainDestinations.LOG_IN_ROUTE) {
            LogInScreen(actions)
        }
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                actions,
                openDrawer = openDrawer
            )
        }
        composable(MainDestinations.CONVERSATION) {
            ConversationScreen(onNavigationEvent = actions, openDrawer = openDrawer)
        }
        composable("${MainDestinations.PROFILE}/{userId}") { backStackEntry ->
            ProfileScreen(backStackEntry.arguments?.let { it.getString("userId") } ?: meProfile.userId)
        }
        composable(MainDestinations.PLACE) {
            CraneHome(
                onExploreItemClicked = {},
                onDateSelectionClicked = {
                    context?.let {
                        launchCalendarActivity(context)
                    }
                },
                openDrawer = openDrawer
            )
        }
        composable(MainDestinations.PODCAST) {
            PodcastCategory(categoryId = 0)
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(context: Context?, navController: NavHostController) {
    val toHome: () -> Unit = {
        navController.navigate(MainDestinations.HOME_ROUTE)
    }
    val logOut: () -> Unit = {
        navController.navigate(MainDestinations.LOG_IN_ROUTE) {
            popUpTo(MainDestinations.LOG_IN_ROUTE) {
                inclusive = true
            }
        }
    }
    val chat: () -> Unit = {
        navController.navigate(MainDestinations.CONVERSATION)
//        context?.startActivity(Intent(context, FragmentActivity::class.java))
    }
    val profile: (String) -> Unit = { userId ->
        navController.navigate("${MainDestinations.PROFILE}/$userId")
    }
    val place: () -> Unit = {
        navController.navigate(MainDestinations.PLACE)
    }
    val podcast: () -> Unit = {
        navController.navigate(MainDestinations.PODCAST)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}