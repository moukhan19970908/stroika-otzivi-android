package com.contractors.app.presentation.ui.screen.blog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.Comment
import com.contractors.app.data.network.CommentUser
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.domain.utils.userTypeToRole
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.White

@Composable
internal fun CommentCard(
    item: Comment,
    context: Context,
    openProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Gray, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
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
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            openProfile(item.user.id.toString())
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                DefImage(R.drawable.def_avatar, modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        openProfile(item.user.id.toString())
                    }
                )
            }

            Column(
                Modifier.padding(start = 8.dp)
            ) {
                DefText(
                    item.user.fio ?: item.user.name,
                    weight = FontWeight.Bold,
                    color = Black,
                    size = 16
                )

                if (item.user.user_type_id == 1) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        DefText(userTypeToRole(item.user.user_type_id), size = 12)
                        Spacer(Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DefImage(
                                imageId = R.drawable.icon_diamond3d,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(24.dp)
                            )
                            DefText("0", size = 12, spacing = 16.sp)
                        }
                    }

                }
            }
        }
        Row {
            Spacer(Modifier.weight(1f))
            DefText(
                formatDate(item.created_at),
                size = 11,
                color = Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        DefText(item.text, size = 12, modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview
@Composable

private fun CommentCardPreview() {
    val context = LocalContext.current
    CommentCard(
        item = Comment(
            id = 5371,
            text = "mauris",
            blog_id = 8625,
            user_id = 8278,
            created_at = "mattis",
            updated_at = "egestas",
            user = CommentUser(
                id = 4003,
                fio = null,
                sure_name = "Leonel Owens",
                name = "Jake Clarke",
                last_name = "Brandie Kim",
                user_type_id = 1,
                experience = "cras",
                email = "cherry.durham@example.com",
                phone = "(477) 739-6185",
                email_verified_at = null,
                avatar = null,
                created_at = "his",
                updated_at = "quaestio"
            )
        ), context = context, openProfile = {},

    )

}