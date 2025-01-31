package com.contractors.app.presentation.ui.screen.changeData

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contractors.app.R
import com.contractors.app.data.network.RegistrationInfo
import com.contractors.app.presentation.ui.elements.components.DefText

import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalSendCodeViewModel
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.comment.FinalBtn
import com.contractors.app.presentation.ui.screen.sendCode.SendCodeAction
import com.contractors.app.presentation.ui.screen.sendCode.SendParam
import com.contractors.app.presentation.ui.screen.sendCode.SendType
import com.contractors.app.domain.utils.isValidEmail
import com.contractors.app.domain.utils.isValidPhone
import com.contractors.app.presentation.ui.screen.main.DataAction

enum class ChangeDataType {
    Email,
    Number,
    Pass
}

@Composable
fun ChangeData() {

    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val dataType = stateFlow.changeDataType

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(
                when (dataType) {
                    ChangeDataType.Email -> R.string.changeDataEmail
                    ChangeDataType.Number -> R.string.changeDataNumber
                    ChangeDataType.Pass -> R.string.returnedPass
                }
            ) {
                if (dataType == ChangeDataType.Pass) {
                    navController.navigate(Screen.Login.name)
                } else {
                    navController.navigate(Screen.Profile.name)
                }
            }
            Body()
        }
    }
}

@Composable
private fun Body() {
    val dataViewModel = LocalDataViewModel.current
    val navController = LocalNavController.current
    val sendCodeViewModel = LocalSendCodeViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val dataType = stateFlow.changeDataType

    Column(
        Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (dataType) {
            ChangeDataType.Email -> ChangeEmail {
                sendCodeViewModel.onAction(SendCodeAction.SetParam(SendParam(
                    SendType.Email,
                    email = it,
                    callback = {

                    }
                )))
                navController.navigate(Screen.SendCode.name)
            }
            ChangeDataType.Number -> ChangeNumber { number ->

                sendCodeViewModel.onAction(SendCodeAction.SetParam(SendParam(
                    SendType.Number,
                    number = number,
                    callback = {

                    }
                )))
                navController.navigate(Screen.SendCode.name)
            }
            ChangeDataType.Pass -> ChangePass { number ->
                dataViewModel.onAction(DataAction.ResetPassword(number))
                sendCodeViewModel.onAction(SendCodeAction.SetParam(SendParam(
                    SendType.ReturnPass,
                    number = number,
                    callback = {
                        navController.navigate(Screen.Profile.name)
                    }
                )))
                sendCodeViewModel.onAction(SendCodeAction.SetRegInfo(RegistrationInfo(phone = number)))
                navController.navigate(Screen.SendCode.name)
            }
        }
    }
}

@Composable
private fun ChangePass(success: (String) -> Unit) {
    var number by remember { mutableStateOf("") }
    var isNumberError by remember { mutableStateOf(false) }

    DefText(
        "Введите номер телефона",
        weight = FontWeight.Bold,
        size = 24,
        align = TextAlign.Center
    )

    DefTextField(
        number,
        title = "Номер телефона",
        errorText = "Неверно указан номер телефона",
        label = stringResource(R.string.numberHint),
        keyboardType = KeyboardType.Phone,
        onValueChange = {
            if (it.length < 11) {
                number = it
            }
        },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        isPhone = true,
        isError = isNumberError,
        onFocus = {
            if (number.isNotEmpty()) isNumberError = !number.isValidPhone()
        }
    )

    FinalBtn("Отправить код") {
        isNumberError = !number.isValidPhone()
        if (!isNumberError) success("7${number}")
    }
}

@Composable
private fun ChangeNumber(success: (String) -> Unit) {
    var number by remember { mutableStateOf("") }
    var isNumberError by remember { mutableStateOf(false) }

    DefText(
        "Введите новый номер телефона",
        weight = FontWeight.Bold,
        size = 24,
        align = TextAlign.Center
    )

    DefTextField(
        number,
        title = "Номер телефона",
        errorText = "Неверно указан номер телефона",
        label = stringResource(R.string.numberHint),
        keyboardType = KeyboardType.Phone,
        onValueChange = {
            if (it.length < 11) {
                number = it
            }
        },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        isPhone = true,
        isError = isNumberError,
        onFocus = {
            if (number.isNotEmpty()) isNumberError = !number.isValidPhone()
        }
    )

    FinalBtn("Отправить код") {
        isNumberError = !number.isValidPhone()
        if (!isNumberError) success("7$number")
    }
}

@Composable
private fun ChangeEmail(success: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }

    DefText(
        "Введите новый адрес электронной почты",
        weight = FontWeight.Bold,
        size = 24,
        align = TextAlign.Center
    )

    DefTextField(
        email,
        title = "Электронная почта",
        label = "Новый адрес эл. почты",
        errorText = "Некорректный формат Email",
        keyboardType = KeyboardType.Email,
        onValueChange = {
            email = it
        },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        isError = isEmailError,
        onFocus = {
            if (email.isNotEmpty()) isEmailError = !email.isValidEmail()
        }
    )

    FinalBtn("Отправить код") {
        isEmailError = !email.isValidEmail()
        if (!isEmailError) success(email)
    }
}