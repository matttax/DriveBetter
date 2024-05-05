package com.matttax.drivebetter.map.presentation

import com.matttax.drivebetter.map.domain.GeoPoint
import com.matttax.drivebetter.map.domain.SearchItem
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
import kotlin.jvm.JvmInline

class MapViewModel : ViewModel() {

    private lateinit var searchManagerFacade: SearchManagerFacade

    lateinit var locationTracker: LocationTracker
        private set

    private val currentRidePoint = MutableStateFlow<RidePoint?>(null)
    val currentLocation = currentRidePoint
        .map { it?.location }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _searchState = MutableStateFlow<SearchState>(SearchState.NoSearch)
    val searchState: StateFlow<SearchState>
        get() = _searchState

    fun initSearch() {
        searchManagerFacade = SearchManagerFacade()
        observeSearchResults()
    }

    fun setLocationTracker(locationTracker: LocationTracker) {
        this.locationTracker = locationTracker
        launchLocationTracker()
        observeLocation()
    }

    fun onSearch(query: String, topLeftPoint: GeoPoint, bottomRightPoint: GeoPoint) {
        searchManagerFacade.submit(
            query, topLeftPoint, bottomRightPoint
        )
        _searchState.value = SearchState.Loading
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
                println(ex)
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