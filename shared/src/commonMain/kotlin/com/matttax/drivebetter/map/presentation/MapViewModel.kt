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
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.lighthousegames.logging.KmLog

class MapViewModel : ViewModel() {

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

    private val _routeState = MutableStateFlow<RouteState>(RouteState.NoSearch)
    val routeState: StateFlow<RouteState> get() = _routeState

    private val _selectedDestination = MutableStateFlow<SearchItem?>(null)
    val selectedDestination: StateFlow<SearchItem?> get() = _selectedDestination

    private val _selectedPolyline = MutableStateFlow<Any?>(null)
    val selectedPolyline: StateFlow<Any?> get() = _selectedPolyline

    private val mapState = MutableStateFlow<MapViewState?>(null)

    fun init() {
        routeFinderFacade = RouteFinderFacade()
        searchManagerFacade = SearchManagerFacade()
        observeSearchResults()
        observeRoutes()
    }

    fun setLocationTracker(locationTracker: LocationTracker) {
        this.locationTracker = locationTracker
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

    fun onPolylineSelected(polyline: Any) {
        _selectedPolyline.value = polyline
    }

    fun refreshRoutes() {
        _routeState.value = RouteState.Loading
        routeFinderFacade.refresh()
    }

    fun clearPolyline() {
        _selectedPolyline.value = null
    }

    fun onClearDestination() {
        _selectedDestination.value = null
        routeFinderFacade.cancelRequest()
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
                    )
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
                _routeState.value = when {
                    it.isFailure -> RouteState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
                    it.isSuccess && it.getOrNull().isNullOrEmpty().not() -> RouteState.Results(it.getOrNull()!!)
                    else -> RouteState.Empty
                }
            }.launchIn(viewModelScope)
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

