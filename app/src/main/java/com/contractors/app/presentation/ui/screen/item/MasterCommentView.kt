package com.contractors.app.presentation.ui.screen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.OwnComment
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.domain.utils.userTypeToRole
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.White

@Composable
internal fun MasterCommentView(
    item: MasterComment,
    readMore: (OwnComment) -> Unit,
    openProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Gray, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable(
                onClick = { readMore.invoke(item) },
                indication = null,
                interactionSource = interactionSource
            )
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
                        .clickable(
                            onClick = { openProfile(item.user.id.toString()) }
                        ),
                    contentScale = ContentScale.Crop
                )
            } else {
                DefImage(R.drawable.def_avatar, modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = { openProfile(item.user.id.toString()) }
                    )
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                DefImage(
                    imageId = R.drawable.star,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                DefText(
                    item.honesty_rating.toDouble().toString(),
                    size = 11,
                    color = Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )


            }

            Spacer(Modifier.weight(1f))
            DefText(
                formatDate(item.created_at),
                size = 11,
                color = Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        DefText(item.experience, size = 12)
    }
//    Column(
//        modifier
//            .height(180.dp)
//            .background(color = White, shape = RoundedCornerShape(10.dp))
//            .border(1.dp, LightGray, RoundedCornerShape(10.dp))
//            .padding(10.dp)
//            .fillMaxWidth()
//    ) {
//        Row {
//            if (item.user.avatar != null) {
//                AsyncImage(
//                    model = ImageRequest.Builder(context)
//                        .crossfade(true)
//                        .data("$BASE_URL${item.user.avatar}")
//                        .diskCachePolicy(CachePolicy.DISABLED)
//                        .memoryCachePolicy(CachePolicy.DISABLED)
//                        .build(),
//                    contentDescription = Const.ImageDescription.name,
//                    modifier = Modifier
//                        .size(35.dp)
//                        .clip(CircleShape)
//                        .clickable {
//                            openProfile(item.user.id.toString())
//                        },
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                DefImage(R.drawable.def_avatar, modifier = Modifier
//                    .size(35.dp)
//                    .clip(CircleShape)
//                    .clickable {
//                        openProfile(item.user.id.toString())
//                    })
//            }
//
//            Column(
//                Modifier.padding(start = 10.dp)
//            ) {
//                DefText(
//                    item.user.fio ?: item.user.name,
//                    size = 14,
//                    weight = FontWeight.Bold,
//                    color = Black
//                )
//                Row(
//                    Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    DefText(userTypeToRole(item.user.user_type_id), size = 12, spacing = 10.sp)
//                    if (item.user.user_type_id == 1) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            DefIcon(R.drawable.crystal, tint = Blue, size = 18.dp)
//                            DefText("13", size = 12, spacing = 10.sp)
//                        }
//                    }
//                }
//            }
//        }
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            DefText(
//                formatDate(item.created_at),
//                size = 12,
//                color = Gray,
//                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
//            )
//        }
//        DefText(
//            item.experience,
//            size = 12,
//            color = DarkGray,
//            modifier = Modifier.padding(start = 5.dp, top = 5.dp),
//            maxLength = 100
//        )
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End
//        ) {
//            DefText(
//                stringResource(R.string.readNext),
//                size = 10,
//                weight = FontWeight.Bold,
//                color = Blue,
//                modifier = Modifier.clickable { readMore(item) }
//            )
//        }
//    }

}

