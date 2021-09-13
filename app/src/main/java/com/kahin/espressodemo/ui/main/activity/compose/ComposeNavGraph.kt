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
import com.kahin.espressodemo.ui.main.activity.compose.conversation.ConversationScreen
import com.kahin.espressodemo.ui.main.fragment.FragmentActivity
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val LOG_IN_ROUTE = "logIn"
    const val CONVERSATION = "conversation"
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
            ConversationScreen(/*onNavigationEvent = actions*/)

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
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}