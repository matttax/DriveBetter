package com.matttax.drivebetter.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.EmptyList

@Composable
fun HistoryList(
    rides: List<RideUiModel>,
    onClick: (Int) -> Unit
) {
    if (rides.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(7.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(rides.size) { idx ->
                RideItem(
                    ride = rides[idx],
                    onClick = {
                        onClick(idx)
                    }
                )
            }
        }
    } else {
        EmptyList()
    }
}
