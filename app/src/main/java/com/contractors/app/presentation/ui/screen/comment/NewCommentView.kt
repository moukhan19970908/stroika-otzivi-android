package com.contractors.app.presentation.ui.screen.comment

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.MasterCommentInfo
import com.contractors.app.data.network.RealtorCommentInfo
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.presentation.ui.elements.components.DefBasicTextField
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.elements.components.DefTextField
import com.contractors.app.presentation.ui.elements.SelectInput
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.screen.reg.CheckBoxRow
import com.contractors.app.presentation.ui.screen.reg.roleList
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.presentation.ui.theme.White
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.getFileFromUri
import com.contractors.app.domain.utils.isValidPhone
import com.contractors.app.presentation.ui.screen.main.DataState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NewCommentView() {
    val navController = LocalNavController.current
    var success by remember { mutableStateOf(false) }
    val loginViewModel = LocalLoginViewModel.current

    Column(
        Modifier.fillMaxSize()
    ) {
        Header(
            if (success || loginViewModel.state.userInfo.role != Role.Realtor) R.string.createFeedback else R.string.youComment
        ) {
            if (success) navController.popBackStack()
            else navController.navigate(Screen.Item.name)
        }
        AnimatedContent(targetState = success, label = "success") {
            when (it) {
                true -> SuccessStatus { navController.popBackStack() }
                false -> Body(success = { success = true })
            }
        }

    }
}

@Composable
private fun Body(success: () -> Unit) {

    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val toastHelper = LocalToastHelper.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        when (loginViewModel.state.userInfo.role) {
            Role.Realtor -> NewCommentRealtor(
                stateFlow = stateFlow,
                success = {
                dataViewModel.onAction(
                    DataAction.AddRealtorComment(
                        it,
                        loginViewModel.state.token
                    ) { result, error ->
                        if (result) {
                            dataViewModel.onAction(DataAction.SetPost(stateFlow.selectedPost.toPostDTO()))
                            navController.navigate(Screen.Item.name)
                        } else toastHelper.show(error)
                    })
            })

            Role.Master -> NewCommentMaster(
                stateFlow = stateFlow,
                success = {
                dataViewModel.onAction(
                    DataAction.AddMasterComment(
                        it,
                        loginViewModel.state.token
                    ) { result, error ->
                        if (result) {
                            dataViewModel.onAction(DataAction.SetPost(stateFlow.selectedPost.toPostDTO()))
                            navController.navigate(Screen.Item.name)
                        } else toastHelper.show(error)
                    })
            })

            else -> {}
        }
    }
}

@Composable
fun NewCommentMaster(stateFlow: DataState, success: (MasterCommentInfo) -> Unit) {

    val toastHelper = LocalToastHelper.current
    val context = LocalContext.current

    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    var role by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }
    var ad by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var isNumberError by remember { mutableStateOf(false) }
    var grade1 by remember { mutableStateOf(0) }
    var grade2 by remember { mutableStateOf(0) }
    var grade3 by remember { mutableStateOf(0) }
    var grade4 by remember { mutableStateOf(0) }
    var grade5 by remember { mutableStateOf(0) }
    var workType by remember { mutableStateOf("") }
    var images by remember { mutableStateOf(listOf<Int>()) }
    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }
    var errorList: List<Int> by remember { mutableStateOf(emptyList()) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SelectInput(
            "Роль на объекте*",
            "Выберите вашу роль",
            role,
            list = roleList,
            isSingle = true,
            isError = errorList.contains(1),
            onChange = { role = it },
            modifier = Modifier.padding(top = 10.dp)
        )
        DefTextField(
            workType,
            title = "Виды работ*",
            label = "Выполняемые работы",
            onValueChange = {
                workType = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .height(100.dp)
                .fillMaxWidth(),
            singleLine = false,
            isError = errorList.contains(2)
        )
        DefTextField(
            name,
            title = "Имя заказчика*",
            label = "Имя заказчика",
            supportText = "Будет скрыто",
            onValueChange = {
                name = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            isError = errorList.contains(3),
        )
        DefTextField(
            number,
            title = "Номер телефона заказчика",
            errorText = "Неверно указан номер телефона",
            label = stringResource(R.string.numberHint),
            supportText = "Будет скрыт. Указывать необязательно",
            onValueChange = {
                if (it.length < 11) {
                    number = it
                }
            },
            isPhone = true,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            isError = isNumberError,
            onFocus = {
                if (number.isNotEmpty()) isNumberError = !number.isValidPhone()
            }
        )
        DefTextField(
            exp,
            title = "Отзыв об объекте*",
            label = "Опишите ваш опыт работы на данном объекте",
            onValueChange = {
                exp = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .height(100.dp)
                .fillMaxWidth(),
            singleLine = false,
            isError = errorList.contains(4)
        )
        DefTextField(
            ad,
            title = "Рекомендации заказчику*",
            label = "Ваши рекомендации заказчику объекта",
            onValueChange = {
                ad = it
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .height(100.dp)
                .fillMaxWidth(),
            singleLine = false,
            isError = errorList.contains(5)
        )
        GradeBlock(
            R.string.masterT3,
            grade1,
            onChange = { grade1 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10,
            isError = errorList.contains(5),
        )
        GradeBlock(
            R.string.masterT4,
            grade2,
            onChange = { grade2 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10,
            isError = errorList.contains(6),
        )
        GradeBlock(
            R.string.masterT5,
            grade3,
            onChange = { grade3 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10,
            isError = errorList.contains(7),
        )
        GradeBlock(
            R.string.masterT6,
            grade4,
            onChange = { grade4 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10,
            isError = errorList.contains(8),
        )
        GradeBlock(
            R.string.masterT7,
            grade5,
            onChange = { grade5 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10,
            isError = errorList.contains(9),
        )

        PhotoBlock(
            loaded = {
                images = it
            },
            listNull = {
                images = emptyList()
            },
            Modifier.padding(top = 20.dp)
        )

        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
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
                isError = errorList.contains(10),
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
                isError = errorList.contains(11),
            )
        }

        FinalBtn(
            success = {
                val errors = mutableListOf<Int>()

                if (role.isEmpty()) errors.add(1)
                if (workType.isEmpty()) errors.add(2)
//                if (name.isEmpty()) errors.add(3)
                if (exp.isEmpty()) errors.add(4)
                if (ad.isEmpty()) errors.add(5)
                if (grade1 == 0) errors.add(5)
                if (grade2 == 0) errors.add(6)
                if (grade3 == 0) errors.add(7)
                if (grade4 == 0) errors.add(8)
                if (grade5 == 0) errors.add(9)
                if (!conditions) errors.add(10)
                if (!conditions1) errors.add(11)
                Log.e("TEST", loginViewModel.state.userInfo.toString())

                if (errorList.isNotEmpty()) {
                    errorList = errors
                    toastHelper.show("Заполните необходимые поля")
                    return@FinalBtn
                }

                success(
                    MasterCommentInfo(
                        role,
                        workType,
                        loginViewModel.state.userInfo.name,
                        loginViewModel.state.userInfo.number,
                        exp,
                        ad,
                        grade1,
                        grade2,
                        grade3,
                        grade4,
                        grade5,
                        stateFlow.selectedPost.id,
                        images.toTypedArray()
                    )
                )
            }
        )
    }
}

@Composable
fun NewCommentRealtor(stateFlow: DataState, success: (RealtorCommentInfo) -> Unit) {

    val dataViewModel = LocalDataViewModel.current

    var ad by remember { mutableStateOf("") }
    var disAd by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf(0) }

    var images by remember { mutableStateOf(listOf<Int>()) }

    var conditions by remember { mutableStateOf(false) }
    var conditions1 by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CommentBlock1(
            "Достоинства объекта*",
            "Опишите достоинства объекта",
            ad,
            { ad = it }
        )
        CommentBlock1(
            "Недостатки объекта*",
            "Опишите недостатки объекта",
            disAd,
            { disAd = it },
            Modifier.padding(top = 10.dp)
        )
        PhotoBlock(
            loaded = {
                images = it
            },
            listNull = {
                images = emptyList()
            },
            Modifier.padding(top = 10.dp)
        )
        Column(
            Modifier.padding(horizontal = 5.dp)
        ) {
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
                onChange = {
                    conditions1 = it
                }
            )
        }
        FinalBtn(success = {
            success(
                RealtorCommentInfo(
                    ad,
                    disAd,
                    grade,
                    stateFlow.selectedPost.id,
                    images.toTypedArray()
                )
            )
        })
    }
}

@Composable
fun FinalBtn(text: String = stringResource(R.string.send), success: () -> Unit) {
    Column(
        Modifier
            .imePadding()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefButton(
            text,
            Modifier
                .width(300.dp)
                .padding(bottom = 10.dp)
        ) {
            success()
        }
    }
}

@Composable
fun GradeBlock(
    textId: Int,
    fill: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textSize: Int = 14,
    isError: Boolean = false
) {
    Column(
        modifier
            .border(1.dp, if (isError) Red else Gray, RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefText(
            stringResource(textId),
            color = Black,
            size = textSize,
            align = TextAlign.Center,
            spacing = 10.sp
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefIcon(
                if (fill >= 1) R.drawable.star else R.drawable.empty_star,
                size = 25.dp,
                tint = if (fill >= 1) Orange else Black,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clip(CircleShape)
                    .clickable { onChange(1) },
            )
            DefIcon(
                if (fill >= 2) R.drawable.star else R.drawable.empty_star,
                size = 25.dp,
                tint = if (fill >= 2) Orange else Black,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clip(CircleShape)
                    .clickable { onChange(2) })
            DefIcon(
                if (fill >= 3) R.drawable.star else R.drawable.empty_star,
                size = 25.dp,
                tint = if (fill >= 3) Orange else Black,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clip(CircleShape)
                    .clickable { onChange(3) })
            DefIcon(
                if (fill >= 4) R.drawable.star else R.drawable.empty_star,
                size = 25.dp,
                tint = if (fill >= 4) Orange else Black,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clip(CircleShape)
                    .clickable { onChange(4) })
            DefIcon(
                if (fill >= 5) R.drawable.star else R.drawable.empty_star,
                size = 25.dp,
                tint = if (fill >= 5) Orange else Black,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clip(CircleShape)
                    .clickable { onChange(5) })
        }
    }
}

@Composable
fun PhotoBlock(loaded: (List<Int>) -> Unit, listNull: () -> Unit, modifier: Modifier = Modifier, isError: Boolean = false) {

    val context = LocalContext.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    var isPicked by remember { mutableStateOf(false) }
    var uriList by remember { mutableStateOf(listOf<Uri>()) }
    var height by remember { mutableStateOf(80) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        isPicked = true
        var newList = mutableListOf<Uri>()
        newList.addAll(uriList)
        newList.addAll(uris)

        if (newList.size > 5) {
            newList.forEachIndexed { index, uri ->
                if (index > 4) {
                    val list = newList.toMutableList()
                    list.removeAt(index)
                    newList = list
                }
            }
        }

        uriList = newList

        val files = uriList.map { getFileFromUri(context, it) }

        dataViewModel.onAction(DataAction.UploadImage(files, loginViewModel.state.token, {
            loaded(it)
        }))
    }
    if (uriList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier
                .animateContentSize()
                .padding(top = 20.dp)
                .height(height.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uriList.size) {
                LaunchedEffect(Unit) {
                    if ((it) % 2 == 0) height += 80
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .crossfade(true)
                            .data(uriList[it])
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .build(),
                        contentDescription = Const.ImageDescription.name,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        Modifier
                            .clickable {
                                val list = uriList.toMutableList()
                                list.removeAt(it)
                                uriList = list
                                height = 80
                                list.forEachIndexed { index, _ ->
                                    if (index % 2 == 0) height += 80
                                }
                                if (list.isEmpty()) {
                                    listNull()
                                }
//                                loaded(list)
//                                val files = uriList.map { getFileFromUri(context, it) }

//                                dataViewModel.onAction(
//                                    DataAction.UploadImage(
//                                        files,
//                                        loginViewModel.state.token,
//                                        {
//                                            loaded(it)
//                                        })
//                                )
                            }
                            .clip(CircleShape)
                            .size(40.dp)
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        DefIcon(R.drawable.trash, tint = Red, size = 25.dp)
                    }
                }
            }
            item {
                if (uriList.size in 0..4) {
                    Box(
                        Modifier
                            .background(LightGray, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .aspectRatio(16f / 9f)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier.background(White, CircleShape).size(50.dp),
                            contentAlignment = Alignment.Center
                        ){
                            DefText("+", size = 24)
                        }
                    }
                }
            }

        }
    } else {
        Row(
            Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DefText(
                "+",
                size = 20,
                color = if (isError) Red else Blue,
            )
            DefText(
                "Добавить фотографии (не более 5)",
                size = 14,
                color = if (isError) Red else Blue,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    }
            )
        }
    }
}

@Composable
fun CommentBlock1(
    title: String,
    label: String,
    text: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Box(
        modifier.height(110.dp)
    ) {
        Column(
            Modifier
                .border(1.dp, if (isError) Red else Gray, RoundedCornerShape(5.dp))
                .background(color = White, shape = RoundedCornerShape(5.dp))

                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp)
        ) {
            DefText(title, color = Black, size = 14)
            DefBasicTextField(
                text,
                label,
                textSize = 14,
                onValueChange = onChange,
                prefixPadding = 0,
                startPadding = 0,
                singleLine = false
            )
        }
    }
}

@Composable
private fun SuccessStatus(
    toMain: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefImage(R.drawable.check, Modifier.size(100.dp))
        DefText(stringResource(R.string.createFeedbackSuccess), weight = FontWeight.Bold)
        DefButton(
            stringResource(R.string.toMain),
            modifier = Modifier.padding(top = 100.dp)
        ) { toMain() }
    }
}