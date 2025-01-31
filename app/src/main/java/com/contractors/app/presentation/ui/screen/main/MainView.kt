package com.contractors.app.presentation.ui.screen.main

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contractors.app.R
import com.contractors.app.data.network.model.toPostDTO
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.elements.list.ColumnPostType2List
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.elements.RequestLocationPermission
import com.contractors.app.presentation.ui.screen.login.LoginAction

enum class MainForm {
    Main,
    Search,
    ShowAll
}

enum class ListType {
    Nearest,
    Interesting,
    Top
}

@Composable
fun MainView() {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val mainNavController = rememberNavController()
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current

    var searchText by remember { mutableStateOf("") }

    RequestLocationPermission()

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetPosts(loginViewModel.state.point))
        loginViewModel.onAction(LoginAction.GetProfile())
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            HeaderText(
                stringResource(R.string.main)
            )
            Body(
                mainNavController,
                openPost = { postItem ->
                    dataViewModel.onAction(DataAction.SetPost(postItem.toPostDTO()))
                    navController.navigate(Screen.Item.name)
                },
                stopSearch = {
                    mainNavController.navigate(MainForm.Main.name)
                },
                showAll = {
                    dataViewModel.onAction(DataAction.SetShowAllType(it))
                    mainNavController.navigate(MainForm.ShowAll.name)
                }
            )
        }
    }
}

@Composable
private fun Body(
    mainNavController: NavHostController,
    openPost: (Post) -> Unit,
    stopSearch: () -> Unit,
    showAll: (ListType) -> Unit,
) {
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val list = stateFlow.posts.data.data
    val topList = stateFlow.topPosts.data.data
    val nearestList = stateFlow.nearestPosts.data.data
    val showAllList = when (stateFlow.listType) {
        ListType.Nearest -> nearestList
        ListType.Interesting -> list
        ListType.Top -> topList
    }

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = mainNavController,
        startDestination = MainForm.Main.name
    ) {
        composable(route = MainForm.Main.name) {
            MainLists(
                list,
                topList,
                nearestList,
                openPost,
                showAll
            )
        }
        composable(route = MainForm.ShowAll.name) {
            ColumnPostType2List(
                stringResource(R.string.objectsNear),
                showAllList,
                openPost,
                Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}


