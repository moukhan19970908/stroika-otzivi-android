package com.contractors.app.presentation.ui.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.extentions.backgroundImage
import com.contractors.app.presentation.ui.popup.Popups
import com.contractors.app.presentation.ui.screen.SystemViewModel
import com.contractors.app.presentation.ui.screen.blog.BlogView
import com.contractors.app.presentation.ui.screen.changeData.ChangeData
import com.contractors.app.presentation.ui.screen.comment.CommentListView
import com.contractors.app.presentation.ui.screen.comment.CommentView
import com.contractors.app.presentation.ui.screen.comment.NewCommentView
import com.contractors.app.presentation.ui.screen.comment.NewSubCommentView
import com.contractors.app.presentation.ui.screen.createPost.CreatePostView
import com.contractors.app.presentation.ui.screen.item.ItemView
import com.contractors.app.presentation.ui.screen.login.LoginAction
import com.contractors.app.presentation.ui.screen.login.LoginView
import com.contractors.app.presentation.ui.screen.login.LoginViewModel
import com.contractors.app.presentation.ui.screen.main.DataViewModel
import com.contractors.app.presentation.ui.screen.search.SearchView
import com.contractors.app.presentation.ui.screen.profile.OtherProfileView
import com.contractors.app.presentation.ui.screen.reg.RegView
import com.contractors.app.presentation.ui.screen.reg.SuccessReg
import com.contractors.app.presentation.ui.screen.sendCode.SendCodeView
import com.contractors.app.presentation.ui.screen.sendCode.SendCodeViewModel
import com.contractors.app.presentation.ui.screen.support.SupportView
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White
import com.contractors.app.presentation.ui.screen.myObjects.MyObjectView
import com.contractors.app.presentation.ui.screen.profile.ProfileView
import com.contractors.app.domain.utils.ToastHelper
import com.contractors.app.domain.utils.initYandexLocationManager
import com.contractors.app.presentation.ui.screen.favorites.FavoritesView
import com.contractors.app.presentation.ui.screen.loading.LoadingView
import kotlinx.coroutines.delay

enum class Screen {
    //    Main,
    Login,
    Reg,
    Blog,
    Item,
    Comment,
    NewComment,
    Profile,
    UserProfile,
    CreatePost,
    SendCode,
    SuccessReg,
    CommentList,
    Support,
    ChangeData,
    MyObjects,
    SubComment,
    Search,
    Favorites,
    Loading,
}

val LocalNavController =
    staticCompositionLocalOf<NavController> { error("CompositionLocal NavController not present") }
val LocalToastHelper =
    staticCompositionLocalOf<ToastHelper> { error("CompositionLocal ToastHelper not present") }
val LocalLoginViewModel =
    staticCompositionLocalOf<LoginViewModel> { error("CompositionLocal LoginViewModel not present") }
val LocalDataViewModel =
    staticCompositionLocalOf<DataViewModel> { error("CompositionLocal DataViewModel not present") }
val LocalSendCodeViewModel =
    staticCompositionLocalOf<SendCodeViewModel> { error("CompositionLocal SendCodeViewModel not present") }

@Composable
fun Navigation() {
    val systemViewModel: SystemViewModel = hiltViewModel()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalToastHelper provides ToastHelper(snackbarHostState),
        LocalLoginViewModel provides hiltViewModel(),
        LocalDataViewModel provides hiltViewModel(),
        LocalSendCodeViewModel provides hiltViewModel(),
    ) {

        val loginViewModel = LocalLoginViewModel.current
        var isAppLoaded by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            loginViewModel.onAction(LoginAction.IsAuth {
                if (it) {
                    loginViewModel.onAction(LoginAction.GetProfile())
                }
            })

            initYandexLocationManager {
                loginViewModel.onAction(LoginAction.SetPoint(it))
            }
            delay(1_000)
            isAppLoaded = true
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.padding(WindowInsets.ime.asPaddingValues())
                ) { snackbarData ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = 80.dp, start = 30.dp, end = 30.dp)
                            .background(
                                VeryDarkGray,
                                RoundedCornerShape(10.dp)
                            )
                            .wrapContentWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    ) {
                        DefText(
                            snackbarData.visuals.message,
                            color = White,
                            align = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            },

            ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .backgroundImage(
                        painter = painterResource(R.drawable.new_background),
                        contentScale = ContentScale.FillBounds
                    )
            ) {
                NavHost(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    navController = navController,
                    startDestination = if (!isAppLoaded) Screen.Loading.name else if (loginViewModel.state.token.isNotEmpty()) Screen.Profile.name else Screen.Reg.name
                ) {
//                    composable(route = Screen.Main.name) { MainView() }
                    composable(route = Screen.Login.name) { LoginView() }
                    composable(route = Screen.Reg.name) { RegView() }
                    composable(route = Screen.Blog.name) { BlogView() }
                    composable(route = Screen.Item.name) { ItemView() }
                    composable(route = Screen.Comment.name) { CommentView() }
                    composable(route = Screen.NewComment.name) { NewCommentView() }
                    composable(route = Screen.Profile.name) { ProfileView() }
                    composable(route = Screen.UserProfile.name) { OtherProfileView() }
                    composable(route = Screen.CreatePost.name) { CreatePostView() }
                    composable(route = Screen.SendCode.name) { SendCodeView() }
                    composable(route = Screen.SuccessReg.name) { SuccessReg() }
                    composable(route = Screen.CommentList.name) { CommentListView() }
                    composable(route = Screen.Support.name) { SupportView() }
                    composable(route = Screen.ChangeData.name) { ChangeData() }
                    composable(route = Screen.MyObjects.name) { MyObjectView() }
                    composable(route = Screen.SubComment.name) { NewSubCommentView() }
                    composable(route = Screen.Search.name) { SearchView() }
                    composable(route = Screen.Favorites.name) { FavoritesView() }
                    composable(route = Screen.Loading.name) { LoadingView() }
                }
            }
            Popups(systemViewModel.state)
        }
    }
}
