package com.matttax.drivebetter.profile.presentation.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matttax.drivebetter.profile.presentation.constraints.ProfileDataType
import com.matttax.drivebetter.ui.common.text.ButtonText
import com.matttax.drivebetter.ui.common.text.LargeLabel
import com.matttax.drivebetter.ui.utils.ColorUtils

@Composable
fun RegistrationField(
    title: String = "",
    initialValue: String = "",
    inputType: ProfileDataType = ProfileDataType.Text(""),
    isLast: Boolean = false,
    onMatches: (String, Boolean) -> Unit = { _, _ -> }
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(initialValue) }
    val matches by remember {
        derivedStateOf {
            inputType.matches(text).also {
                onMatches(text, it)
            }
        }
    }
    if (inputType is ProfileDataType.Choice) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 2.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LargeLabel("$title:")
            SingleChoice(
                list = inputType.list,
                onSelected = { onMatches(it, inputType.matches(it)) }
            )
        }
    } else {
        OutlinedTextField(
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp,
                    horizontal = 5.dp
                ),
            value = text,
            onValueChange = { text = it },
            label = { LargeLabel(title) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = if (!isLast) ImeAction.Next else ImeAction.Done,
                keyboardType = when (inputType) {
                    is ProfileDataType.Numeric -> KeyboardType.Number
                    is ProfileDataType.Email -> KeyboardType.Email
                    is ProfileDataType.Password -> KeyboardType.Password
                    else -> KeyboardType.Text
                },
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            shape = RoundedCornerShape(20),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.surface
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = MaterialTheme.colors.primary
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (matches)
                        Icons.Default.Done
                    else
                        Icons.Default.Error,
                    contentDescription = null,
                    tint = ColorUtils.getInputFieldColor(matches)
                )
            }
        )
    }
}

@Composable
fun SingleChoice(
    list: List<String>,
    onSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf("") }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list) { item ->
            OutlinedButton(
                onClick = {
                    selected = if (selected == item) "" else item
                    onSelected(selected)
                },
                shape = CircleShape,
                border = BorderStroke(
                    width = if (item == selected) 3.dp else 1.dp,
                    color = MaterialTheme.colors.primary
                )
            ) {
                ButtonText(
                    text = item,
                    outlined = true
                )
            }
        }
    }
}
