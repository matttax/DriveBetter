package com.matttax.drivebetter.map.presentation

import com.matttax.drivebetter.map.domain.model.Azimuth
import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.map.domain.model.Speed
import com.matttax.drivebetter.map.presentation.states.MapViewState
import com.matttax.drivebetter.map.presentation.states.RouteState
import com.matttax.drivebetter.map.presentation.states.SearchState
import com.matttax.drivebetter.map.routes.RouteFinderFacade
import com.matttax.drivebetter.map.routes.RouteSearchRequest
import com.matttax.drivebetter.map.search.SearchManagerFacade
import com.matttax.drivebetter.ui.utils.NumericUtils.ifZero
import com.matttax.drivebetter.coroutines.provideDispatcher
import com.matttax.drivebetter.map.domain.DriveManager
import com.matttax.drivebetter.map.routes.model.Route
import com.matttax.drivebetter.voice.Language
import com.matttax.drivebetter.voice.MessageDispatcher
import com.matttax.drivebetter.voice.MessageSpeller
import com.matttax.drivebetter.voice.model.SpeedViolation
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.lighthousegames.logging.KmLog

class MapViewModel(
    private val driveManager: DriveManager,
    private val messageDispatcher: MessageDispatcher
) : ViewModel() {

    private lateinit var searchManagerFacade: SearchManagerFacade
    private lateinit var routeFinderFacade: RouteFinderFacade

    lateinit var locationTracker: LocationTracker
        private set

    private val _currentRidePoint = MutableStateFlow<RidePoint?>(null)
    val currentRidePoint: StateFlow<RidePoint?> get() = _currentRidePoint

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText

    private val _searchState = MutableStateFlow<SearchState>(SearchState.NoSearch)
    val searchState: StateFlow<SearchState> get() = _searchState

    private val _routeState = MutableStateFlow<RouteState>(RouteState.NoRoute)
    val routeState: StateFlow<RouteState> get() = _routeState

    private val _selectedDestination = MutableStateFlow<SearchItem?>(null)
    val selectedDestination: StateFlow<SearchItem?> get() = _selectedDestination

    private val _selectedPolyline = MutableStateFlow<Any?>(null)
    val selectedPolyline: StateFlow<Any?> get() = _selectedPolyline

    private val _isDriving = MutableStateFlow(false)
    val isDriving: StateFlow<Boolean> get() = _isDriving

    private val mapState = MutableStateFlow<MapViewState?>(null)
    private var selectedRoute: Route? = null

    var isLocationManagerInitialized: Boolean = false
        private set

    fun init() {
        routeFinderFacade = RouteFinderFacade()
        searchManagerFacade = SearchManagerFacade()
        observeSearchResults()
        observeRoutes()
        observeRide()
    }

    fun setLocationTracker(locationTracker: LocationTracker) {
        this.locationTracker = locationTracker
        isLocationManagerInitialized = true
        launchLocationTracker()
        observeLocation()
    }

    fun onSearch() {
        val delta = mapState.value?.getDelta() ?: 0.0001f
        log.d { "onSearch delta = $delta" }
        val (topLeft, bottomRight) =
            mapState.value?.let {
                GeoPoint(it.targetPoint.latitude - delta, it.targetPoint.longitude - delta) to
                        GeoPoint(it.targetPoint.latitude + delta, it.targetPoint.longitude + delta)
            } ?: currentRidePoint.value?.location?.let {
                GeoPoint(it.latitude - delta, it.longitude - delta) to
                        GeoPoint(it.latitude + delta, it.longitude + delta)
            } ?: run {
                GeoPoint(.0, .0) to GeoPoint(.0, .0)
            }
        onSearch(
            query = searchText.value,
            topLeftPoint = topLeft,
            bottomRightPoint = bottomRight
        )

    }

    fun onMapViewChanged(mapViewState: MapViewState) {
        mapState.value = mapViewState
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }

    fun onDestinationSelected(searchItem: SearchItem) {
        _selectedDestination.value = searchItem
        _routeState.value = RouteState.Loading
        currentRidePoint.value?.let {
            routeFinderFacade.submitRequest(
                RouteSearchRequest(
                    from = it.location,
                    to = searchItem.point
                )
            )
        }
    }

    fun onRouteSelected(route: Route) {
        _selectedPolyline.value = route.polyline
        selectedRoute = route
    }

    fun refreshRoutes() {
        _routeState.value = RouteState.Loading
        routeFinderFacade.refresh()
    }

    fun clearPolyline() {
        _selectedPolyline.value = null
    }

    fun onClearDestination() {
        selectedRoute = null
        _selectedDestination.value = null
        _routeState.value = RouteState.NoRoute
        _searchState.value = SearchState.NoSearch
        routeFinderFacade.cancelRequest()
        clearPolyline()
        if (_isDriving.value) {
            _isDriving.value = false
        }
    }

    fun startRide() {
        messageDispatcher.onRideStarted()
        _isDriving.value = true
    }

    fun finishRide() {
        messageDispatcher.onRideEnded()
        _isDriving.value = false
    }

    private fun onSearch(query: String, topLeftPoint: GeoPoint, bottomRightPoint: GeoPoint) {
        searchManagerFacade.submit(query, topLeftPoint, bottomRightPoint)
        _searchState.value = SearchState.Loading
        log.d {
            "onSearch: query=$query, topLeft=$topLeftPoint, bottomRight=$bottomRightPoint"
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationTracker.stopTracking()
    }

    private fun launchLocationTracker() {
        viewModelScope.launch {
            try {
                locationTracker.stopTracking()
                locationTracker.startTracking()
            } catch (ex: Throwable) {
                log.e(ex) { "launchLocationTracker" }
            }
        }
    }

    private fun observeLocation() {
        locationTracker.getExtendedLocationsFlow()
            .onEach { extLoc ->
                _currentRidePoint.update {
                    RidePoint(
                        location = extLoc.location.coordinates.toGeoPoint(),
                        speed = Speed(extLoc.speed.speedMps),
                        azimuth = Azimuth(extLoc.azimuth.azimuthDegrees)
                    ).also {
                        if (_routeState.value is RouteState.Riding) {
                            driveManager.addRidePoint(it)
                        }
                        if (it.speed.value > 90) {
                            messageDispatcher.onSpeedExceeded(
                                SpeedViolation(
                                    actualSpeed = it.speed.value,
                                    recommendedSpeed = 90.0
                                )
                            )
                        }
                    }
                }
            }
            .flowOn(provideDispatcher().io)
            .launchIn(viewModelScope)
    }

    private fun observeSearchResults() {
        searchManagerFacade.searchResults
            .onEach {
                _searchState.value = when {
                    it.isFailure -> SearchState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                    it.isSuccess && it.getOrNull().isNullOrEmpty().not() -> SearchState.Results(it.getOrNull()!!)
                    else -> SearchState.Empty
                }
            }.launchIn(viewModelScope)
    }

    private fun observeRoutes() {
        routeFinderFacade.routes
            .onEach {
                if (_routeState.value is RouteState.Riding) return@onEach
                _routeState.value = when {
                    it.isFailure -> RouteState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                    it.isSuccess && it.getOrNull().isNullOrEmpty().not() -> RouteState.Results(
                        it.getOrNull()?.sortedBy { ride -> ride.safetyIndex }?.reversed()!!
                    )
                    else -> RouteState.Empty
                }
            }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeRide() {
        _isDriving.flatMapLatest { driving ->
            if (!driving) {
                onClearDestination()
                driveManager.sendBatch(isFinal = true)
                _routeState.value = RouteState.NoRoute
                flow<Int?> { emit(null) }
            } else if (routeState.value is RouteState.Riding && (routeState.value as? RouteState.Riding)?.seconds != 0) {
                flow { emit(null) }
            } else {
                timer()
            }
        }
            .filterNotNull()
            .onEach {
                _routeState.value = RouteState.Riding(it)
            }.launchIn(viewModelScope)
    }

    private fun timer(): Flow<Int> {
        return flow {
            var time = 0
            while (true) {
                delay(1000)
                time += 1
                emit(time)
                if (time % 30 == 0) {
                    driveManager.sendBatch(isFinal = false)
                }
            }
        }.flowOn(provideDispatcher().io)
    }

    companion object {
        private val log = KmLog("MapViewModel")
    }
}

fun LatLng.toGeoPoint() = GeoPoint(
    latitude, longitude
)

fun MapViewState.getDelta(): Float {
    return 20f / zoom.ifZero(1f)
}
