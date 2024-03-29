package com.hbaez.analyzer_presentation.analyzer_presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.analyzer_presentation.analyzer_presentation.AnalyzerEvent
import com.hbaez.analyzer_presentation.analyzer_presentation.AnalyzerOverviewModel
import com.hbaez.core_ui.LocalSpacing

@OptIn(ExperimentalTextApi::class)
@Composable
fun ActivityChart(
    weeklyContributions: List<Int>,
    monthValue: Int,
    currentActivityIndex: Int,
    viewModel: AnalyzerOverviewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val borderColor = MaterialTheme.colorScheme.outline
    val startColor = MaterialTheme.colorScheme.onPrimaryContainer
    val endColor = Color.Green
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellCount = 13
    val cellPadding = spacing.spaceExtraSmall
    val maxValue = (weeklyContributions.maxOrNull() ?: 1).coerceAtLeast(1)
    val textMeasurer = rememberTextMeasurer()
    val style = TextStyle(
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = MaterialTheme.colorScheme.onBackground
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 5)
    ) {
        Canvas(
            modifier = Modifier
                .padding(horizontal = spacing.spaceSmall)
                .fillMaxWidth()
                .height(((screenWidth - spacing.spaceSmall * 2 - (3 * cellPadding.value).dp) / cellCount) * 5)
                .pointerInput(true) {
                    detectTapGestures { offset ->
                        val cellSize =
                            (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount
                        val columnIndex = (offset.x / (cellSize + cellPadding.toPx())).toInt()
                        var rowIndex = (offset.y / (cellSize + cellPadding.toPx())).toInt()
                        if (rowIndex != 0) {
                            rowIndex--
                            val cellIndex = rowIndex + columnIndex * 4
                            viewModel.onEvent(AnalyzerEvent.OnContributionChartClick(cellIndex))
                        }
                    }
                }
        ) {
            // Label quarterly months
            val q1 = 12 - monthValue
            val q2 = (15 - monthValue) % 12
            val q3 = (18 - monthValue) % 12
            val q4 = (21 - monthValue) % 12
            // Calculate the cellSize based on screen width and the number of columns
            val cellSize = (screenWidth.toPx() - spacing.spaceSmall.toPx() * 2 - (cellCount - 1) * cellPadding.toPx()) / cellCount

            // iterate through weeklyContributions and draw each cell
            var index = 0
            for (i in 0 until 13) {
                when(i) {
                    q1 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Jan",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q2 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Mar",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q3 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Jun",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                    q4 -> {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Sept",
                            style = style,
                            topLeft = Offset(
                                x = i * (cellSize + cellPadding.toPx()),
                                y = 0f,
                            )
                        )
                    }
                }
                for (j in 1 until 5) {
                    val contributionCount = weeklyContributions.getOrNull(i * 4 + (j - 1)) ?: 0
                    val color = getCellColor(contributionCount, maxValue, startColor, endColor)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(i * (cellSize + cellPadding.toPx()), j * (cellSize + cellPadding.toPx())),
                        size = Size(cellSize - cellPadding.toPx(), cellSize - cellPadding.toPx()),
                        cornerRadius = CornerRadius(spacing.spaceExtraSmall.toPx())
                    )
                    if(index == currentActivityIndex){
                        drawRoundRect(
                            color = borderColor, // Specify the border color
                            topLeft = Offset(i * (cellSize + cellPadding.toPx()), j * (cellSize + cellPadding.toPx())),
                            size = Size(cellSize - cellPadding.toPx(), cellSize - cellPadding.toPx()),
                            cornerRadius = CornerRadius(spacing.spaceExtraSmall.toPx()), // Use the same corner radius as the filled rectangle
                            style = Stroke(width = 2.dp.toPx()) // Specify the border width
                        )
                    }
                    index++
                }
            }
        }
    }
}

private fun getCellColor(
    contributionCount: Int,
    maxValue: Int,
    startColor: Color,
    endColor: Color
): Color {
    val fraction = contributionCount.toFloat() / maxValue.toFloat()
    return lerp(startColor, endColor, fraction)
}