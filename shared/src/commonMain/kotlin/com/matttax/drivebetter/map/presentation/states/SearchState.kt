package com.matttax.drivebetter.map.presentation.states

import com.matttax.drivebetter.map.domain.model.SearchItem

sealed interface SearchState {

    data object NoSearch : SearchState

    data object Loading : SearchState

    data object Empty : SearchState

    data class Error(val message: String) : SearchState

    data class Results(val list: List<SearchItem>) : SearchState
}
