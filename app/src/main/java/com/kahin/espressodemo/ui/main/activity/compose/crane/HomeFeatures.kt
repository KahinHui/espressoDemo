package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kahin.espressodemo.R

@Composable
fun EatSearchContent(datesSelected: String, eatUpdates: EatSearchContentUpdates) {
    CraneSearch {
        PeopleUserInput(onPeopleChanged = eatUpdates.onPeopleChanged)
        Spacer(modifier = Modifier.height(8.dp))
        DatesUserInput(datesSelected, onDateSelectionClicked = eatUpdates.onDateSelectionClicked)
        Spacer(modifier = Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Time", vectorImageId = R.drawable.ic_time)
        Spacer(modifier = Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Location", vectorImageId = R.drawable.ic_restaurant)
    }
}

@Composable
fun SleepSearchContent(datesSelected: String, sleepUpdates: SleepSearchContentUpdates) {
    CraneSearch {
        PeopleUserInput(onPeopleChanged = sleepUpdates.onPeopleChanged)
        Spacer(modifier = Modifier.height(8.dp))
        DatesUserInput(datesSelected, sleepUpdates.onDateSelectionClicked)
        Spacer(modifier = Modifier.height(8.dp))
        SimpleUserInput(caption = "Select Location", vectorImageId = R.drawable.ic_hotel)
    }
}

@Composable
private fun CraneSearch(content: @Composable () -> Unit) {
    Column(Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)) {
        content()
    }
}