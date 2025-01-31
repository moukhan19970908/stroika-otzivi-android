package com.contractors.app.presentation.ui.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.changeData.ChangeDataType
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.FadedBlue
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.domain.utils.isValidEmail
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.screen.blog.Header

@Composable
fun LoginView() {

    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {

            Header(R.string.enter) { navController.navigate(Screen.Reg.name) }
            Body(
                login = { num, pass ->
                    loginViewModel.onAction(LoginAction.Login(num, pass) { result, error ->
                        if (result) navController.popBackStack()
                        else toastHelper.show(error)
                    })
                },
                reg = {
                    navController.navigate(Screen.Reg.name)
                },
                returnPass = {
                    dataViewModel.onAction(DataAction.SetDataType(ChangeDataType.Pass))
                    navController.navigate(Screen.ChangeData.name)
                }
            )
        }
    }
}

@Composable
private fun Body(
    login: (String, String) -> Unit = { _, _ -> },
    returnPass: () -> Unit = {},
    googleLogin: () -> Unit = {},
    reg: () -> Unit = {},
) {
    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isPassError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.width(300.dp)
        ) {
            DefTextField(
                email,
                title = stringResource(R.string.email2),
                label = stringResource(R.string.email2),
                errorText = "Некорректный формат Email",
                keyboardType = KeyboardType.Email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!email.isValidEmail() && email.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(1)
                            }
                        }
                    }
                ,
                isError = isEmailError || errorList.contains(1),
                onFocus = {
                    if (email.isNotEmpty()) isEmailError = !email.isValidEmail()
                }
            )

            DefTextField(
                text = pass,
                title = stringResource(R.string.pass),
                label = stringResource(R.string.passError),
                supportText = "Не менее 8 символов",
                errorText = "Пароль должен содержать не менее 8 символов",
                onValueChange = {
                    pass = it
                },
                isPass = true,
                isError = isPassError || errorList.contains(2),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (pass.length < 8 && email.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(2)
                            }
                        }
                    },
                onFocus = {
                    if (pass.isNotEmpty()) isPassError = pass.length < 8
                }
            )
            DefButton(
                stringResource(R.string.login),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                val errors = mutableListOf<Int>()
                if (email.isEmpty()) errors.add(1)
                if (pass.isEmpty()) errors.add(2)

                if (errors.isNotEmpty()) {
                    toastHelper.show(context.getString(R.string.emailError2))
                    errorList = errors
                    return@DefButton
                }

                login(email, pass)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefText(
                    stringResource(R.string.returnPass),
                    color = Blue,
                    weight = FontWeight.Bold,
                    size = 14,
                    modifier = Modifier.clickable { returnPass() }
                )
            }
        }
        Row(
            Modifier.padding(top = 20.dp)
        ) {
            DefText(stringResource(R.string.ifNotReg), color = FadedBlue, size = 13)
            DefText(
                stringResource(R.string.toReg),
                color = Orange,
                size = 13,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        reg()
                    })

        }
    }
}