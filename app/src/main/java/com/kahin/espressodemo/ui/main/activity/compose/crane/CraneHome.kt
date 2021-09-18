package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.kahin.espressodemo.ui.main.activity.compose.calendar.data.DatesLocalDataSource
import com.kahin.espressodemo.ui.main.activity.compose.calendar.data.DatesRepository
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.City
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.DestinationsLocalDataSource
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.DestinationsRepository
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.ExploreModel

@Composable
fun CraneHome(
    onExploreItemClicked: OnExploreItemClicked,
    onDateSelectionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {}
    ) {
        CraneHomeContent(
            onExploreItemClicked = onExploreItemClicked,
            onDateSelectionClicked = onDateSelectionClicked,
            openDrawer = { /*TODO*/ },
            viewModel = MainViewModel(
                DestinationsRepository(DestinationsLocalDataSource()),
                DatesRepository(DatesLocalDataSource())
            )
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

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        appBar = { /*TODO*/ },
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
