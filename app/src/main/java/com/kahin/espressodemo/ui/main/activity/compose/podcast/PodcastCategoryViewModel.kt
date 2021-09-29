package com.kahin.espressodemo.ui.main.activity.compose.podcast

import androidx.lifecycle.ViewModel
import com.kahin.espressodemo.ui.main.activity.compose.podcast.data.PodcastWithExtraInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PodcastCategoryViewModel(
) : ViewModel() {
    private val _state = MutableStateFlow(PodcastCategoryViewState())

    val state: StateFlow<PodcastCategoryViewState>
        get() = _state
}

data class PodcastCategoryViewState(
    val topPodcasts: List<PodcastWithExtraInfo> = emptyList(),
//    val episodes: List<EpisodeToPodcast> = emptyList()
)