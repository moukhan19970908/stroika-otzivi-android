package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.theme.DarkGreen
import com.contractors.app.presentation.ui.theme.Green
import com.contractors.app.presentation.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularProgressBar(
    totalSegments: Int = 8,
    segmentColor: Color = Green,
    secondSegmentColor: Color = DarkGreen,
    activeSegmentColor: Color = White,
    strokeWidth: Float = 8f,
    radius: Float = 100f,
    intervalMillis: Long = 100
) {
    var activeSegments by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            activeSegments++
            if (activeSegments >= totalSegments) activeSegments = 0

            delay(intervalMillis)
        }
    }

    Canvas(modifier = Modifier.size((radius).dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val segmentAngle = 360f / totalSegments

        for (i in 0 until totalSegments) {
            val angleInRadians = Math.toRadians((i * segmentAngle).toDouble() - 90)

            val startX = centerX + (radius - strokeWidth) * cos(angleInRadians).toFloat()
            val startY = centerY + (radius - strokeWidth) * sin(angleInRadians).toFloat()
            val endX = centerX + radius * cos(angleInRadians).toFloat()
            val endY = centerY + radius * sin(angleInRadians).toFloat()

            val paintColor =
                if (i == activeSegments) activeSegmentColor
                else if (i % 2 == 0) segmentColor else secondSegmentColor

            drawLine(
                color = paintColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}