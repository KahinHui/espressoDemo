package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

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
            viewModel = MainViewModel()
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
    viewModel: MainViewModel// = viewModel()
) {

    val onPeopleChanged: (Int) -> Unit = { /*viewModel.updatePeople(it)*/ }

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
                exploreList = exploreList,
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
    val datesSelected = viewModel.datesSelected

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

private val exploreList = listOf(
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas"),
    ExploreModel(
        city = City("sz", "", "", ""),
        description = "ddd",
        imageUrl = ".sjdas")
)
