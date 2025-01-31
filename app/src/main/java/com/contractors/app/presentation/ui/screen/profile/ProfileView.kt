package com.contractors.app.presentation.ui.screen.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.Image
import com.contractors.app.data.network.UpdateProfileInfo
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.calculateDistanceYandex
import com.contractors.app.domain.utils.formatPhoneNumber
import com.contractors.app.domain.utils.getFileFromUri
import com.contractors.app.domain.utils.isValidEmail
import com.contractors.app.domain.utils.isValidPhone
import com.contractors.app.presentation.ui.elements.PassBasicTextField
import com.contractors.app.presentation.ui.elements.components.DefBasicTextField
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefCardPostType1
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.model.toPost
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.changeData.ChangeDataType
import com.contractors.app.presentation.ui.screen.login.LoginAction
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.elements.Footer
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.Dark
import com.contractors.app.presentation.ui.theme.DarkBlue
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.SecondaryDark
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White
import kotlinx.coroutines.delay

enum class EditType {
    Name,
    Email,
    Passport,
    Number
}

@Composable
fun ProfileView() {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val toastHelper = LocalToastHelper.current

    val editType by remember { mutableStateOf(EditType.Name) }
    var isEdit by remember { mutableStateOf(false) }
    var isExit by remember { mutableStateOf(false) }
    var newPhotoUri by remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(Unit) {
        loginViewModel.onAction(LoginAction.GetProfile())
        Log.e("APP ST", loginViewModel.state.toString())
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            newPhotoUri = uri
            val file = getFileFromUri(context, uri)
            loginViewModel.onAction(LoginAction.UploadAvatar(file))
        }
    }



    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            HeaderText(stringResource(R.string.youProfile))
            Body(
                newPhotoUri,
                changePhoto = {
                    imagePickerLauncher.launch("image/*")
                },
                logout = {
                    isExit = true
                }
            )
        }
        Footer(
            startId = 2,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
        AnimatedVisibility(
            isEdit,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(100))
        ) {
            if (editType == EditType.Passport) {
                EditPopupPass(
                    success = { old, new ->
                        loginViewModel.onAction(
                            LoginAction.ChangePass(
                                old,
                                new
                            ) { isSuccess, error ->
                                if (!isSuccess)
                                    toastHelper.show(error)
                            })
                        isEdit = false
                    },
                    close = {
                        isEdit = false
                    }
                )
            } else {
                EditPopup(
                    editType = editType,
                    success = {
                        loginViewModel.onAction(
                            LoginAction.UpdateUser(
                                UpdateProfileInfo(
                                    if (editType == EditType.Name) it else loginViewModel.state.userInfo.name,
                                    if (editType == EditType.Number) "7${it}" else loginViewModel.state.userInfo.number,
                                    if (editType == EditType.Email) it else loginViewModel.state.userInfo.email,
                                    loginViewModel.state.userInfo.userTypeId
                                )
                            )
                        )
                        isEdit = false
                    },
                    close = {
                        isEdit = false
                    }
                )
            }
        }
        AnimatedVisibility(
            isExit,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(100))
        ) {
            LogoutPopup(
                success = {
                    loginViewModel.onAction(LoginAction.Logout)
                    navController.popBackStack()
                    isExit = false
                },
                cancel = {
                    isExit = false
                }
            )
        }
    }
}

@Composable
fun EditPopupPass(success: (String, String) -> Unit, close: () -> Unit) {
    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current
    var pass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var repPass by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(Dark)
            .clickable { close() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .width(320.dp)
                .background(White, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            DefText(stringResource(R.string.changePass), color = Black)
            DefText(stringResource(R.string.pass), modifier = Modifier.padding(top = 10.dp))
            PassBasicTextField(pass, onValueChange = { pass = it })
            DefText(stringResource(R.string.newPass), modifier = Modifier.padding(top = 10.dp))
            PassBasicTextField(newPass, onValueChange = { newPass = it })
            DefText(
                stringResource(R.string.repeatNewPass),
                modifier = Modifier.padding(top = 10.dp)
            )
            PassBasicTextField(repPass, onValueChange = { repPass = it })
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileBtn(R.string.changePassBtn) {
                    if (pass.isEmpty()) {
                        toastHelper.show(context.getString(R.string.passEmpty))
                        return@ProfileBtn
                    }
                    if (newPass.isEmpty() || repPass.isEmpty() && newPass != repPass) {
                        toastHelper.show(context.getString(R.string.passDontRepeat))
                        return@ProfileBtn
                    }

                    success(pass, newPass)
                }
            }
        }
    }
}

@Composable
fun LogoutPopup(success: () -> Unit, cancel: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Dark)
            .clickable { cancel() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .width(290.dp)
                .background(White, RoundedCornerShape(20.dp))
                .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefText(
                stringResource(R.string.exitRequest),
                size = 12,
                color = Black,
                modifier = Modifier.padding(top = 10.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefText(
                    stringResource(R.string.cancel),
                    color = Black,
                    size = 12,
                    modifier = Modifier.clickable {
                        cancel()
                    })
                DefText(stringResource(R.string.logout),
                    color = Blue,
                    size = 12,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            success()
                        })
            }
        }
    }
}

@Composable
fun EditPopup(editType: EditType, success: (String) -> Unit, close: () -> Unit) {

    val context = LocalContext.current
    val toastHelper = LocalToastHelper.current

    val titleId = when (editType) {
        EditType.Name -> R.string.changeName
        EditType.Email -> R.string.changeEmail
        EditType.Passport -> R.string.changeEmail
        EditType.Number -> R.string.changeNumber
    }
    val hintId = when (editType) {
        EditType.Name -> R.string.enterName
        EditType.Email -> R.string.enterEmail
        EditType.Passport -> R.string.changeEmail
        EditType.Number -> R.string.enterNumber
    }
    val btnId = when (editType) {
        EditType.Name -> R.string.setName
        EditType.Email -> R.string.setNewEmail
        EditType.Passport -> R.string.changeEmail
        EditType.Number -> R.string.setNewNumber
    }
    var text by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(Dark)
            .clickable { close() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .width(320.dp)
                .background(White, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            DefText(stringResource(titleId), color = Black)
            DefText(stringResource(hintId), modifier = Modifier.padding(top = 10.dp))
            DefBasicTextField(
                text,
                onValueChange = {
                    if (editType == EditType.Number) {
                        if (it.length < 11) {
                            text = it
                        }
                    } else {
                        text = it
                    }
                },
                isPhone = editType == EditType.Number,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Gray, RoundedCornerShape(10.dp))
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileBtn(btnId) {
                    when (editType) {
                        EditType.Name -> {
                            if (text.isEmpty()) {
                                toastHelper.show(context.getString(R.string.fioError))
                                return@ProfileBtn
                            }
                        }

                        EditType.Email -> {
                            if (!text.isValidEmail()) {
                                toastHelper.show(context.getString(R.string.emailError2))
                                return@ProfileBtn
                            }
                        }

                        EditType.Passport -> {}
                        EditType.Number -> {
                            if (!text.isValidPhone()) {
                                toastHelper.show(context.getString(R.string.phoneError2))
                                return@ProfileBtn
                            }
                        }
                    }

                    success(text)
                }
            }
        }
    }
}

@Composable
private fun Body(
    newPhotoUri: Uri,
//    openEdit: (EditType, String) -> Unit,
    changePhoto: () -> Unit,
    logout: () -> Unit,
) {
    val context = LocalContext.current
    val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetFavoriteFourPosts)
        dataViewModel.onAction(DataAction.GetOwnPosts(loginViewModel.state.token))
        loginViewModel.onAction(LoginAction.GetProfile())

        Log.e("APPPPP", loginViewModel.state.userInfo.toString())
    }

    val favoritePostList: List<Post> = stateFlow.favoritePosts

    val ownPost = stateFlow.ownPosts.data.mapIndexed { index, item ->
            if (stateFlow.favoritePosts.any { dbList -> dbList.id == item.id }) {
                item.toPost(id = index, isFavorite = true)
            } else {
                item.toPost(id = index, isFavorite = false)
            }
        }

    val navController = LocalNavController.current
    val user = loginViewModel.state.userInfo
    var isFullProfileShown by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    start = 16.dp, end = 16.dp
                )
        ) {
            Column {
                DefText(
                    text = user.name + " " + user.lastname + " " + (user.surname ?: ""),
                    size = 22,
                    color = Black
                )
                Spacer(Modifier.height(4.dp))
                if (user.role == Role.Master) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DefImage(
                            imageId = R.drawable.icon_diamond3d,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )

                        DefText("0", size = 14, spacing = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        DefText(
                            text = stringResource(roleToStrId(user.role)),
                            size = 14,
                            color = Black,
                            align = TextAlign.End
                        )
                    }
                } else {
                    DefText(
                        text = stringResource(roleToStrId(user.role)),
                        size = 14,
                        color = Black,
                        align = TextAlign.End
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Box {
                if (newPhotoUri == Uri.EMPTY && !user.imageUrl.contains("null")) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .crossfade(true)
                            .data(if (newPhotoUri != Uri.EMPTY) newPhotoUri else user.imageUrl)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .build(),
                        contentDescription = Const.ImageDescription.name,
                        modifier = Modifier
                            .clickable {
                                changePhoto()
                            }
                            .padding(top = 20.dp)
                            .clip(CircleShape)
                            .size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier
                            .clickable {
                                changePhoto()
                            }
                            .padding(start = 12.dp)
                            .size(80.dp)
                            .background(LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        DefImage(
                            R.drawable.no_photo,
                            Modifier
                                .size(50.dp)
                        )
                    }
                }

            }
        }

        if (!isFullProfileShown && user.role != Role.Realtor) {
            val interactionSource = remember { MutableInteractionSource() }
            DefText(
                text = "Показать данные профиля",
                size = 14,
                color = Blue,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            isFullProfileShown = !isFullProfileShown
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)

            )
        }
        if (user.role == Role.Realtor) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp, end = 16.dp
                    )
            ) {
                ProfileRow(
                    "Электронная почта",
                    user.email,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    true
                ) {
                    dataViewModel.onAction(DataAction.SetDataType(ChangeDataType.Email))
                    navController.navigate(Screen.ChangeData.name)
                }
                ProfileRow(
                    "Номер телефона",
                    formatPhoneNumber(user.number),
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    true
                ) {
                    dataViewModel.onAction(DataAction.SetDataType(ChangeDataType.Number))
                    navController.navigate(Screen.ChangeData.name)
                }
                Spacer(Modifier.height(4.dp))
            }

        }
        AnimatedVisibility(isFullProfileShown) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp, end = 16.dp
                    )
            ) {
                if (user.role == Role.Master) {
                    ProfileRow(
                        title = "Специальность",
                        text = user.specialist,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        isEdit = false
                    )
                    ProfileRow(
                        title = "Опыт работы",
                        text = user.experience,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        isEdit = false
                    )
                }
                ProfileRow(
                    "Электронная почта",
                    user.email,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    true
                ) {
                    dataViewModel.onAction(DataAction.SetDataType(ChangeDataType.Email))
                    navController.navigate(Screen.ChangeData.name)
                }
                ProfileRow(
                    "Номер телефона",
                    formatPhoneNumber(user.number),
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    true
                ) {
                    dataViewModel.onAction(DataAction.SetDataType(ChangeDataType.Number))
                    navController.navigate(Screen.ChangeData.name)
                }
                Spacer(Modifier.height(4.dp))
                val interactionSource = remember { MutableInteractionSource() }
                DefText(
                    text = "Свернуть",
                    size = 14,
                    color = Blue,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                isFullProfileShown = !isFullProfileShown
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }

        if (user.role == Role.Master) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                DefText(
                    text = "Мои объекты",
                    size = 24,
                    color = Black,
                    modifier = Modifier
                )
                Spacer(Modifier.weight(1f))
                val interactionSource = remember { MutableInteractionSource() }
                if (ownPost.isNotEmpty()) {
                    DefText(
                        text = "Показать все",
                        size = 14,
                        color = Blue,
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    navController.navigate(Screen.MyObjects.name)

                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )

                }
            }

        }
        if (user.role == Role.Master && ownPost.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)

            ) {
                DefCardPostType1(
                    item = ownPost[0],
                    distance = calculateDistanceYandex(ownPost.first().distance),
                    onCardClicked = { postItem ->
                        dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                        navController.navigate(Screen.Item.name)
                    },
                    onFavoriteIconUnfilledClicked = {
                        dataViewModel.onAction(DataAction.AddPostToFavorite(ownPost[0]))
                    },
                    onFavoriteIconFilledClicked = {
                        dataViewModel.onAction(DataAction.RemoveFavoritePost(ownPost[0]))
                    },
                    modifier = Modifier.weight(1f)
                )

                if (ownPost.getOrNull(1) != null) {
                    Spacer(Modifier.width(12.dp))
                    DefCardPostType1(
                        item = ownPost[1],
                        distance = "",
                        onCardClicked = { postItem ->
                            dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                            navController.navigate(Screen.Item.name)
                        },
                        onFavoriteIconUnfilledClicked = {
                            dataViewModel.onAction(DataAction.AddPostToFavorite(ownPost[1]))
                        },
                        onFavoriteIconFilledClicked = {
                            dataViewModel.onAction(DataAction.RemoveFavoritePost(ownPost[1]))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }


        }
        if (user.role == Role.Master && ownPost.isEmpty()) {
            DefText(
                text = "Вы ещё не добавили ни одного объекта",
                size = 16,
                color = Black,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)

            )

        }
        if (user.role == Role.Master) {
            DefButton(
                text = "Добавить объект",
                onClick = { navController.navigate(Screen.CreatePost.name) },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .align(Alignment.CenterHorizontally),
            )

        }


        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            DefText(
                text = "Избранное",
                size = 24,
                color = Black,
                modifier = Modifier
            )
            Spacer(Modifier.weight(1f))
            val interactionSource = remember { MutableInteractionSource() }
            if (favoritePostList.isNotEmpty()) {
                DefText(
                    text = "Показать все",
                    size = 14,
                    color = Blue,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {

                                navController.navigate(Screen.Favorites.name)


                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }


        if (favoritePostList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)

            ) {
                DefCardPostType1(
                    item = favoritePostList[0],
                    distance = "",
                    onCardClicked = { postItem ->
                        dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                        navController.navigate(Screen.Item.name)
                    },
                    onFavoriteIconUnfilledClicked = {
                        dataViewModel.onAction(DataAction.AddPostToFavorite(favoritePostList[0]))
                    },
                    onFavoriteIconFilledClicked = {
                        dataViewModel.onAction(DataAction.RemoveFavoritePost(favoritePostList[0]))
                    },
                    modifier = Modifier.weight(1f)
                )

                if (favoritePostList.getOrNull(1) != null) {
                    Spacer(Modifier.width(12.dp))
                    DefCardPostType1(
                        item = favoritePostList[1],
                        distance = "",
                        onCardClicked = { postItem ->
                            dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                            navController.navigate(Screen.Item.name)
                        },

                        onFavoriteIconUnfilledClicked = {
                            dataViewModel.onAction(DataAction.AddPostToFavorite(favoritePostList[1]))
                        },
                        onFavoriteIconFilledClicked = {
                            dataViewModel.onAction(DataAction.RemoveFavoritePost(favoritePostList[1]))
                        },
                        modifier = Modifier.weight(1f)
                    )

                }

            }
        } else {
            DefText(
                text = "Вы ещё не добавили ни одного объекта в избранное",
                size = 16,
                color = Black,
                modifier = Modifier
                    .padding(horizontal = 16.dp)

            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            DefText(
                "Написать администратору",
                color = Blue,
                textDecoration = TextDecoration.None,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Support.name)
                    }
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            )
            Spacer(Modifier.weight(1f))
            DefText(
                "Выйти",
                color = Blue,
                textDecoration = TextDecoration.None,
                modifier = Modifier
                    .clickable {
                        logout()
                    }
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            )

        }


        Spacer(Modifier.height(100.dp))

    }
}

@Composable
private fun HeaderCategory(
    interactionSource: MutableInteractionSource,
    navController: NavController
) {
}

@Composable
private fun RowWithArrow(click: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable {
                click()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            DefText(
                stringResource(R.string.changePass),
                size = 13,
                modifier = Modifier.padding(top = 5.dp),
                color = Black
            )
        }
        DefIcon(R.drawable.arrow_left, tint = VeryDarkGray, size = 12.dp)
    }
}

@Composable
fun ProfileBtn(
    textId: Int,
    click: () -> Unit
) {
    Box(
        Modifier
            .clickable { click() }
            .height(40.dp)
            .width(160.dp)
            .background(DarkBlue, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        DefText(
            stringResource(textId),
            size = 12,
            color = White,
            weight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileRow(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            DefText(title, size = 12, color = SecondaryDark)
            DefText(text, size = 16, modifier = Modifier.padding(top = 5.dp), color = Black)
        }
        if (isEdit)
            DefIcon(R.drawable.edit, tint = DarkGray, size = 18.dp, modifier = Modifier

                .padding(top = 20.dp)
                .clickable { onClick() }
            )
    }

}

fun roleToStrId(role: Role): Int {
    return when (role) {
        Role.Master -> R.string.master
        Role.Realtor -> R.string.realtor
        Role.Customer -> R.string.customer
    }
}
