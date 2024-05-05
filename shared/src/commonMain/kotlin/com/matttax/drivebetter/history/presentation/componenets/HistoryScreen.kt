package com.matttax.drivebetter.history.presentation.componenets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.history.presentation.HistoryList
import com.matttax.drivebetter.history.presentation.RideHistoryState
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.history.presentation.toUiModel
import com.matttax.drivebetter.ui.common.LoadingScreen
import com.matttax.drivebetter.ui.common.text.Title
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun HistoryScreen(
    viewModel: RidesHistoryViewModel,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getRides()
    }
    when (val state = viewState) {
        is RideHistoryState.Loading -> LoadingScreen()
        is RideHistoryState.Error -> {}
        is RideHistoryState.RidesList -> {
            Column(
                modifier = modifier
            ) {
                Title(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(SharedRes.strings.ride_history),
                )
                HistoryList(
                    rides = state.rides.map { it.toUiModel() }.sortedByDescending { it.dateTimeStarted }
                ) {
                    viewModel.getRideData(it)
                }
            }
        }
        is RideHistoryState.RideDetails -> {
            RideScreen(
                modifier = modifier,
                viewModel = viewModel
            )
        }
        is RideHistoryState.Unauthorized -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Title("You're not authorized")
            }
        }
    }
}
