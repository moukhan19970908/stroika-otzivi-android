package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray

@Composable
fun PassBasicTextField(
    text: String,
    label: String = "",
    labelSize: Int = 16,
    textSize: Int = 16,
    startPadding: Int = 15,
    prefixPadding: Int = 10,
    clip: Int = 10,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    onFocus: () -> Unit = {},
    prefix: @Composable () -> Unit = {},
    suffix: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    backgroundColor: Color = Color.White,
    border: Color = Gray,
) {
    var onFocus by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        visualTransformation = if (isShow) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle.Default.copy(fontSize = textSize.sp),
        modifier = Modifier.onFocusChanged {
            if (it.isFocused) onFocus()
            onFocus = it.isFocused
        },
        decorationBox = { innerTextField ->
            Box{
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(clip.dp))
                        .background(backgroundColor)
                        .border(1.dp, border, RoundedCornerShape(clip.dp))
                        .padding(horizontal = startPadding.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefix()
                    Spacer(Modifier.width(prefixPadding.dp))
                    if (label.isNotEmpty() && text.isEmpty() && !onFocus) DefText(label, color = Gray, size = labelSize)
                    innerTextField()
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd).padding(horizontal = 20.dp)
                ) {
                    DefIcon(if (isShow) R.drawable.eye_off else R.drawable.eye, tint = DarkGray, size = 20.dp, modifier = Modifier.clickable {
                        isShow = !isShow
                    })
                }
            }
        }
    )
}