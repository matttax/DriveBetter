package com.matttax.drivebetter.map.presentation

import com.matttax.drivebetter.map.MapViewState
import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
import com.matttax.drivebetter.map.routes.RouteFinderFacade
import com.matttax.drivebetter.map.routes.RouteSearchRequest
import com.matttax.drivebetter.map.routes.model.Route
import com.matttax.drivebetter.map.search.SearchManagerFacade
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.lighthousegames.logging.KmLog
import kotlin.jvm.JvmInline

class MapViewModel : ViewModel() {

    private lateinit var searchManagerFacade: SearchManagerFacade
    private lateinit var routeFinderFacade: RouteFinderFacade

    lateinit var locationTracker: LocationTracker
        private set

    private val currentRidePoint = MutableStateFlow<RidePoint?>(null)
    val currentLocation = currentRidePoint
        .map { it?.location }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String>
        get() = _searchText

    private val _searchState = MutableStateFlow<SearchState>(SearchState.NoSearch)
    val searchState: StateFlow<SearchState>
        get() = _searchState

    private val _routeState = MutableStateFlow<RouteState>(RouteState.NoSearch)
    val routeState: StateFlow<RouteState>
        get() = _routeState

    private val _selectedDestination = MutableStateFlow<SearchItem?>(null)
    val selectedDestination: StateFlow<SearchItem?>
        get() = _selectedDestination

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
            } ?: currentLocation.value?.let {
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
        currentLocation.value?.let {
            routeFinderFacade.submitRequest(
                RouteSearchRequest(
                    from = it,
                    to = searchItem.point
                )
            )
        }
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
                currentRidePoint.update {
                    RidePoint(
                        location = extLoc.location.coordinates.toGeoPoint(),
                        speed = Speed(extLoc.speed.speedMps),
                        azimuth = Azimuth(extLoc.azimuth.azimuthDegrees)
                    )
                }
            }.launchIn(viewModelScope)
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

@JvmInline
value class Speed(
    val speed: Double
)

@JvmInline
value class Azimuth(
    val azimuth: Double
)

data class RidePoint(
    val location: GeoPoint,
    val speed: Speed = Speed(0.0),
    val azimuth: Azimuth = Azimuth(0.0),
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

sealed interface SearchState {

    data object NoSearch : SearchState

    data object Loading : SearchState

    data object Empty : SearchState

    data class Error(val message: String) : SearchState

    data class Results(val list: List<SearchItem>) : SearchState
}

sealed interface RouteState {

    data object NoSearch : RouteState

    data object Loading : RouteState

    data object Empty : RouteState

    data class Error(val message: String) : RouteState

    data class Results(val list: List<Route>) : RouteState
}

fun Float.ifZero(then: Float): Float {
    return if (equals(0)) then else this
}

fun MapViewState.getDelta(): Float {
    return 20f / zoom.ifZero(1f)
}
