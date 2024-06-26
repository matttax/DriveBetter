package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun ImageSourceOptionDialog(
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {},
    dialogState: MaterialDialogState
) {
    MaterialDialog(
        dialogState = dialogState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select an Image Source",
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .clickable {
                        onCameraRequest.invoke()
                        dialogState.hide()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Camera,
                    contentDescription = null
                )
                Text(
                    text = "Camera",
                    color = MaterialTheme.colors.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .clickable {
                        onGalleryRequest.invoke()
                        dialogState.hide()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.BrowseGallery,
                    contentDescription = null
                )
                Text(text = "Gallery", color = MaterialTheme.colors.onSurface)
            }
        }
    }
}
