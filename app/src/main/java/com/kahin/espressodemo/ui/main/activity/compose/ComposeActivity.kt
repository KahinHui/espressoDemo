package com.kahin.espressodemo.ui.main.activity.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kahin.espressodemo.ui.main.activity.compose.conversation.LocalBackPressedDispatcher
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme
import kotlinx.coroutines.launch

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Provide WindowInsets to our content. We don't want to consume them, so that
        // they keep being pass down the view hierarchy (since we're using fragments).
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(
                    LocalBackPressedDispatcher provides onBackPressedDispatcher,
                ) {
                    ComposeApp()
                }
            }
        }
    }
}

@Composable
fun ComposeApp() {
    EspressoDemoTheme {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
        }

        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        // This top level scaffold contains the app drawer, which needs to be accessible
        // from multiple screens. An event to open the drawer is passed down to each
        // screen that needs it.
        val scaffoldState = rememberScaffoldState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.LOG_IN_ROUTE
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    toHome = { navController.navigate(MainDestinations.HOME_ROUTE) {
                        popUpTo(MainDestinations.HOME_ROUTE) {
                            inclusive = true
                        }
                    } },
                    logOut = { navController.navigate(MainDestinations.LOG_IN_ROUTE) {
                        popUpTo(MainDestinations.LOG_IN_ROUTE) {
                            inclusive = true
                        }
                    } }
                ) {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                }
            }
        ) {
            ComposeNavGraph(
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
    }
}