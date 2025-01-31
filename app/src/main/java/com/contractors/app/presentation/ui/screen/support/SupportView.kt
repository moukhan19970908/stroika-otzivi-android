package com.contractors.app.presentation.ui.screen.support

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.comment.FinalBtn
import com.contractors.app.presentation.ui.screen.reg.CheckBoxRow

@Composable
fun SupportView() {

    val navController = LocalNavController.current

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(R.string.supportHeader) {
                navController.navigate(Screen.Profile.name)
            }
            Body()
        }
    }
}

@Composable
private fun Body() {

    var theme by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }

    Column(
        Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefTextField(
            theme,
            title = "Тема обращения*",
            label = "Напишите тему обращения",
            onValueChange = {
                theme = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            isError = errorList.contains(1)
        )
        DefTextField(
            text,
            title = "Текст обращения*",
            label = "Текст обращения",
            onValueChange = {
                text = it
            },
            singleLine = false,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(100.dp),
            isError = errorList.contains(2)
        )
        Column {
            CheckBoxRow(
                buildAnnotatedString {
                    append("Даю ")
                    withStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("согласие на обработку персональных данных")
                    }
                },
                conditions,
                onChange = {
                    conditions = it
                },
                isError = errorList.contains(3)
            )
            CheckBoxRow(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Я ознакомлен с частью законодательства РФ о нарушении в случае предоставления ложной и недостоверной информации")
                    }
                },
                conditions1,
                onChange = {
                    conditions1 = it
                },
                isError = errorList.contains(4)
            )
        }

        FinalBtn {

            val errors = mutableListOf<Int>()
            if (theme.isEmpty()) errors.add(1)
            if (text.isEmpty()) errors.add(2)

            if (!conditions) errors.add(3)
            if (!conditions1) errors.add(4)

            if (errors.isNotEmpty()) {
                errorList = errors
                return@FinalBtn
            }


        }
    }
}