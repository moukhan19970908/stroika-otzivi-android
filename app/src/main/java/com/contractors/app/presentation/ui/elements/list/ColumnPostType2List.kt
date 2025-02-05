package com.contractors.app.presentation.ui.elements.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.calculateDistanceYandex

@Composable
fun ColumnPostType2List(
    title: String,
    list: List<Post>,
    clickItem: (Post) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            if (title.isNotEmpty()) {
                DefText(
                    if (list.isNotEmpty()) title else stringResource(R.string.notFound),
                    weight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        items(list.size, key = { list[it].id }) {
            val item = list[it]
            val distance by remember { mutableStateOf(calculateDistanceYandex(item.distance)) }

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(1.dp, Gray, RoundedCornerShape(10.dp))
                    .clickable {
                        clickItem(item)
                    },
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data("$BASE_URL${item.get_first_image.image_path}")
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = Const.ImageDescription.name,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                ) {
                    DefText(item.title, size = 14, weight = FontWeight.Bold, spacing = 10.sp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .offset(x = -3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefIcon(
                            R.drawable.adress,
                            tint = DarkGray,
                            size = 14.dp
                        )
                        DefText(
                            item.address,
                            size = 10,
                            spacing = 12.sp,
                            color = Gray,
                            modifier = Modifier.padding(start = 5.dp, top = 10.dp).offset(y = -3.dp)
                        )
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DefIcon(R.drawable.star_half, tint = Orange, size = 12.dp)
                            DefText(
                                item.rating,
                                color = Black,
                                size = 10,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }

                        if (item.distance != -1.0) {
                            DefText(
                                distance,
                                color = Black,
                                size = 10,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        DefIcon(R.drawable.home, tint = VeryDarkGray, size = 12.dp)
                        DefText(
                            "42.0 m\u00B2",
                            color = Black,
                            size = 10,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    DefText(item.description, size = 10, maxLength = 200, spacing = 10.sp)
                }
            }
        }
        item { Spacer(Modifier.height(100.dp)) }
    }
}