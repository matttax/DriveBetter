package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.profile.domain.model.Gender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.presentation.componenets.ChangeAvatarButton
import com.matttax.drivebetter.profile.presentation.componenets.ChoiceField
import com.matttax.drivebetter.profile.presentation.componenets.DataField
import com.matttax.drivebetter.profile.presentation.componenets.NumericDataField
import com.matttax.drivebetter.profile.presentation.componenets.StringDataField
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.StringUtils
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun ProfileDataScreen(
    profile: ProfileDomainModel,
    onEdit: (ProfileDomainModel) -> Unit,
    onLogOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = StringUtils.Titles.PROFILE,
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        KamelImage(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(CircleShape),
            resource = asyncPainterResource(profile.avatarByteArray.toString()),
            onFailure = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        ChangeAvatarButton {
            println(it)
        }
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        StringDataField(
            title = StringUtils.ProfileFields.NAME,
            data = profile.name ?: ""
        )
        NumericDataField(
            title = StringUtils.ProfileFields.AGE,
            data = 21,
        )
        ChoiceField(
            title = StringUtils.ProfileFields.GENDER,
            data = Gender.listAll(),
            selectedIndex = if (profile.gender == Gender.MALE) 0 else 1
        )
        StringDataField(
            title = StringUtils.ProfileFields.CITY,
            data = profile.city ?: ""
        )
        StringDataField(
            title = StringUtils.ProfileFields.LICENSE_NUMBER_SHORT,
            data = profile.driversLicenseId ?: ""
        )
        DataField(
            title = StringUtils.ProfileFields.RATING,
            data = if (profile.rating == null) "" else ((profile.rating * 100).toInt() / 100.0).toString()
        )
        StringDataField(
            title = "Voice notifications",
            data = "Enabled"
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onLogOut) {
            Text(
                text = StringUtils.Actions.LOG_OUT,
                color = Color.Red
            )
        }
    }
}

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
