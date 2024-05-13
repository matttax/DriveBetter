package com.matttax.drivebetter.map.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.matttax.drivebetter.map.domain.model.SearchItem
import com.matttax.drivebetter.ui.common.text.SectionTitle
import com.matttax.drivebetter.ui.common.text.Title

@Composable
fun DestinationView(
    searchItem: SearchItem,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
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
