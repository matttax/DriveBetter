package com.matttax.drivebetter.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matttax.drivebetter.ui.common.text.SectionTitle
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import org.example.library.SharedRes

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
        textStyle = TextStyle(
            fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_display_regular),
            fontSize = 19.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary
        )
    )
}
