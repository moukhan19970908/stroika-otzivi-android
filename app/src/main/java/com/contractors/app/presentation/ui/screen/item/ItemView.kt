package com.contractors.app.presentation.ui.screen.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.OwnComment
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.calculateDistanceYandex
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.domain.utils.fullTrim
import com.contractors.app.domain.utils.openYandexMaps
import com.contractors.app.presentation.ui.elements.RequestLocationPermission
import com.contractors.app.presentation.ui.elements.components.DefButton
import java.time.Instant

@Composable
fun ItemView() {
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    RequestLocationPermission()

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(R.string.objectPage) {
                navController.popBackStack()
            }
            Body(

                readComment = {
                    dataViewModel.onAction(DataAction.SetComment(it))
                    navController.navigate(Screen.Comment.name)
                }
            )

        }
    }
}

@Composable
private fun Body(readComment: (OwnComment) -> Unit) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val item = dataViewModel.state.selectedPost
    val loginViewModel = LocalLoginViewModel.current
    val list = item.master_comments + item.rieltor_comments
    Box {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var onFavoriteIconClicked by remember { mutableStateOf(false) }
            Column(
                Modifier.padding(horizontal = 16.dp)
            ) {
                if (item.images.isNotEmpty()) {
                    LazyRow(
                        Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .height(220.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(item.images.size) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .crossfade(true)
                                    .data("$BASE_URL${item.images[it].image_path}")
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .memoryCachePolicy(CachePolicy.DISABLED)
                                    .build(),
                                contentDescription = Const.ImageDescription.name,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .fillParentMaxSize()
                                    .aspectRatio(6f / 4f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Column {
                    Box(
                        Modifier.fillMaxWidth()
                    ) {
                        Column {
                            DefText(
                                "${stringResource(R.string.dateFrom)} ${formatDate(item.created_at)}",
                                size = 10,
                                color = Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                            ) {
                                DefImage(imageId = R.drawable.star, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(4.dp))
                                DefText(
                                    item.rating,
                                    color = Black,
                                    size = 16,
                                )
                                Spacer(Modifier.weight(1f))
                                DefIcon(
                                    imageId = if (onFavoriteIconClicked) R.drawable.icon_favorite_filled else R.drawable.icon_favorite,
                                    tint = Color(0xFF565776),
                                    size = 24.dp,
                                    modifier = Modifier
                                        .clickable {
                                            if (onFavoriteIconClicked) {
                                                dataViewModel.onAction(
                                                    DataAction.RemoveFavoritePost(
                                                        item
                                                    )
                                                )
                                            } else {
                                                dataViewModel.onAction(DataAction.AddPostToFavorite(item))
                                            }
                                            onFavoriteIconClicked = !onFavoriteIconClicked
                                        }
                                        .padding(8.dp)
                                )

                            }

                            DefText(
                                text = item.title,
                                size = 24,
                                color = Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable {
                                        try {
                                            openYandexMaps(
                                                item.longitude
                                                    .fullTrim()
                                                    .toDouble(),
                                                item.latitude
                                                    .fullTrim()
                                                    .toDouble(),
                                                context
                                            )
                                        } catch (_: Exception) {
                                        }
                                    }
                                    .fillMaxWidth()
                            ) {
                                DefImage(imageId = R.drawable.adress, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(8.dp))
                                DefText(
                                    text = item.address,
                                    color = Gray,
                                    size = 14,
                                )
                            }
                        }
                    }
                    Spacer(
                        Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(LightGray, CircleShape)
                    )

                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        Modifier.animateContentSize()
                    ) {
                        DefText(
                            stringResource(R.string.description),
                            size = 16,
                            color = Black,
                            weight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        DefText(
                            text = item.description,
                            size = 14,
                            color = Black,
                            modifier = Modifier.padding(top = 5.dp),
                            maxLength = if (expanded) 9999 else 137,
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (item.description.length > 120) {
                                DefText(
                                    stringResource(if (expanded) R.string.collapse else R.string.readNext),
                                    size = 13,
                                    weight = FontWeight.Bold,
                                    color = Blue,
                                    modifier = Modifier.clickable { expanded = !expanded }
                                )
                            }
                        }
                        Spacer(
                            modifier= Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(LightGray, CircleShape)
                        )
                        Row(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DefText(
                                text = "Отзывы",
                                size = 16,
                                color = Black,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                            DefText(
                                stringResource(R.string.showAllComments),
                                size = 12,
                                weight = FontWeight.Bold,
                                textDecoration = TextDecoration.None,
                                color = Blue,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .clickable {
                                        dataViewModel.onAction(DataAction.SetCommentList(list))
                                        navController.navigate(Screen.CommentList.name)
                                    }
                            )
                        }
                    }
                }
            }

            CommentList(
                commentList = list.sortedBy { Instant.parse(it.created_at).toEpochMilli() }
                    .reversed(),
                readMore = readComment
            ) {
                dataViewModel.onAction(DataAction.SetProfileById(it))
                navController.navigate(Screen.UserProfile.name)
            }
            Spacer(Modifier.height(48.dp))

        }

        if (loginViewModel.state.token.isNotEmpty() && loginViewModel.state.userInfo.role != Role.Customer) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 26.dp)
            ) {
                DefButton(
                    text = "Добавить отзыв",
                    onClick = { navController.navigate(Screen.NewComment.name) }
                )
            }

        }

    }
}

