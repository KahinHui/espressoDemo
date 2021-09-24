package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.kahin.espressodemo.ui.main.activity.compose.calendar.data.DatesLocalDataSource
import com.kahin.espressodemo.ui.main.activity.compose.calendar.data.DatesRepository
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.DestinationsLocalDataSource
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.DestinationsRepository

@Composable
fun CraneHome(
    onExploreItemClicked: OnExploreItemClicked,
    onDateSelectionClicked: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val transitionState = remember { MutableTransitionState(SplashState.Shown) }
    val transition = updateTransition(targetState = transitionState, label = "splashTransition")
    val splashAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 100) }, label = "splashAlpha"
    ) {
        if (it.targetState == SplashState.Shown) 1f else 0f
    }
    val contentAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "splashAlpha"
    ) {
        if (it.targetState == SplashState.Shown) 0f else 1f
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {}
    ) {
        Box {
            LandingScreen(
                modifier = Modifier.alpha(splashAlpha)
            ) {
                transitionState.targetState = SplashState.Completed
            }
            CraneHomeContent(
                modifier = Modifier.alpha(contentAlpha),
                onExploreItemClicked = onExploreItemClicked,
                onDateSelectionClicked = onDateSelectionClicked,
                openDrawer = openDrawer,
                viewModel = MainViewModel(
                    DestinationsRepository(DestinationsLocalDataSource()),
                    DatesRepository(DatesLocalDataSource())
                )
            )
        }
    }
}

@Composable
private fun HomeTabBar(
    openDrawer: () -> Unit,
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    CraneTabBar(
        modifier,
        onMenuClicked = openDrawer
    ) { tabBarModifier ->
        CraneTabs(
            tabBarModifier,
            CraneScreen.values().map { it.name },
            tabSelected = tabSelected,
            onTabSelected = { newTab -> onTabSelected(CraneScreen.values()[newTab.ordinal]) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CraneHomeContent(
    onExploreItemClicked: OnExploreItemClicked,
    onDateSelectionClicked: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val onPeopleChanged: (Int) -> Unit = { viewModel.updatePeople(it) }
    var tabSelected by remember { mutableStateOf(CraneScreen.Eat) }

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        appBar = {
            HomeTabBar(
                openDrawer = openDrawer,
                tabSelected = tabSelected,
                onTabSelected = { tabSelected = it})
        },
        backLayerContent = {
            Text(
                text = "gkjshdfgjhdskl",
                modifier = Modifier.padding(20.dp)
            )
            SearchContent(
                viewModel = viewModel,
                onPeopleChanged = onPeopleChanged,
                onDateSelectionClicked = onDateSelectionClicked,
                onExploreItemClicked = onExploreItemClicked
            )
            Text(
                text = "gkjshdfgjhdskl",
                modifier = Modifier.padding(20.dp)
            )
        },
        frontLayerContent = {
            ExploreSection(
                title = "Explore Flights by Destination",
                exploreList = viewModel.restaurants,
                onItemClicked = {}
            )
        },
        frontLayerBackgroundColor = Color.Transparent,
        backLayerBackgroundColor = Color.Transparent
    )
}

@Composable
private fun SearchContent(
    viewModel: MainViewModel,
    onPeopleChanged: (Int) -> Unit,
    onDateSelectionClicked: () -> Unit,
    onExploreItemClicked: OnExploreItemClicked
) {
    // Reading datesSelected State from here instead of passing the String from the ViewModel
    // to cause a recomposition when the dates change.
    val datesSelected = viewModel.datesSelected.toString()

    EatSearchContent(
        datesSelected = datesSelected,
        eatUpdates = EatSearchContentUpdates(
            onPeopleChanged,
            onDateSelectionClicked,
            onExploreItemClicked
        )
    )
}

data class EatSearchContentUpdates(
    val onPeopleChanged: (Int) -> Unit,
    val onDateSelectionClicked: () -> Unit,
    val onExploreItemClicked: OnExploreItemClicked
)

enum class SplashState { Shown, Completed }