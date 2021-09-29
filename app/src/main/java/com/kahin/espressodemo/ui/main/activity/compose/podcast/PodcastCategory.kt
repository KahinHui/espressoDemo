package com.kahin.espressodemo.ui.main.activity.compose.podcast

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.podcast.data.PodcastWithExtraInfo

val Keyline1 = 24.dp

@Composable
fun PodcastCategory(
    categoryId: Long,
    modifier: Modifier = Modifier
) {
    val viewModel: PodcastCategoryViewModel = viewModel()

    val viewState by viewModel.state.collectAsState()

    Column() {
        CategoryPodcasts(topPodcasts = viewState.topPodcasts, viewModel = viewModel)
    }
}

@Composable
private fun CategoryPodcasts(
    topPodcasts: List<PodcastWithExtraInfo>,
    viewModel: PodcastCategoryViewModel
) {
    LazyRow(
        contentPadding = PaddingValues(0.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        item {
            CategoryPodcastRow(
                podcasts = topPodcasts,
                onToggleFollowClicked = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CategoryPodcastRow(
    podcasts: List<PodcastWithExtraInfo>,
    onToggleFollowClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastIndexed = podcasts.size - 1
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = Keyline1, top = 8.dp, end = Keyline1, bottom = 24.dp)
    ) {
        itemsIndexed(items = podcasts) { index: Int, (podcast, _, isFollowed): PodcastWithExtraInfo ->
            PodcastRowItem(
                podcastTitle = podcast.title,
                isFollowed = isFollowed,
                onToggleFollowClicked = { onToggleFollowClicked(podcast.uri) },
                modifier = Modifier.width(128.dp)
            )

            if (index < lastIndexed) Spacer(modifier = Modifier.width(24.dp))
        }
    }
}

@Composable
private fun PodcastRowItem(
    podcastTitle: String,
    isFollowed: Boolean,
    modifier: Modifier = Modifier,
    onToggleFollowClicked: () -> Unit,
    podcastImageUrl: String? = null
) {
    Column(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            if (podcastImageUrl != null) {
                Image(
                    painter = rememberImagePainter(
                        data = podcastImageUrl,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }

        ToggleFollowPodcastIconButton(
            isFollowed = isFollowed,
            onClick = onToggleFollowClicked,
            modifier = Modifier.align(alignment = Alignment.End)
        )
    }

    Text(
        text = podcastTitle,
        style = MaterialTheme.typography.body2,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ToggleFollowPodcastIconButton(
    isFollowed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel = stringResource(if (isFollowed) R.string.cd_unfollow else R.string.cd_follow)
    IconButton(
        onClick = onClick,
        modifier = modifier.semantics {
            onClick(label = clickLabel, action = null)
        }
    ) {
        Icon(
            imageVector = when {
                isFollowed -> Icons.Default.Check
                else -> Icons.Default.Add
            },
            contentDescription = when {
                isFollowed -> stringResource(id = R.string.cd_following)
                else -> stringResource(id = R.string.cd_not_following)
            },
            tint = animateColorAsState(
                when {
                    isFollowed -> LocalContentColor.current
                    else -> Color.Black.copy(alpha = ContentAlpha.high)
                }
            ).value,
            modifier = Modifier
                .shadow(
                    elevation = animateDpAsState(if (isFollowed) 0.dp else 1.dp).value,
                    shape = MaterialTheme.shapes.small
                )
                .background(
                    color = animateColorAsState(
                        when {
                            isFollowed -> MaterialTheme.colors.surface.copy(0.38f)
                            else -> Color.White
                        }
                    ).value,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)
        )
    }
}