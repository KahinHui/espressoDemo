package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsHeight
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.City
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.ExploreModel
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.BottomSheetShape
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.crane_caption
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.crane_divider_color

typealias OnExploreItemClicked = (ExploreModel) -> Unit

@Composable
fun ExploreSection(
    modifier: Modifier = Modifier,
    title: String,
    exploreList: List<ExploreModel>,
    onItemClicked: OnExploreItemClicked
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        shape = BottomSheetShape
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, top = 20.dp, end = 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption.copy(crane_caption)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(exploreList) { exploreItem ->
                    Column(Modifier.fillMaxWidth()) {
                        ExploreItem(
                            item = exploreItem,
                            onItemClicked = onItemClicked,
                            modifier = Modifier.fillParentMaxWidth()
                        )
                        Divider(color = crane_divider_color)
                    }
                }
                item {
                    Spacer(modifier = Modifier.navigationBarsHeight())
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ExploreItem(
    modifier: Modifier = Modifier,
    item: ExploreModel,
    onItemClicked: OnExploreItemClicked
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(item) }
            .padding(top = 12.dp, bottom = 12.dp)
    ) {
        ExploreImageContainer {
            Box {
                val painter = rememberImagePainter(
                    data = item.imageUrl,
                    builder = {
                        crossfade(true)
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (painter.state is ImagePainter.State.Loading) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(
                text = item.city.nameToDisplay,
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.caption.copy(color = crane_caption)
            )
        }
    }
}

@Composable
private fun ExploreImageContainer(content: @Composable () -> Unit) {
    Surface(Modifier.size(width = 60.dp, height = 60.dp), RoundedCornerShape(4.dp)) {
        content()
    }
}

@Preview
@Composable
fun FlyExploreSection(){
    ExploreSection(
        title = "Explore Flights by Destination",
        exploreList = listOf(
            ExploreModel(
                city = City("sz", "", "", ""),
                description = "ddd",
                imageUrl = ".sjdas")
        ),
        onItemClicked = {}
    )
}