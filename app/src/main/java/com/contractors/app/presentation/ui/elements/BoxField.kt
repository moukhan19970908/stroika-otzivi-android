package com.contractors.app.presentation.ui.elements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contractors.app.domain.utils.isValidEmail
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.StrokeActive
import com.contractors.app.presentation.ui.theme.White

@Composable
fun BoxField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    finalText: (String) -> Unit,
) {
    val focusRequester = remember { List(4) { FocusRequester() } }
    var textList by remember { mutableStateOf(MutableList(4) { "" }) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleDigitInput(
            focusRequester[0],
            isError,
            onValueChanged = {
                textList[0] = it
                if (it.isNotEmpty()) focusRequester[1].requestFocus()
                else focusRequester[1].freeFocus()
            }
        )
        SingleDigitInput(
            focusRequester[1],
            isError,
            onValueChanged = {
                textList[1] = it
                if (textList.filter { it.isEmpty() }.isNotEmpty()) {
                    finalText("")
                }
                if (it.isNotEmpty()) focusRequester[2].requestFocus()
                else focusRequester[0].requestFocus()
            }
        )
        SingleDigitInput(
            focusRequester[2],
            isError,
            onValueChanged = {
                textList[2] = it
                if (it.isNotEmpty()) focusRequester[3].requestFocus()
                else focusRequester[1].requestFocus()
            }
        )
        SingleDigitInput(
            focusRequester[3],
            isError,
            onValueChanged = {
                textList[3] = it
                if (textList.filter { it.isEmpty() }.isEmpty()) {
                    finalText(textList.joinToString(separator = "") { it })
                }
                if (it.isEmpty()) focusRequester[2].requestFocus()
            }
        )
    }
}

@Composable
fun SingleDigitInput(
    focusRequester: FocusRequester,
    isError: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(1.dp, if (isError) Red else if (isFocused || text.isNotBlank()) StrokeActive else Gray, RoundedCornerShape(4.dp))
            .background(color = White, shape = RoundedCornerShape(4.dp))
            ,
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                onValueChanged(it)
                if (it.length <= 1) {
                    text = it
                }
            },
            textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = "",
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .align(Alignment.Center)
                .onFocusChanged { isFocused = it.isFocused }


        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleDigitInputPreview() {
    SingleDigitInput(focusRequester = FocusRequester(), isError = false, onValueChanged = {})

}