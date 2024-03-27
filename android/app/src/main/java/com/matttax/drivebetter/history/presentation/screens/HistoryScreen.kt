package com.matttax.drivebetter.history.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.history.presentation.components.list.RideItem
import com.matttax.drivebetter.ui.common.EmptyList
import com.matttax.drivebetter.ui.common.Title

@Composable
fun HistoryScreen(
    viewModel: RidesHistoryViewModel,
    onItemClick: (Int) -> Unit
) {
    val driversRides by viewModel.driversRides.collectAsState()
    val passengersRides by viewModel.passengerRides.collectAsState()
    Column {
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "History",
        )
        Text(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 2.dp
            ),
            text = "Driver"
        )
        if (driversRides.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(7.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(driversRides.size) {
                    RideItem(
                        ride = driversRides[it],
                        onClick = onItemClick
                    )
                }
            }
        } else {
            EmptyList()
        }
        Spacer(Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 2.dp
            ),
            text = "Passenger"
        )
        if (passengersRides.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(7.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(passengersRides.size) {
                    RideItem(
                        ride = passengersRides[it],
                    ) { }
                }
            }
        } else {
            EmptyList()
        }
    }
}
