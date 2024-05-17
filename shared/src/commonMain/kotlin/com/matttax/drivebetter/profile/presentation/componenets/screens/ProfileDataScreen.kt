package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.profile.domain.model.Gender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.presentation.avatar.SharedImage
import com.matttax.drivebetter.profile.presentation.componenets.ChangeAvatarButton
import com.matttax.drivebetter.profile.presentation.componenets.ChoiceField
import com.matttax.drivebetter.profile.presentation.componenets.DataField
import com.matttax.drivebetter.profile.presentation.componenets.NumericDataField
import com.matttax.drivebetter.profile.presentation.componenets.StringDataField
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.StringUtils

@Composable
fun ProfileDataScreen(
    profile: ProfileDomainModel,
    onEdit: (ProfileDomainModel) -> Unit,
    onChangeAvatar: (SharedImage) -> Unit,
    onLogOut: () -> Unit
) {
    var avatarImageBitmap by mutableStateOf<ImageBitmap?>(null)
    fun ProfileDomainModel.update() { onEdit(this) }
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
        if (avatarImageBitmap == null) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
        } else {
            avatarImageBitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(CircleShape),
                    painter = BitmapPainter(it),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        ChangeAvatarButton {
            avatarImageBitmap = it.toImageBitmap()
            onChangeAvatar(it)
        }
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        StringDataField(
            title = StringUtils.ProfileFields.NAME,
            data = profile.name ?: ""
        ) {
            profile.copy(name = it).update()
        }
        NumericDataField(
            title = StringUtils.ProfileFields.AGE,
            data = profile.age,
        )
        ChoiceField(
            title = StringUtils.ProfileFields.GENDER,
            data = Gender.listAll(),
            selectedIndex = if (profile.gender == Gender.MALE) 0 else 1
        ) {
            profile.copy(gender = if (it == 0) Gender.MALE else Gender.FEMALE).update()
        }
        StringDataField(
            title = StringUtils.ProfileFields.CITY,
            data = profile.city ?: ""
        ) {
            profile.copy(city = it).update()
        }
        StringDataField(
            title = StringUtils.ProfileFields.LICENSE_NUMBER_SHORT,
            data = profile.driversLicenseId ?: ""
        ) {
            profile.copy(driversLicenseId = it).update()
        }
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
