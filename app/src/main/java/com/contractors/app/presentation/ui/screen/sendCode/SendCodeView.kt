package com.contractors.app.presentation.ui.screen.sendCode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.BoxField
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalSendCodeViewModel
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.login.LoginAction
import com.contractors.app.presentation.ui.theme.LightBlue
import com.contractors.app.domain.utils.SEND_CODE_DELAY
import com.contractors.app.domain.utils.formatPhoneNumber
import kotlinx.coroutines.delay

@Composable
fun SendCodeView() {
    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val sendCodeViewModel = LocalSendCodeViewModel.current
    val toastHelper = LocalToastHelper.current
    var counter by remember { mutableStateOf(SEND_CODE_DELAY) }
    var isError by remember { mutableStateOf(false) }
    val param = sendCodeViewModel.state.param
    val registrationInfo = sendCodeViewModel.state.registrationInfo

    val text1 =
        if (param.number.isNotEmpty())
            stringResource(R.string.sendCode1)
        else
            "Введите код из смс"
    val text2 =
        if (param.number.isNotEmpty())
            "${stringResource(R.string.sendCode2)} ${formatPhoneNumber("+${param.number}")} ${stringResource(R.string.sendCode21)}\n${stringResource(R.string.sendCode3)}"
        else
            "На адрес ${param.email}\nотправлено письмо с кодом "


    LaunchedEffect(counter) {
        delay(1000)
        if (counter > 0)
            counter--
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            when(param.type) {
                SendType.Reg -> R.string.reg
                SendType.ReturnPass -> R.string.returnedPass
                SendType.Email -> R.string.changeDataEmail
                SendType.Number -> R.string.changeDataNumber
            }
        ) {
            navController.popBackStack()
        }

        DefText(text1, size = 20, modifier = Modifier.padding(top = 20.dp))
        DefText(text2, size = 14, modifier = Modifier.padding(top = 20.dp), align = TextAlign.Center)

        BoxField(
            isError = isError,
            modifier = Modifier.padding(top = 20.dp)
        ) { code ->
            if (code.isEmpty()) {
                isError = false
                return@BoxField
            }

            loginViewModel.onAction(LoginAction.Verify(param.number, code) { isSuccess, error ->
                if (isSuccess) {
                    param.callback(true)
                } else {
                    isError = true
                    toastHelper.show(error)
                }
            })
        }
        if (isError) {
            DefText(
                stringResource(R.string.sendCode5), size = 14, color = Red, modifier = Modifier.padding(top = 5.dp),
                align = TextAlign.Center, weight = FontWeight.Bold)
        }

        if (counter == 0) {
            DefText(
                stringResource(R.string.sendCode6), size = 14, color = LightBlue, modifier = Modifier.padding(top = 10.dp).clickable {
                    counter = SEND_CODE_DELAY
                    loginViewModel.onAction(LoginAction.Registration(registrationInfo) { code, error -> })
                },
                align = TextAlign.Center, weight = FontWeight.Bold)
        } else {
            DefText(
                "${stringResource(R.string.sendCode4)} ${formatTime(counter)}", size = 14, modifier = Modifier.padding(top = 10.dp),
                align = TextAlign.Center, weight = FontWeight.Bold)
        }
    }
}

fun formatTime(t: Int) : String{
    var minutes = (t % 3600) / 60
    var seconds = t % 60

    return "${if (minutes < 10) "0" else ""}$minutes:${if (seconds < 10) "0" else ""}$seconds"
}