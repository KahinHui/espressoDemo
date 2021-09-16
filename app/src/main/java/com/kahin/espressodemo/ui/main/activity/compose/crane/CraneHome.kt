package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        appBar = { /*TODO*/ },
        backLayerContent = {
            Text(text = "gkjshdfgjhdskl")
            Spacer(modifier = Modifier.height(300.dp))
            Text(text = "gkjshdfgjhdskl")
        },
        frontLayerContent = {
            ExploreSection(
                title = "Explore Flights by Destination",
                exploreList = listOf(
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
                ),
                onItemClicked = {}
            )
        }
    )
}