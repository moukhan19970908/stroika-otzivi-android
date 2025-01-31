package com.contractors.app.presentation.ui.elements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.White

@Composable
fun DefBasicTextField(
    text: String,
    label: String = "",
    labelSize: Int = 14,
    textSize: Int = 14,
    startPadding: Int = 15,
    prefixPadding: Int = 10,
    clip: Int = 20,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    onFocus: () -> Unit = {},
    prefix: @Composable () -> Unit = {},
    suffix: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    backgroundColor: Color = Color.White,
    border: Color = White,
    isPhone: Boolean = false,
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    var onFocus by remember { mutableStateOf(false) }
    BasicTextField(
        value = text,
        onValueChange = onValueChange,

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        textStyle = TextStyle.Default.copy(fontSize = textSize.sp),
        modifier = Modifier.onFocusChanged {
            if (it.isFocused) onFocus()
            onFocus = it.isFocused
        },
        visualTransformation = if (isPhone) PhoneVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Box{
                Row(
                    modifier = modifier
                        .background(backgroundColor)
                        .padding(horizontal = startPadding.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefix()
                    Spacer(Modifier.width(prefixPadding.dp))
                    if (label.isNotEmpty() && text.isEmpty() && !onFocus) DefText(label, color = Gray, size = labelSize)
                    innerTextField()
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 20.dp)
                ) {
                    suffix()
                }
            }
        },
        interactionSource = interactionSource
    )
}