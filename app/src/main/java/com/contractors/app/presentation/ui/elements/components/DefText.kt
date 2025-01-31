package com.contractors.app.presentation.ui.elements.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.nonScaledSp

@Composable
fun DefText(
    text: String,
    size: Int = 14,
    color: Color = DarkGray,
    weight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    align: TextAlign = TextAlign.Start,
    spacing: TextUnit = 20.sp,
    textDecoration: TextDecoration = TextDecoration.None,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    singleLine: Boolean = false,
    maxLength: Int = 10000
) {
    Text(
        text = if (text.length > maxLength) text.substring(0, maxLength) + "..." else text,
        fontSize = size.nonScaledSp,
        color = color,
        fontStyle = fontStyle,
        fontWeight = weight,
        modifier = modifier,
        lineHeight = spacing,
        textAlign = align,
        style = textStyle,
        maxLines = if (singleLine) 1 else 50,
        textDecoration = textDecoration,
        overflow = TextOverflow.Ellipsis
    )
}