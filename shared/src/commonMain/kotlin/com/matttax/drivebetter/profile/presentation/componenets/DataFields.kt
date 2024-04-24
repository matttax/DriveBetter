package com.matttax.drivebetter.profile.presentation.componenets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.TextFieldStyle
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun NumericDataField(
    title: String,
    data: Int,
    onEdit: (Int) -> Unit = {}
) {
    val dialogState = rememberMaterialDialogState()
    DataField(title, data.toString(), dialogState)
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        title(text = title)
        input(
            label = "",
            prefill = data.toString(),
            onInput = {
                onEdit(it.replace(" ", "").toIntOrNull() ?: data)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textFieldStyle = TextFieldStyle.Outlined,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            )
        )
    }
}

@Composable
fun StringDataField(
    title: String,
    data: String,
    onEdit: (String) -> Unit = {}
) {
    val dialogState = rememberMaterialDialogState()
    DataField(title, data, dialogState)
    buildDialog(dialogState) {
        title(text = title)
        input(
            label = "",
            prefill = data,
            onInput = onEdit,
            singleLine = true,
            textFieldStyle = TextFieldStyle.Outlined,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            )
        )
    }
}

@Composable
fun ChoiceField(
    title: String,
    data: List<String>,
    selectedIndex: Int,
    onNewSelect: (Int) -> Unit = {}
) {
    val dialogState = rememberMaterialDialogState()
    DataField(title, data[selectedIndex], dialogState)
    buildDialog(dialogState) {
        title(text = title)
        listItemsSingleChoice(
            list = data,
            initialSelection = selectedIndex,
            onChoiceChange = onNewSelect
        )
    }
}

@Composable
fun DataField(
    title: String,
    data: String,
    dialogState: MaterialDialogState? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 7.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(0.4f),
            text = title,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            modifier = Modifier
                .weight(0.6f)
                .clickable {
                    dialogState?.show()
                },
            text = data,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
private fun buildDialog(
    state: MaterialDialogState,
    content: @Composable MaterialDialogScope.() -> Unit
) = MaterialDialog(
    dialogState = state,
    buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    },
    content = content
)
