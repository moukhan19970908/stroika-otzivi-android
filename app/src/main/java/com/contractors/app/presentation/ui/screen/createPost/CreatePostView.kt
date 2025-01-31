package com.contractors.app.presentation.ui.screen.createPost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.data.network.CreatePostInfo
import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.elements.SelectInput
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.comment.FinalBtn
import com.contractors.app.presentation.ui.screen.comment.PhotoBlock
import com.contractors.app.presentation.ui.elements.RequestLocationPermission
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.screen.reg.CheckBoxRow
import com.contractors.app.presentation.ui.screen.reg.objectTypeList
import com.contractors.app.domain.utils.isValidPhone
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.main.DataViewModel

@Composable
fun CreatePostView() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current
    val toastHelper = LocalToastHelper.current

    RequestLocationPermission()

    Column(
        Modifier.fillMaxSize()
    ) {
        Header(R.string.createPost) {
            navController.popBackStack()
        }

        Body(
            create = { createPostInfo ->
                dataViewModel.onAction(
                    DataAction.CreatePost(
                        createPostInfo,
                        loginViewModel.state.token
                    ) { result, error ->
                        if (result) {
                            toastHelper.show("Объект будет опубликован после проверки администратором")
                            navController.navigate(Screen.Profile.name)
                        } else
                            toastHelper.show(error)
                    })
            },
            loadPhoto = {

            }
        )
    }
}

@Composable
private fun Body(create: (CreatePostInfo) -> Unit, loadPhoto: () -> Unit) {

    val toastHelper = LocalToastHelper.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val latitude by remember { mutableStateOf(0.0) }
    val longitude by remember { mutableStateOf(0.0) }
    var images by remember { mutableStateOf(listOf<Int>()) }
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }
    val isNumberError by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { dataViewModel.onAction(DataAction.GetObjectTypes()) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DefTextField(
            text = title,
            title = "Название*",
            label = "Название объекта",
            onValueChange = {
                title = it
            },
            errorText = "Укажите название объекта",
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            isError = errorList.contains(4)
        )

        DefTextField(
            address,
            title = "Адрес*",
            label = "Область, район, город, улица, номер дома",
            errorText = "Укажите адрес объекта",
            onValueChange = {
                address = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            isError = errorList.contains(1)
        )

        SelectInput(
            title = "Тип объекта*",
            label = "Выберите тип объекта",
            text = type,
            errorText = "Укажите тип объекта",
            list = dataViewModel.state.objectTypes,
            isSingle = true,
            onChange = { type = it },
            modifier = Modifier.padding(top = 10.dp),
            isError = errorList.contains(2)
        )


        DefTextField(
            text = description,
            title = "Описание*",
            label = "Описание объекта",
            errorText = "Укажите описание объекта",
            onValueChange = {
                description = it
            },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(100.dp),
            isError = errorList.contains(7)
        )

        if (loginViewModel.state.userInfo.role == Role.Master) {
            DefTextField(
                name,
                title = "ФИО заказчика*",
                label = "Имя заказчика",
                supportText = "Будет скрыто",
                errorText = "Укажите ФИО заказчика",
                onValueChange = {
                    name = it
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                isError = errorList.contains(4)
            )
            DefTextField(
                number,
                title = "Номер телефона заказчика",
                label = stringResource(R.string.numberHint),
                supportText = "Будет скрыт. Указывать необязательно",
                onValueChange = {
                    if (it.length < 11) {
                        number = it
                    }
                },
                isError = isNumberError,
                isPhone = true
            )
        }
        PhotoBlock(
            loaded = {
                images = it
            },
            listNull = {
                images = emptyList()
            },
            isError = errorList.contains(3),
            modifier = Modifier.padding(top = 10.dp),
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
                value = conditions,
                onChange = {
                    conditions = it
                },
                isError = errorList.contains(5)
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
                isError = errorList.contains(6)
            )
        }

        FinalBtn {

            val errors = mutableListOf<Int>()
            if (address.isEmpty()) errors.add(1)
            if (type.isEmpty()) errors.add(2)
            if (images.isEmpty()) errors.add(3)
            if (title.isEmpty()) errors.add(4)
            if (description.isEmpty()) errors.add(7)
            if (!conditions) errors.add(5)
            if (!conditions1) errors.add(6)
//            if (loginViewModel.state.userInfo.role == Role.Master) errors.add(8)

            if (errors.isNotEmpty()) {
                errorList = errors
                toastHelper.show("Заполните необходимые поля")
                return@FinalBtn
            }

            create(
                CreatePostInfo(
                    title,
                    description,
                    latitude,
                    longitude,
                    address,
                    images.toTypedArray(),
                    type_id = dataViewModel.state.objectTypes.indexOf(type)+1,
                    customer_name = loginViewModel.state.userInfo.name + " " + loginViewModel.state.userInfo.surname
                )
            )
        }
    }
}