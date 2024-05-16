package com.matttax.drivebetter.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matttax.drivebetter.ui.utils.ColorUtils
import dev.icerock.moko.resources.compose.fontFamilyResource
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import org.example.library.SharedRes

@Composable
fun BottomNavigationBar(navigator: Navigator) {
    val current by navigator.currentEntry.collectAsState(initial = null)
    Card(
        modifier = Modifier.padding(horizontal = 7.dp),
        elevation = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BottomNavigationItem.entries.forEach { item ->
                val selected = current?.route?.route == item.route
                IconButton(
                    modifier = Modifier
                        .weight(0.25f)
                        .padding(vertical = 10.dp),
                    onClick = {
                        navigator.navigate(
                            item.route,
                            NavOptions(
                                popUpTo = PopUpTo.First()
                            ),
                        )
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = item.icon,
                            tint = ColorUtils.getBottomColor(selected),
                            contentDescription = null
                        )
                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                        Text(
                            text = item.title,
                            color = ColorUtils.getBottomColor(selected),
                            fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_text_medium),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
