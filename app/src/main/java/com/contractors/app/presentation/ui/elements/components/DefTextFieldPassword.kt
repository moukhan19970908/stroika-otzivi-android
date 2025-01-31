package com.contractors.app.presentation.ui.elements.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contractors.app.R
import com.contractors.app.domain.utils.keyboardAsState
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.VeryDarkGray

@Composable
fun DefTextFieldPassword(
    text: String,
    label: String = "",
    supportText: String = "",
    errorText: String = "",
    title: String = "",
    labelSize: Int = 16,
    startPadding: Int = 1,
    clip: Int = 5,
    onValueChange: (String) -> Unit,
    onFocus: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    backgroundColor: Color = Color.White,
    border: Color = Gray,
    isPass: Boolean = false,
    isPhone: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 5,
) {

    var focus by remember { mutableStateOf(false) }
    val color = if (isError) Red else if (focus) DarkGray else border
    val isKeyboardOpen by keyboardAsState()

    LaunchedEffect(isKeyboardOpen) {
        if (!isKeyboardOpen) onFocus(false)
    }
    var isPasswordShown by remember { mutableStateOf(false) }
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPhone) KeyboardType.Number else keyboardType),
        visualTransformation =
        if (isPass && !isPasswordShown) PasswordVisualTransformation()
        else VisualTransformation.None,
        singleLine = singleLine,
        modifier = Modifier.onFocusChanged {
            focus = it.isFocused
            onFocus(it.isFocused)
        },
        maxLines = maxLines,
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
        decorationBox = { innerTextField ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .height(70.dp)
                        .background(backgroundColor, RoundedCornerShape(clip.dp))
                        .border(1.dp, color, RoundedCornerShape(clip.dp))
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(start = startPadding.dp)
                    ) {
                        DefText(
                            title,
                            color = VeryDarkGray,
                            size = 14,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Box() {
                            if (label.isNotEmpty() && text.isEmpty() && !focus) DefText(
                                label,
                                color = Gray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            else innerTextField()
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.End,
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        DefImage(
                            imageId = if (isPasswordShown) R.drawable.eye else R.drawable.eye_off, contentScale = ContentScale.Fit, modifier =
                            Modifier
                                .size(24.dp)
                                .clickable(
                                    onClick = {
                                        isPasswordShown = !isPasswordShown
                                    },
                                    indication = null,
                                    interactionSource = interactionSource
                                )
                        )
                    }


                }
                AnimatedVisibility(supportText.isNotEmpty() || errorText.isNotEmpty() && isError) {
                    DefText(
                        if (isError && errorText.isNotEmpty()) errorText else supportText,
                        color = if (isError && errorText.isNotEmpty()) Red else Gray,
                        size = 12,
                        modifier = Modifier.padding(start = 17.dp)
                    )
                }
            }
        }
    )
}
