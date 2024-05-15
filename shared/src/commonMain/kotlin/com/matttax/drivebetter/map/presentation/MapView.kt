package com.matttax.drivebetter.map.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.map.YandexMapView
import com.matttax.drivebetter.map.presentation.components.DestinationView
import com.matttax.drivebetter.map.presentation.components.RidingView
import com.matttax.drivebetter.map.presentation.components.RouteView
import com.matttax.drivebetter.map.presentation.components.SearchBar
import com.matttax.drivebetter.map.presentation.states.RouteState
import com.matttax.drivebetter.map.presentation.states.SearchState
import com.matttax.drivebetter.ui.common.ErrorMessage
import com.matttax.drivebetter.ui.common.ProgressBar
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.utils.AnimationUtils
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.example.library.SharedRes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MapView(
    mapViewModel: MapViewModel,
    snackbarHostState: SnackbarHostState
) {
    val destination by mapViewModel.selectedDestination.collectAsState()
    val routeState by mapViewModel.routeState.collectAsState()
    val pagerState = rememberPagerState(0) {
        (routeState as? RouteState.Results)?.list?.size ?: 0
    }
    LaunchedEffect(true) {
        mapViewModel.searchState.collectLatest {
            when(it) {
                is SearchState.Empty -> snackbarHostState.showSnackbar("Nothing found. Try to expand the search area")
                is SearchState.Error -> snackbarHostState.showSnackbar("Error: ${it.message}")
                else -> {}
            }
        }
    }
    BindLocationTrackerEffect(mapViewModel.locationTracker)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            searchText = mapViewModel.searchText,
            onSearch = mapViewModel::onSearch,
            onChange = mapViewModel::onSearchTextChanged
        )
        AnimatedVisibility(
            visible = destination != null,
            enter = AnimationUtils.popUpEnter(AnimationUtils.PopUpDirection.DOWN),
            exit = AnimationUtils.popUpExit(AnimationUtils.PopUpDirection.DOWN),
        ) {
            destination?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            if (routeState !is RouteState.Riding) {
                                280.dp
                            } else 100.dp
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (routeState !is RouteState.Riding) {
                        DestinationView(
                            modifier = Modifier
                                .background(MaterialTheme.colors.surface)
                                .padding(20.dp),
                            searchItem = it,
                            onClose = mapViewModel::onClearDestination
                        )
                    }
                    when (val routes = routeState) {
                        is RouteState.Results -> {
                            HorizontalPager(pagerState) { index ->
                                mapViewModel.onRouteSelected(
                                    routes.list[index]
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    RouteView(
                                        route = routes.list[index],
                                        onStartRide = mapViewModel::startRide
                                    )
                                    BodyText("${index + 1} / ${routes.list.size}")
                                }
                            }
                        }
                        is RouteState.Riding -> {
                            RidingView(
                                timeSec = routes.seconds,
                                onFinish = mapViewModel::finishRide
                            )
                        }
                        is RouteState.Loading -> {
                            ProgressBar(
                                modifier = Modifier.fillMaxWidth(),
                                size = 30.dp
                            )
                        }
                        is RouteState.Error -> {
                            ErrorMessage(
                                modifier = Modifier.fillMaxWidth(),
                                message = routes.message,
                                onRetry = mapViewModel::refreshRoutes
                            )
                        }
                        is RouteState.Empty -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                BodyText(stringResource(SharedRes.strings.no_routes))
                            }
                        }
                        is RouteState.NoRoute -> Unit
                    }
                }
            }
        }
        YandexMapView(
            mapViewModel.currentRidePoint,
            mapViewModel.selectedDestination,
            mapViewModel.selectedPolyline,
            mapViewModel.isDriving,
            mapViewModel.searchState.map { state ->
                (state as? SearchState.Results)?.list ?: emptyList()
            },
            onCreate = mapViewModel::init,
            onUpdate = mapViewModel::onMapViewChanged,
            onDestinationSelected = mapViewModel::onDestinationSelected
        )
    }
}
