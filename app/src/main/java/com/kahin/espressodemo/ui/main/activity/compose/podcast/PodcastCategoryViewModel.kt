package com.kahin.espressodemo.ui.main.activity.compose.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kahin.espressodemo.ui.main.activity.compose.podcast.data.Podcast
import com.kahin.espressodemo.ui.main.activity.compose.podcast.data.PodcastWithExtraInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

val topPodcasts = listOf(
    Podcast(
        uri = "https://www.npr.org/podcasts/510289/planet-money",
        title = "Planet Money",
        imageUrl = "https://media.npr.org/assets/img/2018/08/02/npr_planetmoney_podcasttile_sq-7b7fab0b52fd72826936c3dbe51cff94889797a0.jpg?s=1400"
    ),
    Podcast(
        uri = "https://99percentinvisible.org",
        title = "99% Invisible",
        imageUrl = "https://image.simplecastcdn.com/images/5b7d8c77-15ba-4eff-a999-2e725db21db5/c43e134f-15e6-41ae-9a2e-5e57cf761473/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg?aid=rss_feed"
    ),
    Podcast(
        uri = "https://www.thenakedscientists.com/podcasts/naked-scientists-podcast",
        title = "The Naked Scientists Podcast",
        imageUrl = "https://www.thenakedscientists.com/sites/default/files/media/media/images/NS_Podcasts_1400.png"
    )
)
val topPodcastsaaa = listOf(
    Podcast(
        uri = "https://www.npr.org/podcasts/510289/planet-money",
        title = "Planet Money",
        imageUrl = "https://media.npr.org/assets/img/2018/08/02/npr_planetmoney_podcasttile_sq-7b7fab0b52fd72826936c3dbe51cff94889797a0.jpg?s=1400"
    ),
    Podcast(
        uri = "https://99percentinvisible.org",
        title = "99% Invisible",
        imageUrl = "https://image.simplecastcdn.com/images/5b7d8c77-15ba-4eff-a999-2e725db21db5/c43e134f-15e6-41ae-9a2e-5e57cf761473/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg?aid=rss_feed"
    ),
    Podcast(
        uri = "https://www.thenakedscientists.com/podcasts/naked-scientists-podcast",
        title = "The Naked Scientists Podcast",
        imageUrl = "https://www.thenakedscientists.com/sites/default/files/media/media/images/NS_Podcasts_1400.png"
    ),
    Podcast(
        uri = "https://99percentinvisible.org",
        title = "99% Invisible",
        imageUrl = "https://image.simplecastcdn.com/images/5b7d8c77-15ba-4eff-a999-2e725db21db5/c43e134f-15e6-41ae-9a2e-5e57cf761473/3000x3000/stitcher-cover-99percentinvisible-3000x3000-r2021-final.jpg?aid=rss_feed"
    ),
    Podcast(
        uri = "https://www.thenakedscientists.com/podcasts/naked-scientists-podcast",
        title = "The Naked Scientists Podcast",
        imageUrl = "https://www.thenakedscientists.com/sites/default/files/media/media/images/NS_Podcasts_1400.png"
    )
)

class PodcastCategoryViewModel(
) : ViewModel() {
    private val _state = MutableStateFlow(PodcastCategoryViewState())

    val state: StateFlow<PodcastCategoryViewState>
        get() = _state

    init {
        val podcastWithExtraInfoList = mutableListOf<PodcastWithExtraInfo>()
        topPodcasts.forEachIndexed { index, item ->
            podcastWithExtraInfoList.add(
                index,
                PodcastWithExtraInfo().apply {
                podcast = item
                }
            )
        }
        _state.value = PodcastCategoryViewState(topPodcasts = podcastWithExtraInfoList)
    }

    fun onTogglePodcastFollowed(title: String) {
        viewModelScope.launch {
            _state.value.apply {
                this.topPodcasts.forEach {
                    if (it.podcast.title == title) {
                        it.isFollowed = !it.isFollowed
                    }
                }
            }
        }
    }
}

data class PodcastCategoryViewState(
    val topPodcasts: List<PodcastWithExtraInfo> = emptyList(),
//    val episodes: List<EpisodeToPodcast> = emptyList()
)