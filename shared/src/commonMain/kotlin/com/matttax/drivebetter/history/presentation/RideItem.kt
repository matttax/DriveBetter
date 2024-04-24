package com.matttax.drivebetter.history.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.StarRatingBar
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.common.text.SectionTitle
import com.matttax.drivebetter.ui.utils.DateUtils.asSingleString
import com.matttax.drivebetter.ui.utils.NumericUtils.toRoundedString

@Composable
fun RideItem(
    ride: RideUiModel,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 2.dp
            )
            .clickable { onClick(ride.id) },
        shape = MaterialTheme.shapes.small,
        elevation = 5.dp,
        contentColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 5.dp,
                    top = 3.dp,
                    bottom = 3.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.7f),
                verticalArrangement = Arrangement.Center
            ) {
                SectionTitle(
                    text = ride.dateTimeStarted.asSingleString(),
                )
                BodyText(
                    text = ride.locationStarted,
                    isImportant = false
                )
            }
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    StarRatingBar(
                        rating = ride.rating / 10,
                        starSize = 30.dp,
                        starCount = 1
                    )
                    BodyText(
                        text = ride.rating.toRoundedString(),
                        isImportant = true
                    )
                }
            }
        }
    }
}
