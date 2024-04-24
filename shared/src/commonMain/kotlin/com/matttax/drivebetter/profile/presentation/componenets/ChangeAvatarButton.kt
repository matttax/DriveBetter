package com.matttax.drivebetter.profile.presentation.componenets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.matttax.drivebetter.permissions.PermissionCallback
import com.matttax.drivebetter.permissions.PermissionStatus
import com.matttax.drivebetter.permissions.PermissionType
import com.matttax.drivebetter.permissions.createPermissionsManager
import com.matttax.drivebetter.profile.presentation.avatar.rememberCameraManager
import com.matttax.drivebetter.profile.presentation.avatar.rememberGalleryManager
import com.matttax.drivebetter.profile.presentation.componenets.screens.ImageSourceOptionDialog
import com.matttax.drivebetter.ui.utils.StringUtils
import com.matttax.drivebetter.util.provideDispatcher
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ChangeAvatarButton(
    onImageSelected: (bitmap: ImageBitmap) -> Unit
) {
    val chooseDialogState = rememberMaterialDialogState()
    val permissionDialogState = rememberMaterialDialogState()
    val coroutineScope = rememberCoroutineScope()
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(
        object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus
            ) {
                when (status) {
                    PermissionStatus.GRANTED -> {
                        when (permissionType) {
                            PermissionType.CAMERA -> launchCamera = true
                            PermissionType.GALLERY -> launchGallery = true
                        }
                    }
                    else -> {
                        permissionDialogState.show()
                    }
                }
            }
        }
    )
    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            withContext(provideDispatcher().io) {
                it?.toImageBitmap()
            }?.also {
                onImageSelected(it)
            }
        }
    }
    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            withContext(provideDispatcher().io) {
                it?.toImageBitmap()
            }?.also {
                onImageSelected(it)
            }
        }
    }
    ImageSourceOptionDialog(
        onGalleryRequest = { launchGallery = true },
        onCameraRequest = { launchCamera = true },
        dialogState = chooseDialogState
    )
    MaterialDialog(
        permissionDialogState,
        buttons = {
            positiveButton(
                text = "Settings",
                onClick = {
                    launchSetting = true
                }
            )
            negativeButton(
                text = "Cancel"
            )
        }
    ) {
        title("Permission Required")
        message("To set your profile picture, please grant this permission. You can manage permissions in your device settings.")
    }
    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }
    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }
    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    Button(
        onClick = {
            chooseDialogState.show()
        },
        shape = CircleShape
    ) {
        Text(
            text = StringUtils.Actions.CHANGE_AVATAR,
            color = MaterialTheme.colors.surface
        )
    }
}
