package com.contractors.app.presentation.ui.screen.myObjects

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contractors.app.R
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.model.toPost
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.elements.Footer
import com.contractors.app.presentation.ui.elements.list.GridPostType1List as GridPostType1List1

@Composable
fun MyObjectView() {
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetOwnPosts(loginViewModel.state.token))
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(R.string.my_objects) {
                navController.popBackStack()
            }
            Body()
        }
        Footer(
            modifier = Modifier.align(Alignment.BottomCenter),
            startId = 2
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 94.dp)
        ) {
            DefButton(
                text = "Добавить объект",
                onClick = { navController.navigate(Screen.CreatePost.name) }
            )
        }
    }
}

@Composable
private fun Body() {
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()

    val ownPosts = stateFlow.ownPosts.data.mapIndexed { index, item ->
        if (stateFlow.favoritePosts.any { dbList -> dbList.id == item.id }) {
            item.toPost(id = index, isFavorite = true)
        } else {
            item.toPost(id = index, isFavorite = false)
        }
    }.reversed()

    Log.e("APP", ownPosts.toString())
    GridPostType1List1(
        title = "",
        list = ownPosts,
        clickItem = { postItem ->
            dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
            navController.navigate(Screen.Item.name)
        },
        modifier = Modifier.padding(0.dp),
        onFavoriteIconUnfilledClicked = { postItem ->
            dataViewModel.onAction(DataAction.AddPostToFavorite(postItem))
        },
        onFavoriteIconFilledClicked = { postItem ->
            dataViewModel.onAction(DataAction.RemoveFavoritePost(postItem))
        },

    )
}