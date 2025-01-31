package com.contractors.app.presentation.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.data.network.Image
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.presentation.ui.elements.Footer
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.elements.components.DefBasicTextField
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.elements.list.ColumnPostType1List
import com.contractors.app.presentation.ui.model.SearchParam
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.ColorIcon
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SearchView() {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            HeaderText(
                title = stringResource(R.string.search)
            )
            Body()
        }
        Footer(
            modifier = Modifier.align(Alignment.BottomCenter),
            startId = 1
        )
    }

}

@Composable
private fun Body(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val user = LocalLoginViewModel.current.state
//    При получении объекта приходит Platform type и нужно пересоздавать объект

    val list = dataViewModel.state.searchPosts.data.data.map {
        if (dataViewModel.state.favoritePosts.any { dbList -> dbList.id == it.id }) {
            it.copy(
                id = it.id,
                title = it.title,
                description = it.description,
                user_id = it.user_id,
                latitude = it.latitude,
                longitude = it.longitude,
                status = it.status,
                address = it.address,
                rating = it.rating,
                created_at = it.created_at,
                updated_at = it.updated_at,
                get_first_image = it.get_first_image ?: Image(),
                images = it.images ?: emptyList(),
                master_comments = it.master_comments ?: emptyList(),
                rieltor_comments = it.rieltor_comments ?: emptyList(),
                distance = it.distance,
                isFavorite = true
            )
        } else {
            it.copy(
                id = it.id,
                title = it.title,
                description = it.description,
                user_id = it.user_id,
                latitude = it.latitude,
                longitude = it.longitude,
                status = it.status,
                address = it.address,
                rating = it.rating,
                created_at = it.created_at,
                updated_at = it.updated_at,
                get_first_image = it.get_first_image ?: Image(),
                images = it.images ?: emptyList(),
                master_comments = it.master_comments ?: emptyList(),
                rieltor_comments = it.rieltor_comments ?: emptyList(),
                distance = it.distance,
                isFavorite = false
            )
        }

    }

    var searchText by remember { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isDropdownMenuShown by rememberSaveable { mutableStateOf(false) }
    var isFirstInit by remember { mutableStateOf(true) }
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    LaunchedEffect(searchText) {
        delay(300)
        if (searchText.isNotBlank()) {
            val param = SearchParam(
                text = searchText,
                title = "",
                subTitle = context.getString(R.string.found),
                clickItem = { post ->
                    dataViewModel.onAction(DataAction.SetPost(post.toPostDTO()))
                    navController.navigate(Screen.Item.name)
                },
                back = {
                    navController.popBackStack()
                },
                returnId = "0"
            )
            dataViewModel.onAction(DataAction.SetSearchParam(param))
            isDropdownMenuShown = true
        }
        delay(100)
        isDropdownMenuExpanded = true
        isLoading = false

        dataViewModel.onAction(DataAction.GetFavoriteAllPosts)
        println(dataViewModel.state.favoritePosts)
    }
    if(interactionSource.collectIsFocusedAsState().value) {
        isFirstInit = false
    }
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column {
            Row(
                Modifier
                    .padding(top = 24.dp)
            ) {
                DefBasicTextField(
                    interactionSource = interactionSource,
                    text = searchText,
                    label = stringResource(R.string.enterAddress),
                    suffix = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = ColorIcon,
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            searchText
                            DefIcon(
                                imageId = R.drawable.icon_map,
                                tint = Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    },
                    prefix = {
                        DefIcon(
                            imageId = R.drawable.search,
                            tint = Gray,
                            modifier = Modifier.size(28.dp)
                        )

                    },
                    onValueChange = {
                        searchText = it
                        isLoading = true
                    },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(28.dp)
                        )

                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CircleShape)

                )
            }
            if (!isFirstInit && list.isEmpty()) {
                DefText(
                    text = "Объект с таким адресом не найден",
                    size = 16,
                    color = Black,
                    modifier = Modifier
                        .padding(top = 21.dp)
                )
                if (user.userInfo.role == Role.Master) {
                    DefButton(
                        text = "Добавить объект",
                        onClick = { navController.navigate(Screen.CreatePost.name) },
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }

            } else if (isLoading && list.isEmpty()) {
                DefText(
                    text = "Загрузка...",
                    size = 16,
                    color = Black,
                    modifier = Modifier
                        .padding(top = 21.dp)
                )
            } else if (list.isNotEmpty()) {
                DefText(
                    text = "Результат поиск",
                    size = 21,
                    color = Black,
                    modifier = Modifier
                        .padding(top = 21.dp)
                )

                ColumnPostType1List(
                    title = "",
                    list = list,
                    clickItem = { postItem ->
                        dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                        navController.navigate(Screen.Item.name)
                    },
                    onFavoriteIconUnfilledClicked = { id ->
                        val item = list.find { it.id == id }
                        item?.let { dataViewModel.onAction(DataAction.AddPostToFavorite(it)) }
                    },
                    onFavoriteIconFilledClicked = { id ->
                        val item = list.find { it.id == id }
                        item?.let { dataViewModel.onAction(DataAction.RemoveFavoritePost(it)) }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        if (!isFirstInit && list.isNotEmpty()) {
            val interactionSource = remember { MutableInteractionSource() }
            Column(
                modifier = Modifier
                    .padding(top = 75.dp)
                    .fillMaxWidth()
                    .background(color = White, shape = RoundedCornerShape(28.dp))
                    .border(
                        color = Color.LightGray,
                        width = 1.dp,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                list.filter { it.title != searchText }.take(3).forEach { post ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    searchText = post.title
                                },
                                indication = null,
                                interactionSource = interactionSource
                            )
                    ) {
                        DefText(
                            text = post.title,
                            size = 16,
                            color = Black,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
//            Box {
//                DropdownMenu(
//                    expanded = isDropdownMenuExpanded,
//                    onDismissRequest = {
//                        isDropdownMenuExpanded = false
//                    },
//                    shape = RoundedCornerShape(28.dp),
//                    containerColor = White,
//
//                    ) {
//
//
//                }
//
//            }

        }


    }

}
