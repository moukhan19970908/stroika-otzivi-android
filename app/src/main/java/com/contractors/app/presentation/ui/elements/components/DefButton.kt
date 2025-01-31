package com.contractors.app.presentation.ui.elements.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.theme.Brand
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White

@Composable
fun DefButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = VeryDarkGray,
    textColor: Color = White,
    textWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }


    val pressedGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF102D69),
            Color(0xFF009FE3),
        )
    )

    val normalGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF009FE3),
            Color(0xFF102D69)
        )
    )
    val interactionSource = remember { MutableInteractionSource() }


    Box(
        modifier = modifier
            .height(50.dp)
            .width(300.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(if (isPressed) normalGradient else pressedGradient)

            .clickable(
                onClick = {
                    onClick()
                },
                indication = null,
                interactionSource = interactionSource
            )
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onClick()
                        awaitRelease()
                        isPressed = false
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        DefText(
            text = text,
            color = textColor,
            weight = textWeight,
            size = 16
        )
    }
}

@Preview
@Composable
private fun DefBtnPreview() {
    DefButton(
        text = "Пример",
        color = Brand,
        textColor = White,
        textWeight = FontWeight.Normal,
        onClick = { }
    )

}