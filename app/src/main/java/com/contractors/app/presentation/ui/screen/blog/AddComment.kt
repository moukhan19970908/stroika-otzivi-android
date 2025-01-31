package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.screen.comment.CommentBlock1
import com.contractors.app.presentation.ui.screen.comment.FinalBtn
import com.contractors.app.presentation.ui.screen.reg.CheckBoxRow

@Composable
fun AddCommentScreen(modifier: Modifier = Modifier) {

}

@Composable
fun AddComment(
    modifier: Modifier = Modifier,
    setComment: (String) -> Unit
) {
    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current

    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }
    var comment by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(horizontal = 16.dp).padding(top = 24.dp)) {

        CommentBlock1(
            title = "Комментарий",
            label = "Оставьте ваш комментарий",
            text = comment,
            onChange = { comment = it },
            modifier = Modifier
                .padding(top = 10.dp),
            isError = errorList.contains(3)
        )
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
            isError = errorList.contains(1),
            onChange = {
                conditions = it
            }
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
            isError = errorList.contains(2),
            onChange = {
                conditions1 = it
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FinalBtn {
                val errors = mutableListOf<Int>()
                if (!conditions) errors.add(1)
                if (!conditions1) errors.add(2)
                if (comment.isEmpty()) errors.add(3)

                if (errors.isNotEmpty()) {
                    errorList = errors
                    return@FinalBtn
                }

                setComment(comment)
                comment = ""
            }
        }
        Spacer(Modifier.height(50.dp))
    }
}