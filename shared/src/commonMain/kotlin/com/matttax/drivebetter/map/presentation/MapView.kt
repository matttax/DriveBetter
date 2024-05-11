package com.matttax.drivebetter.map.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.map.YandexMapView
import com.matttax.drivebetter.map.domain.SearchItem
import com.matttax.drivebetter.ui.common.text.SectionTitle
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.AnimationUtils
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.library.SharedRes

@Composable
fun MapView(
    mapViewModel: MapViewModel
) {
    val destination by mapViewModel.selectedDestination.collectAsState()
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
                DestinationView(
                    searchItem = it,
                    onClose = mapViewModel::onClearDestination
                )
            }
        }
        YandexMapView(
            mapViewModel.currentLocation,
            mapViewModel.selectedDestination,
            mapViewModel.searchState.map { state ->
                (state as? SearchState.Results)?.list ?: emptyList()
            },
            onCreate = mapViewModel::init,
            onUpdate = mapViewModel::onMapViewChanged,
            onDestinationSelected = mapViewModel::onDestinationSelected
        )
    }
}

@Composable
fun SearchBar(
    searchText: Flow<String>,
    onSearch: () -> Unit,
    onChange: (String) -> Unit = {},
) {
    val query by searchText.collectAsState("")
    TextField(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = query,
        onValueChange = onChange,
        placeholder = {
            SectionTitle(
                text = stringResource(SharedRes.strings.search),
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary
        )
    )
}

@Composable
fun DestinationView(
    searchItem: SearchItem,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            searchItem.name?.let { name ->
                searchItem.description?.let { description ->
                    Title(name)
                    SectionTitle(description)
                }
            }
        }
        Icon(
            modifier = Modifier.clickable(onClick = onClose),
            imageVector = Icons.Default.Close,
            contentDescription = null
        )
    }
}
