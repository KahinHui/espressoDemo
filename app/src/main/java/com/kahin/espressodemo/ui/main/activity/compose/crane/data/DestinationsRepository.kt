package com.kahin.espressodemo.ui.main.activity.compose.crane.data

import javax.inject.Inject

class DestinationsRepository @Inject constructor(
    private val destinationsLocalDataSource: DestinationsLocalDataSource
) {
    val destinations: List<ExploreModel> = destinationsLocalDataSource.craneRestaurants
    val restaurants: List<ExploreModel> = destinationsLocalDataSource.craneRestaurants

    fun getDestination(cityName: String): ExploreModel? {
        return destinationsLocalDataSource.craneRestaurants.firstOrNull {
            it.city.name == cityName
        }
    }
}