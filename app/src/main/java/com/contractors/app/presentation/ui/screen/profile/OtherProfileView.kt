package com.contractors.app.presentation.ui.screen.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.OtherComment
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.domain.utils.formatYears
import com.contractors.app.domain.utils.userTypeToRole
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.comment.GradeRow
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.DarkBlue
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.presentation.ui.theme.White
import java.time.Instant

enum class ProfileForm {
    Statistic,
    Feedback,
    NewFeedback
}

@Composable
fun OtherProfileView() {

    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    var form by remember { mutableStateOf(ProfileForm.Statistic) }

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetProfileById(dataViewModel.state.otherUserId))
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(
                if (form == ProfileForm.Feedback) R.string.userFeedback else R.string.userProfile
            ) {
                if (form == ProfileForm.Feedback) form = ProfileForm.Statistic
                else navController.popBackStack()
            }
            Body(
                form,
                openForm = {
                    form = it
                },
            )
        }
    }
}

@Composable
private fun Body(
    feedback: ProfileForm,
    openForm: (ProfileForm) -> Unit,
) {
    val context = LocalContext.current
    val dataViewModel = LocalDataViewModel.current
    val user = dataViewModel.state.otherUser.user

    val role = if (user.user_type_id == 3) Role.Customer else Role.Master

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(DarkBlue)
                .padding(start = 30.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!user.avatar.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data("$BASE_URL${user.avatar}")
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = Const.ImageDescription.name,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                DefImage(R.drawable.def_avatar, modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                )
            }

            Column(
                Modifier.padding(start = 20.dp)
            ) {
                DefText(userTypeToRole(user.user_type_id), size = 12, color = Gray)
                DefText(user.name, size = 14, color = White, weight = FontWeight.Bold)
//                DefText("${stringResource(R.string.rating)} 23", size = 12, color = White)
            }
        }

        AnimatedContent(
            feedback,
            label = "body",
            transitionSpec = {
                if (targetState > initialState) {
                    val contentTransform = (slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) + fadeIn()).togetherWith(slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth }
                    ) + fadeOut())
                    contentTransform
                } else {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth }
                    ) + fadeIn()).togetherWith(slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth }
                    ) + fadeOut())
                }
            }
        ) {
            when(it) {
                ProfileForm.Feedback -> FeedbackList(role)
                ProfileForm.Statistic -> {
                    when (role) {
                        Role.Master -> MasterBody(
                            showAllFeedback = {openForm(ProfileForm.Feedback)},
                            createFeedback = {openForm(ProfileForm.NewFeedback)}
                        )
                        Role.Customer -> CustomerBody(
                            showAllFeedback = {openForm(ProfileForm.Feedback)},
                            createFeedback = {openForm(ProfileForm.NewFeedback)}
                        )
                        else -> MasterBody(
                            showAllFeedback = {openForm(ProfileForm.Feedback)},
                            createFeedback = {openForm(ProfileForm.NewFeedback)}
                        )
                    }
                }
                ProfileForm.NewFeedback -> NewProfileCommentView { openForm(ProfileForm.Statistic) }
            }
        }
    }
}

@Composable
private fun FeedbackList(role: Role) {
    val dataViewModel = LocalDataViewModel.current
    val list = dataViewModel.state.otherCommentList.sortedBy { Instant.parse(it.created_at).toEpochMilli() }.reversed()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(list.size, key = { list[it].id }) {
            if (role == Role.Master)
                CommentItemMaster(list[it])
            else if (role == Role.Customer)
                CommentItemCustomer(list[it])
        }
    }
}

@Composable
fun CommentItemCustomer(commentItem: OtherComment) {

    val context = LocalContext.current
    var avatar by remember { mutableStateOf("") }
    val dataViewModel = LocalDataViewModel.current

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetAvatarById(commentItem.user_id.toString()) {
            if (it != null) avatar = it
        })
    }

    Column(
        Modifier
            .padding(5.dp)
            .fillMaxSize()
            .border(1.dp, LightGray, RoundedCornerShape(10.dp))
            .padding(20.dp)
    ) {
        Row {
            if (avatar.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data("$BASE_URL${avatar}")
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = Const.ImageDescription.name,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                DefImage(R.drawable.def_avatar, modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape))
            }

            Column(
                Modifier.padding(start = 10.dp)
            ) {
                DefText(commentItem.name_client, size = 14, weight = FontWeight.Bold, color = Black)
                DefText(commentItem.role, size = 12, spacing = 10.sp)
            }
        }
        GradeRow(R.string.customerProfile1, commentItem.emotion_rating, Modifier.padding(top = 6.dp))
        GradeRow(R.string.customerProfile2, commentItem.payment_rating)
        GradeRow(R.string.customerProfile3, commentItem.honesty_rating)
        GradeRow(R.string.customerProfile4, commentItem.delivery_rating)
        GradeRow(R.string.customerProfile5, commentItem.quality_rating)
        DefText(commentItem.experience)
    }
}

@Composable
fun CommentItemMaster(commentItem: OtherComment) {
    Column(
        Modifier
            .padding(5.dp)
            .fillMaxSize()
            .border(1.dp, LightGray, RoundedCornerShape(10.dp))
            .padding(20.dp)
    ) {
        Row {
            DefImage(
                R.drawable.commentimg,
                Modifier
                    .size(35.dp)
                    .clip(CircleShape)
            )
            Column(
                Modifier.padding(start = 10.dp)
            ) {
                DefText(commentItem.name_client, size = 14, weight = FontWeight.Bold, color = Black)
                DefText(commentItem.role, size = 12, spacing = 10.sp)
            }
        }
        GradeRow(R.string.masterF1, 4, Modifier.padding(top = 6.dp))
        GradeRow(R.string.masterF2, 4)
        DefText(commentItem.experience)
    }
}

@Composable
private fun MasterBody(
    showAllFeedback: () -> Unit,
    createFeedback: () -> Unit,
) {
    val dataViewModel = LocalDataViewModel.current
    val user = dataViewModel.state.otherUser.user

    Column {
        ProfileRow(
            stringResource(R.string.regDate),
            formatDate(user.created_at),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRow(
            stringResource(R.string.experience),
            formatYears(user.experience),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
//        ProfileRow(
//            stringResource(R.string.objectCreated),
//            "0",
//            isShowArrow = true,
//            onClick = {},
//            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
//        )
//        ProfileRowOneText(
//            stringResource(R.string.showAllFeedback),
//            isShowArrow = true,
//            onClick = showAllFeedback,
//            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
//        )
    }
}

@Composable
private fun CustomerBody(
    showAllFeedback: () -> Unit,
    createFeedback: () -> Unit,
) {
    val dataViewModel = LocalDataViewModel.current
    val user = dataViewModel.state.otherUser.user
    val userResp = dataViewModel.state.otherUser

    LaunchedEffect(Unit) {
        dataViewModel.onAction(DataAction.GetUserComments(user.id))
    }

    Column {
        ProfileRow(
            stringResource(R.string.regDate),
            formatDate(user.created_at),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRow(
            stringResource(R.string.createdCard),
            "25",
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRow(
            stringResource(R.string.customerFeedback),
            userResp.comment_count.toString(),
            isShowArrow = true,
            onClick = showAllFeedback,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowStars(
            stringResource(R.string.customerProfile1),
            userResp.emotion_rating.toFloat().toInt(),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowStars(
            stringResource(R.string.customerProfile2),
            userResp.payment_rating.toFloat().toInt(),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowStars(
            stringResource(R.string.customerProfile3),
            userResp.honesty_rating.toFloat().toInt(),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowStars(
            stringResource(R.string.customerProfile4),
            userResp.delivery_rating.toFloat().toInt(),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowStars(
            stringResource(R.string.customerProfile5),
            userResp.quality_rating.toFloat().toInt(),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
        ProfileRowOneText(
            stringResource(R.string.showAllFeedback),
            isShowArrow = true,
            onClick = showAllFeedback,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
//        ProfileRowOneText(
//            stringResource(R.string.setCustomerFeedback),
//            isShowArrow = true,
//            onClick = createFeedback,
//            modifier = Modifier
//                .padding(vertical = 10.dp, horizontal = 20.dp)
//        )
    }
}


@Composable
private fun ProfileRowStars(
    title: String,
    fill: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            DefText(title, size = 12)
            Grade(fill, Modifier)
        }
        Row(
            Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefIcon(R.drawable.star_half, tint = Orange, size = 14.dp)
            DefText(
                fill.toString(),
                size = 12,
                modifier = Modifier.padding(start = 5.dp, top = 2.dp)
            )
        }
    }
    Spacer(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Gray)
    )
}

@Composable
private fun Grade(fill: Int, modifier: Modifier) {
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
        )
        DefIcon(
            if (fill >= 2) R.drawable.star else R.drawable.empty_star,
            size = 25.dp,
            tint = if (fill >= 2) Orange else Black,
            modifier = Modifier
                .padding(start = 2.dp)
                .clip(CircleShape)
        )
        DefIcon(
            if (fill >= 3) R.drawable.star else R.drawable.empty_star,
            size = 25.dp,
            tint = if (fill >= 3) Orange else Black,
            modifier = Modifier
                .padding(start = 2.dp)
                .clip(CircleShape)
        )
        DefIcon(
            if (fill >= 4) R.drawable.star else R.drawable.empty_star,
            size = 25.dp,
            tint = if (fill >= 4) Orange else Black,
            modifier = Modifier
                .padding(start = 2.dp)
                .clip(CircleShape)
        )
        DefIcon(
            if (fill >= 5) R.drawable.star else R.drawable.empty_star,
            size = 25.dp,
            tint = if (fill >= 5) Orange else Black,
            modifier = Modifier
                .padding(start = 2.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun ProfileRowOneText(
    title: String,
    modifier: Modifier = Modifier,
    isShowArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefText(title, size = 14, color = Black)
        if (isShowArrow)
            DefIcon(
                R.drawable.arrow_left,
                tint = DarkGray,
                size = 12.dp
            )
    }
    Spacer(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Gray)
    )
}

@Composable
private fun ProfileRow(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    isShowArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            DefText(title, size = 12)
            DefText(text, size = 13, modifier = Modifier.padding(top = 5.dp), color = Black)
        }
        if (isShowArrow)
            DefIcon(
                R.drawable.arrow_left,
                tint = DarkGray,
                size = 12.dp
            )
    }
    Spacer(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Gray)
    )
}