package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.matttax.drivebetter.ui.theme.Gray
import com.matttax.drivebetter.ui.theme.Green
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.example.library.SharedRes

@Composable
fun LineChartSample() {
    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Visibility",
            data = listOf(70.0, 98.0, 50.33, 40.0, 99.0, 92.1),
            lineColor = Green,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        ),
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LineChart(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Gray,
            xAxisData = listOf("20:05", "20:15", "20:25", "20:35", "20:45", "20:55"),
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 12.sp,
                color = Gray,
                fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_display_light),
                fontWeight = FontWeight.W400
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Gray,
                fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_display_light),
                fontWeight = FontWeight.W400
            ),
            descriptionStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_text_medium),
                color = MaterialTheme.colors.onPrimary
            ),
            yAxisRange = 14,
            oneLineChart = false,
            gridOrientation = GridOrientation.VERTICAL
        )
    }
}
