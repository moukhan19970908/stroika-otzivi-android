package com.contractors.app.presentation.ui.screen.comment

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.CommentUser
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.RealtorComment
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.elements.StarRow
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
import com.contractors.app.presentation.ui.theme.VeryLightGray
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.domain.utils.userTypeToRole
import com.contractors.app.presentation.ui.screen.profile.roleToStrId

@Composable
fun CommentView() {

    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current

    Box(
        Modifier.fillMaxSize()
    ) {
        Column {
            Header(R.string.toReturn) {
                navController.navigate(Screen.Item.name)
            }
            Body {
                dataViewModel.onAction(DataAction.SetProfileById(it))
//                navController.navigate(Screen.UserProfile.name)
            }
        }
    }
}

@Composable
private fun Body(openProfile: (String) -> Unit) {

    val dataViewModel = LocalDataViewModel.current
    val stateFlow by dataViewModel.stateFlow.collectAsStateWithLifecycle()
    val item = stateFlow.comment

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(5.dp)
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)

    ) {
       when(item) {
           is MasterComment -> MasterComment(item, openProfile)
           is RealtorComment -> RealtorComment(item, openProfile)
       }
    }
}

@Composable
fun MasterComment(item: MasterComment, openProfile: (String) -> Unit) {
    val context = LocalContext.current
    val loginViewModel = LocalLoginViewModel.current
    val navController = LocalNavController.current
    var sendComment by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 16.dp, end = 16.dp
            )
    ) {
        Column {
            DefText(
                text = item.user.name + " " + item.user.last_name + " " + (item.user.sure_name ?: ""),
                size = 22,
                color = Black
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DefImage(
                    imageId = R.drawable.icon_diamond3d,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )

                DefText("0", size = 14, spacing = 16.sp)
                Spacer(Modifier.width(8.dp))
                DefText(
                    text = "Мастер",
                    size = 14,
                    color = Black,
                    align = TextAlign.End
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Box {
            if (item.user.avatar != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data("$BASE_URL${item.user.avatar}")
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = Const.ImageDescription.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            openProfile(item.user.id.toString())
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                DefImage(R.drawable.def_avatar, modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        openProfile(item.user.id.toString())
                    })
            }
        }
    }
    Row {
        Column(
            Modifier.padding(start = 10.dp)
        ) {
            DefText(item.user.fio ?: item.user.name, size = 16, weight = FontWeight.Bold, color = Black)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefText(userTypeToRole(item.user.user_type_id), size = 16, spacing = 10.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DefIcon(R.drawable.icon_diamond3d, tint = Blue, size = 20.dp)
                    DefText("13", size = 14, spacing = 10.sp)
                }
            }

        }
    }
    Row(
        Modifier.padding(top = 10.dp)
    ) {
        DefText(
            stringResource(R.string.setRole),
            size = 12,
        )
        DefText(
            item.role,
            weight = FontWeight.Bold,
            size = 12,
            modifier = Modifier.padding(start = 5.dp)
        )
    }

    Row(
        Modifier.padding(top = 10.dp)
    ) {
        DefText(
            stringResource(R.string.workPerformed),
            size = 14,
        )
        DefText(
            item.type_work,
            weight = FontWeight.Bold,
            size = 14,
            modifier = Modifier.padding(start = 5.dp)
        )
    }

    GradeRow(R.string.customerEmotionality, item.emotion_rating, Modifier.padding(top = 10.dp))
    GradeRow(R.string.customerSolvency, item.payment_rating, Modifier.padding(top = 10.dp))
    GradeRow(R.string.customerHonesty, item.honesty_rating, Modifier.padding(top = 10.dp))
    GradeRow(R.string.customerQuality, item.quality_rating, Modifier.padding(top = 10.dp))
    GradeRow(R.string.customerTimeliness, item.delivery_rating, Modifier.padding(top = 10.dp))
    Spacer(
        Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(LightGray, CircleShape)
    )
    DefText(item.experience, size = 14, color = Black)
    DefText(
        stringResource(R.string.customerRecommendation),
        size = 14,
        color = Gray,
        modifier = Modifier.padding(top = 10.dp),
        spacing = 14.sp
    )
    DefText(item.recommendations, size = 14, color = Black, modifier = Modifier.padding(top = 10.dp))
    if (loginViewModel.state.userInfo.role == Role.Customer) {
        if (!sendComment) {
            Row(
                Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefText(
                    "+",
                    size = 20,
                    color = Blue,
                )
                DefText(
                    "Оставить комментарий",
                    size = 16,
                    color = Blue,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            navController.navigate(Screen.SubComment.name)
                            sendComment = true
                        }
                )
            }
        }
        else {
            Box(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(VeryLightGray, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                DefText(
                    "Ваш комментарий будет опубликован после проверки администратором",
                    size = 14,
                    color = Black,
                )
            }
        }
    }

}

@Composable
fun RealtorComment(item: RealtorComment, openProfile: (String) -> Unit) {

    val context = LocalContext.current

    Row {
        if (item.user.avatar != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .crossfade(true)
                    .data("$BASE_URL${item.user.avatar}")
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .build(),
                contentDescription = Const.ImageDescription.name,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .clickable {
                        openProfile(item.user.id.toString())
                    },
                contentScale = ContentScale.Crop
            )
        } else {
            DefImage(R.drawable.def_avatar, modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .clickable {
                    openProfile(item.user.id.toString())
                })
        }


        Column(
            Modifier.padding(start = 10.dp)
        ) {
            DefText(item.user.fio ?: item.user.name, size = 14, weight = FontWeight.Bold, color = Black)
            DefText(userTypeToRole(item.user.user_type_id), size = 12, spacing = 10.sp)
        }
    }
    Row(
        Modifier.padding(top = 20.dp)
    ) {
        StarRow(item.rating)
        DefText(
            formatDate(item.updated_at),
            size = 12,
            color = Gray,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
   Spacer(
       Modifier
           .padding(vertical = 5.dp)
           .fillMaxWidth()
           .height(1.dp)
           .background(LightGray, CircleShape)
    )
    DefText(
        stringResource(R.string.advantages),
        size = 14,
        color = Gray,
        modifier = Modifier.padding(top = 10.dp),
        spacing = 14.sp
    )
    DefText(item.advantage, size = 14, color = Black)
    DefText(
        stringResource(R.string.disadvantages),
        size = 14,
        color = Gray,
        modifier = Modifier.padding(top = 10.dp),
        spacing = 14.sp
    )
    DefText(item.disadvantage, size = 14, color = Black, modifier = Modifier.padding(top = 10.dp))
}


@Composable
fun GradeRow(textId: Int, grade: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefText(
            stringResource(textId),
            size = 12,
            color = Black,
            modifier = Modifier.weight(1f),
            spacing = 14.sp
        )
        Row {
            StarRow(grade)
            DefText(
                grade.toString(),
                size = 12,
                color = Gray,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}