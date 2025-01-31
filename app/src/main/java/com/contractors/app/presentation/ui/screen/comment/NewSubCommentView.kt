package com.contractors.app.presentation.ui.screen.comment

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
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.reg.CheckBoxRow

@Composable
fun NewSubCommentView() {
    val navController = LocalNavController.current

    Column(
        Modifier.fillMaxSize()
    ) {
        Header(R.string.createSubComment) {
            navController.navigate(Screen.Comment.name)
        }
        Body(success = { navController.navigate(Screen.Comment.name) })
    }
}

@Composable
private fun Body(success: () -> Unit) {

    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val toastHelper = LocalToastHelper.current
    var text by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp).padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefTextField(
            text,
            title = "Комментарий",
            label = "Оставьте ваш комментарий",
            onValueChange = {
                text = it
            },
            singleLine = false,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(100.dp),
            isError = errorList.contains(1),
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
                isError = errorList.contains(2),
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
                isError = errorList.contains(3),
            )
        }

        FinalBtn {

            val errors = mutableListOf<Int>()
            if (text.isEmpty()) errors.add(1)
            if (!conditions) errors.add(2)
            if (!conditions1) errors.add(3)

            if (errors.isNotEmpty()) {
                errorList = errors
                return@FinalBtn
            }

            success()
        }
    }
}