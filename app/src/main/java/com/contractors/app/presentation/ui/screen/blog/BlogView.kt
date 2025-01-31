package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.Blog
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.LocalToastHelper
import com.contractors.app.presentation.ui.screen.login.LoginAction
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.elements.Footer
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.presentation.ui.elements.HeaderText
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.theme.LightGray

enum class BlogForm {
    List,
    Article,
    Comments,
    AddComment,

}

@Composable
fun BlogView() {
    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val blogNavController = rememberNavController()
    var selectedBlog by remember { mutableStateOf(Blog()) }
    val dataViewModel = LocalDataViewModel.current

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetBlogs(loginViewModel.state.token))
        loginViewModel.onAction(LoginAction.GetProfile())
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            val currentDest = blogNavController.currentBackStackEntryAsState().value?.destination?.route
            when (currentDest) {
                BlogForm.List.name -> {
                    HeaderText(stringResource(R.string.blog))
                }
                BlogForm.Article.name -> {
                    Header(R.string.article) {
                        blogNavController.navigate(BlogForm.List.name)
                    }
                }
                BlogForm.AddComment.name -> {
                    Header(R.string.add_comment) {
                        blogNavController.navigate(BlogForm.Article.name)
                    }
                }
                BlogForm.Comments.name -> {
                    Header(R.string.comments) {
                        blogNavController.navigate(BlogForm.Article.name)
                    }
                }
            }

            Body(
                navHostController = blogNavController,
                selectedBlog = selectedBlog,
                openBlogItem = {
                    selectedBlog = it
                    blogNavController.navigate(BlogForm.Article.name)
                })
        }
        Footer(
            startId = 3,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun Body(
    navHostController: NavHostController,
    selectedBlog: Blog,
    openBlogItem: (Blog) -> Unit
) {val loginViewModel = LocalLoginViewModel.current
    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val blog = stateFlow.blog
    val toastHelper = LocalToastHelper.current
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navHostController,
        startDestination = BlogForm.List.name
    ) {
        composable(route = BlogForm.List.name) { BlogsList(openBlogItem) }
        composable(route = BlogForm.Article.name) { OpenBlog(navHostController, selectedBlog) }
        composable(route = BlogForm.Comments.name) {
            CommentColumnListShort(list = blog.comments) { article -> dataViewModel.onAction(DataAction.SetProfileById(article))}
        }
        composable(route = BlogForm.AddComment.name) { AddComment { commentText ->
            dataViewModel.onAction(DataAction.AddCommentToBlog(
                blog.id.toString(),
                commentText,
                loginViewModel.state.token,
                {
                    toastHelper.show("Ваш комментарий будет опубликован после проверки администратором")
                }
            ))
        } }
    }
}

@Composable
private fun OpenBlog(
    navHostController: NavHostController,
    item: Blog
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current
    val toastHelper = LocalToastHelper.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val blog = stateFlow.blog
    val scrollState = rememberScrollState()

    val userInfo = loginViewModel.state.userInfo
    val isCanComment = loginViewModel.state.token.isNotEmpty() && userInfo.role != Role.Realtor
    var showAll by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dataViewModel.onAction(
            DataAction.GetBlogById(
                loginViewModel.state.token,
                item.id.toString()
            )
        )
    }

    Box {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(20.dp),
        ) {
            DefText("Публикация ${formatDate(item.created_at)}", size = 14, color = Gray)
            DefText(blog.title, weight = FontWeight.Normal, size = 24)
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .crossfade(true)
                    .data("$BASE_URL${blog.image}")
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .build(),
                contentDescription = Const.ImageDescription.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            DefText(text = blog.body, size = 14, modifier = Modifier.padding(top = 20.dp))
            Spacer(
                Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LightGray, CircleShape)
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefText(
                    text = "Комментарии",
                    size = 16,
                    color = Black,
                    weight = FontWeight.Medium,
                    modifier = Modifier
                )
                Spacer(Modifier.weight(1f))
                val interactionSource = remember { MutableInteractionSource() }
                DefText(
                    text = "Смотреть все",
                    size = 14,
                    color = Blue,
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                navHostController.navigate(BlogForm.Comments.name)
                            }
                        )
                        .padding()
                )
            }
            CommentRowListShort(list = blog.comments.take(3)) {
                dataViewModel.onAction(DataAction.SetProfileById(it))
            }
            Spacer(Modifier.height(128.dp))
        }
        if (isCanComment) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp)
            ) {
                DefButton(
                    text = "Оставить комментарий",
                    onClick = {
                        navHostController.navigate(BlogForm.AddComment.name)
                    }
                )
            }
        }


    }

}



