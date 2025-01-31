package com.contractors.app.presentation.ui.elements.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.calculateDistanceYandex

@Composable
internal fun RowList(list: List<Post>, clickItem: (Post) -> Unit) {
    val context = LocalContext.current
    val loginViewModel = LocalLoginViewModel.current

    LazyRow(
        Modifier
            .padding(top = 20.dp)
            .height(240.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(Modifier.width(10.dp))
        }
        items(list.size, key = { list[it].id }) {
            val item = list[it]
            val distance by remember { mutableStateOf(calculateDistanceYandex(item.distance)) }

            Column(
                Modifier
                    .border(1.dp, LightGray, RoundedCornerShape(20.dp))
                    .width(200.dp)
                    .clickable {
                        clickItem(item)
                    }
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
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    Modifier.padding(10.dp)
                ) {
                    DefText(item.title, weight = FontWeight.Bold, color = Black)
                    DefText(item.address, size = 12, maxLength = 55)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        DefIcon(R.drawable.star_half, tint = Orange, size = 20.dp)
                        DefText(
                            item.rating,
                            color = Black,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(
                            Modifier
                                .padding(start = 10.dp)
                                .width(1.dp)
                                .height(3.dp)
                                .background(LightGray)
                        )
                        if (item.distance != -1.0) {
                            DefText(
                                distance,
                                color = Black,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(Modifier.width(5.dp))
        }
    }
}