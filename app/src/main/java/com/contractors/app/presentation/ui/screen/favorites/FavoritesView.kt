package com.contractors.app.presentation.ui.screen.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contractors.app.R
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.presentation.ui.elements.list.GridPostType1List
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.elements.Footer


@Composable
fun FavoritesView() {
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetFavoriteAllPosts)
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(R.string.favorite) {
                navController.popBackStack()
            }
            Body()
        }
        Footer(
            modifier = Modifier.align(Alignment.BottomCenter),
            startId = 2
        )
    }
}

@Composable
private fun Body() {
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val favoritesPost = stateFlow.favoritePosts

    GridPostType1List(
        title = "",
        list = favoritesPost,
        clickItem = { postItem ->
            dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
            navController.navigate(Screen.Item.name)
        },
        onFavoriteIconUnfilledClicked = { postItem ->
            dataViewModel.onAction(DataAction.AddPostToFavorite(postItem))
        },
        onFavoriteIconFilledClicked = { postItem ->
            dataViewModel.onAction(DataAction.RemoveFavoritePost(postItem))
        },

        modifier = Modifier.padding(0.dp)
    )
}