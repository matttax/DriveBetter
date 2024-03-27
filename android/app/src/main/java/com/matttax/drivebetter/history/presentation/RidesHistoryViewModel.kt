package com.matttax.drivebetter.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matttax.drivebetter.history.data.DriveRepository
import com.matttax.drivebetter.history.data.model.RideUiModel
import com.matttax.drivebetter.history.data.model.toUiModel
import com.matttax.drivebetter.profile.data.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RidesHistoryViewModel @Inject constructor(
    private val driveRepository: DriveRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _driversRides = MutableStateFlow<List<RideUiModel>>(emptyList())
    val driversRides = _driversRides.asStateFlow()

    private val _passengerRides = MutableStateFlow<List<RideUiModel>>(emptyList())
    val passengerRides = _passengerRides.asStateFlow()

    init {
        driveRepository
            .getRideHistoryById(accountRepository.getId())
            .flowOn(Dispatchers.IO)
            .onEach { list ->
                _driversRides.value = list
                    .filter { it.detectedRole == "водитель" }
                    .map { it.toUiModel() }
                _passengerRides.value = list
                    .filter { it.detectedRole == "пассажир" }
                    .map { it.toUiModel() }
            }.launchIn(viewModelScope)
    }
}
