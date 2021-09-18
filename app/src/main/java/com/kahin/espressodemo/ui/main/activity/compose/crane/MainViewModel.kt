package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kahin.espressodemo.ui.main.activity.compose.calendar.data.DatesRepository
import com.kahin.espressodemo.ui.main.activity.compose.calendar.model.DatesSelectedState
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.DestinationsRepository
import com.kahin.espressodemo.ui.main.activity.compose.crane.data.ExploreModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

const val MAX_PEOPLE = 4

@HiltViewModel
class MainViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository,
    datesRepository: DatesRepository
) : ViewModel() {

    val restaurants: List<ExploreModel> = destinationsRepository.restaurants
    val datesSelected: DatesSelectedState = datesRepository.datesSelected

    private val _suggestedDestinations = MutableLiveData<List<ExploreModel>>()
//    val suggestedDestinations = LiveData<List<ExploreModel>>
//        get() = _suggestedDestinations

    init {
        _suggestedDestinations.value = destinationsRepository.destinations
    }

    fun updatePeople(people: Int) {
        viewModelScope.launch {
            if (people > MAX_PEOPLE) {
                _suggestedDestinations.value = emptyList()
            } else {
                val newDestinations = withContext(Dispatchers.Default) {
                    destinationsRepository.destinations
                        .shuffled(Random(people * (1..100).shuffled().first()))
                }
                _suggestedDestinations.value = newDestinations
            }
        }
    }

    fun toDestinationChanged(newDestination: String) {
        viewModelScope.launch {
            val newDestinations = withContext(Dispatchers.Default) {
                destinationsRepository.destinations
                    .filter { it.city.nameToDisplay.contains(newDestination) }
            }
            _suggestedDestinations.value = newDestinations
        }
    }
}