package com.contractors.app.presentation.ui.screen.reg

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.contractors.app.R
import com.contractors.app.data.network.RegistrationInfo
import com.contractors.app.domain.utils.ToastHelper
import com.contractors.app.domain.utils.isValidEmail
import com.contractors.app.domain.utils.isValidPhone
import com.contractors.app.presentation.ui.elements.Footer
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.elements.SelectInput
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.elements.components.DefTextFieldPassword
import com.contractors.app.presentation.ui.elements.components.RoleButton
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalSendCodeViewModel
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.login.LoginAction
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.screen.sendCode.SendCodeAction
import com.contractors.app.presentation.ui.screen.sendCode.SendParam
import com.contractors.app.presentation.ui.screen.sendCode.SendType
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.LightBlue
import com.contractors.app.presentation.ui.theme.White

enum class RegForm {
    Set,
    Master,
    Realtor,
    Customer,
    SuccessReg,
    SuccessPay,
    PayStatus,
    Pay,
    SuccessStatus
}

@Composable
fun RegView() {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val regNavController = rememberNavController()
    val loginViewModel = LocalLoginViewModel.current
    val sendCodeViewModel = LocalSendCodeViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val toastHelper = LocalToastHelper.current
    var form by remember { mutableStateOf(RegForm.Set) }
    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetSpecializations)
    }
    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            if (regNavController.currentBackStackEntryAsState().value?.destination?.route != RegForm.Set.name) {
                Header(R.string.reg) { navController.navigate(Screen.Reg.name) }
            } else {
                HeaderText(stringResource(R.string.reg))
            }

            Body(
                navController = regNavController,
                form = form,
                changeRegForm = {
                    form = it
                    regNavController.navigate(form.name)
                },
                reg = { registrationInfo ->
                    loginViewModel.onAction(LoginAction.Registration(registrationInfo) { code, error ->
                        if (code) {
                            sendCodeViewModel.onAction(SendCodeAction.SetParam(
                                SendParam(
                                    type = SendType.Reg,
                                    number = registrationInfo.phone,
                                    callback = {
                                        navController.navigate(Screen.SuccessReg.name)
                                    }
                                )
                            ))
                            navController.navigate(Screen.SendCode.name)
                        } else {
                            toastHelper.show(error)
                        }
                    })
                },
                toPay = {
                    form = RegForm.Pay
                    regNavController.navigate(form.name)
                },
                toStatus = {
                    form = RegForm.PayStatus
                    regNavController.navigate(form.name)
                },
                toProfile = {
                    navController.navigate(Screen.Profile.name)
                },
                updateStatysPage = {
                    form = RegForm.PayStatus
                    regNavController.navigate(form.name)
                },
                pay = {
                    form = RegForm.SuccessPay
                    regNavController.navigate(form.name)
                },
            )
        }
        if (RegForm.Set.name == regNavController.currentBackStackEntryAsState().value?.destination?.route) {
            Footer(
                startId = 0,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

    }
}

@Composable
private fun Body(
    navController: NavHostController,
    form: RegForm,
    changeRegForm: (RegForm) -> Unit,
    reg: (RegistrationInfo) -> Unit,
    toPay: () -> Unit,
    toStatus: () -> Unit,
    toProfile: () -> Unit,
    updateStatysPage: () -> Unit,
    pay: () -> Unit,
) {

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = form.name
    ) {
        composable(route = RegForm.Set.name) { SetProfession(changeRegForm = changeRegForm) }
        composable(route = RegForm.Master.name) { Forms(form, reg, toPay) }
        composable(route = RegForm.Customer.name) { Forms(form, reg, toPay) }
        composable(route = RegForm.Realtor.name) { Forms(form, reg, toPay) }
        composable(route = RegForm.SuccessPay.name) { SuccessPay(toStatus, toProfile) }
        composable(route = RegForm.Pay.name) { Pay(pay) }
        composable(route = RegForm.PayStatus.name) { PayStatus(updateStatysPage, toProfile) }
        composable(route = RegForm.SuccessStatus.name) { SuccessStatus(toProfile) }
    }
}

@Composable
fun Pay(
    pay: () -> Unit,
) {

    var card by remember { mutableStateOf("") }
    var term by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefImage(
                    R.drawable.visa,
                    Modifier
                        .width(80.dp)
                        .height(50.dp)
                )
                DefImage(
                    R.drawable.mastercard,
                    Modifier
                        .width(80.dp)
                        .height(50.dp)
                )
                DefImage(
                    R.drawable.maestro,
                    Modifier
                        .width(80.dp)
                        .height(50.dp)
                )
            }
            DefTextField(
                card,
                title = stringResource(R.string.cardNumber),
                label = stringResource(R.string.setCardNumber),
                keyboardType = KeyboardType.Number,
                startPadding = 5,
                onValueChange = {
                    card = it
                },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            )
            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefTextField(
                    term,
                    title = stringResource(R.string.term),
                    startPadding = 5,
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        term = it
                    },
                    modifier = Modifier
                        .width(140.dp)
                )
                DefTextField(
                    cvv,
                    title = stringResource(R.string.ccv),
                    startPadding = 5,
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        cvv = it
                    },
                    modifier = Modifier
                        .width(140.dp)
                )
            }
            DefButton(
                stringResource(R.string.pay1000),
                modifier = Modifier.padding(top = 30.dp)
            ) { pay() }
        }
    }

}


@Composable
fun Forms(
    regForm: RegForm,
    reg: (RegistrationInfo) -> Unit,
    toPay: () -> Unit
) {

    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var sureName by remember { mutableStateOf("") }
    var special by remember { mutableStateOf(if (regForm == RegForm.Realtor) "Риелтор" else "") }
    var skills by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passRepeat by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var conditions2 by remember { mutableStateOf(false) }
    var isNumberError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPassError by remember { mutableStateOf(false) }
    var isPassRepeatError by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Column(
            Modifier.fillMaxWidth()
        ) {
            DefTextField(
                text = lastName,
                title = "Фамилия*",
                label = "Ваша фамилия",
                onValueChange = {
                    lastName = it
                },
                isError = errorList.contains(1),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            DefTextField(
                name,
                title = "Имя*",
                label = "Ваше имя",
                onValueChange = {
                    name = it
                },
                isError = errorList.contains(2),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            DefTextField(
                sureName,
                title = "Отчество",
                label = "Ваше отчество",
                supportText = "(если оно есть)",
                onValueChange = {
                    sureName = it
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )

            if (regForm == RegForm.Master) {
                SelectInput(
                    title = "Специальность*",
                    label = "Ваша специальность",
                    text = special,
                    list = stateFlow.specializations,
                    isSingle = true, // was false
                    onChange = { special = it },
                    isError = errorList.contains(3),
                    modifier = Modifier.padding(top = 10.dp)
                )
                println(" my special == ${special}")
            }

            AnimatedVisibility(regForm == RegForm.Master) {
                SelectInput(
                    title = "Опыт работы*",
                    label = "Ваш опыт работы",
                    skills,
                    list = skillsList,
                    isSingle = true,
                    isError = errorList.contains(4),
                    onChange = { skills = it },
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            DefTextField(
                email,
                title = "Электронная почта*",
                label = "Ваша электронная почта",
                errorText = "Некорректный формат Email",
                keyboardType = KeyboardType.Email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!email.isValidEmail() && email.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(9)
                            }
                        }
                    },
                isError = isEmailError || errorList.contains(9),
                onFocus = {
                    if (email.isNotEmpty()) isEmailError = !email.isValidEmail()
                }
            )

            DefTextField(
                number,
                title = "Номер телефона*",
                errorText = "Неверно указан номер телефона",
                label = stringResource(R.string.numberHint),
                keyboardType = KeyboardType.Phone,
                onValueChange = {
                    if (it.length < 11) {
                        number = it
                    }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!number.isValidPhone() && number.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(8)
                            }
                        }
                    },
                isPhone = true,
                isError = isNumberError || errorList.contains(8),
                onFocus = {
                    if (number.isNotEmpty()) isNumberError = !number.isValidPhone()
                }
            )

            DefTextFieldPassword(
                text = pass,
                title = stringResource(R.string.setPass),
                label = stringResource(R.string.pass),
                supportText = "Не менее 8 символов",
                errorText = "Пароль должен содержать не менее 8 символов",
                onValueChange = {
                    pass = it
                },
                isPass = true,
                isError = isPassError || errorList.contains(10),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (pass.length < 8 && pass.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(10)
                            }
                        }
                    },
                onFocus = {
                    if (pass.isNotEmpty()) isPassError = pass.length < 8
                }
            )

            DefTextFieldPassword(
                passRepeat,
                title = stringResource(R.string.agree_pass),
                label = stringResource(R.string.repeat_pass),
                errorText = "Пароли должны совпадать",
                onValueChange = {
                    passRepeat = it
                },
                isPass = true,
                isError = isPassRepeatError || errorList.contains(11),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (pass != passRepeat && passRepeat.isNotEmpty()) {
                            errorList = mutableListOf<Int>().apply {
                                addAll(errorList)
                                add(11)
                            }
                        }
                    },
                onFocus = {
                    if (passRepeat.isNotEmpty()) isPassRepeatError = pass != passRepeat
                }
            )

        }
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
            isError = errorList.contains(5),
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
            isError = errorList.contains(6),
            onChange = {
                conditions1 = it
            }
        )
        CheckBoxRow(
            buildAnnotatedString {
                append("Обязуюсь выполнять правила тех политики приложения")
            },
            conditions2,
            isError = errorList.contains(7),
            onChange = {
                conditions2 = it
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            DefButton(
                stringResource(if (regForm == RegForm.Realtor) R.string.nextPay else R.string.reg),
                modifier = Modifier
                    .imePadding()
                    .padding(bottom = 20.dp)
            ) {
                checkRegistrations(
                    context = context,
                    toastHelper = toastHelper,
                    info = RegistrationInfo(
                        fio = "$sureName $name $lastName",
                        sure_name = sureName,
                        name = name,
                        last_name = lastName,
                        special = special,
                        experience = skills,
                        email = email,
                        phone = "7${number}",
                        user_type_id = when (regForm) {
                            RegForm.Master -> "1"
                            RegForm.Realtor -> "2"
                            RegForm.Customer -> "3"
                            else -> "0"
                        },
                        password = pass,
                        passwordRepeat = passRepeat,
                        condition = conditions,
                        condition1 = conditions1,
                        condition2 = conditions2
                    ),
                    condition = conditions,
                    regForm = regForm,
                    callbackError = {
                        errorList = it
                    },
                    reg = reg,
                )
            }
        }
    }
}

@Composable
fun CheckBoxRow(
    text: AnnotatedString,
    value: Boolean,
    isError: Boolean = false,
    onChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            value,
            modifier = Modifier.offset(x = -13.dp),
            onCheckedChange = onChange,
            colors = CheckboxDefaults.colors(
                checkedColor = LightBlue,
                uncheckedColor = if (isError) Red else DarkGray,
                checkmarkColor = White
            )
        )
        Text(
            text,
            fontSize = 12.sp,
            color = Black,
            lineHeight = 14.sp,
            modifier = Modifier.offset(x = -20.dp)
        )
    }
}

private fun checkRegistrations(
    context: Context,
    toastHelper: ToastHelper,
    info: RegistrationInfo,
    condition: Boolean,
    regForm: RegForm,
    callbackError: (List<Int>) -> Unit,
    reg: (RegistrationInfo) -> Unit
) {

    val errorList = mutableListOf<Int>()

    if (info.last_name.isEmpty()) errorList.add(1)
    if (info.name.isEmpty()) errorList.add(2)
    if (regForm == RegForm.Master && info.special.isEmpty()) errorList.add(3)
    if (regForm == RegForm.Master && info.experience.isEmpty()) errorList.add(4)
    if (!info.condition) errorList.add(5)
    if (!info.condition1) errorList.add(6)
    if (!info.condition2) errorList.add(7)
    if (!info.phone.isValidPhone()) errorList.add(8)
    if (!info.email.isValidEmail()) errorList.add(9)
    if (info.password.length < 8) errorList.add(10)
    if (info.password != info.passwordRepeat) errorList.add(11)
    if (errorList.isNotEmpty()) {
        callbackError(errorList)
        toastHelper.show("Заполните необходимые поля")
        return
    }

    reg(info)
}

@Composable
fun SetProfession(
    changeRegForm: (RegForm) -> Unit
) {
    var isRoleButtonClicked by remember { mutableStateOf(false) }
    LazyColumn(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            RoleButton(
                backgroundImageId = R.drawable.start_orange,
                imageId = R.drawable.master,
                textTitle = stringResource(R.string.master),
                textColor = White,
                textWeight = FontWeight.Normal,
                textDescription = stringResource(R.string.description_master),
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                changeRegForm(RegForm.Master)
            }
        }
        item {
            RoleButton(
                backgroundImageId = R.drawable.start_blue,
                imageId = R.drawable.customer,
                textTitle = stringResource(R.string.customer),
                textColor = White,
                textWeight = FontWeight.Normal,
                textDescription = stringResource(R.string.description_customer),
                modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)
            ) {
                changeRegForm(RegForm.Customer)
            }
        }
        item {
            RoleButton(
                backgroundImageId = R.drawable.start_green,
                imageId = R.drawable.realtor,
                textTitle = stringResource(R.string.realtor),
                textColor = White,
                textWeight = FontWeight.Normal,
                textDescription = stringResource(R.string.description_realtor),
                modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)
            ) {
                changeRegForm(RegForm.Realtor)
            }
        }
        item {
            Spacer(Modifier.height(72.dp))
        }
    }
}