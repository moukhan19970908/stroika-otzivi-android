package com.contractors.app.presentation.ui.elements.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.R
import com.contractors.app.data.network.Image
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.Orange
import com.contractors.app.presentation.ui.theme.White
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const

@Composable
fun DefCardPostType1(
    item: Post,
    distance: String,
    onCardClicked: (Post) -> Unit,
    onFavoriteIconUnfilledClicked: () -> Unit,
    onFavoriteIconFilledClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var onFavoriteIconClicked by remember { mutableStateOf(item.isFavorite) }
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = White,
            contentColor = Black,
            disabledContainerColor = White,
            disabledContentColor = Black
        ),
        elevation = CardDefaults.cardElevation(),
        border = BorderStroke(1.dp, LightGray),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onCardClicked(item)
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
                .clip(RoundedCornerShape(7.dp))
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        ) {
            DefImage(imageId = R.drawable.star_new, Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            DefText(
                item.rating,
                color = Black,
                size = 12,
            )
            if (item.distance != -1.0) {
                Spacer(
                    Modifier
                        .padding(start = 4.dp)
                        .width(1.dp)
                        .height(3.dp)
                        .background(LightGray)
                )
                DefText(
                    text = "$distance км",
                    color = Black,
                    size = 12,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            DefIcon(
//                todo интеграция с backendом
                imageId = if (onFavoriteIconClicked) R.drawable.icon_favorite_filled else R.drawable.icon_favorite,
                tint = Color(0xFF565776),
                size = 16.dp,
                modifier = Modifier
                    .clickable {
                        if (onFavoriteIconClicked) {
                            onFavoriteIconFilledClicked()
                        } else onFavoriteIconUnfilledClicked()
                        onFavoriteIconClicked = !onFavoriteIconClicked
                    }
                    .padding(8.dp)
            )

        }
        Column(
            Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            DefText(item.title, weight = FontWeight.Bold, color = Black)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .offset(x = (-3).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefIcon(R.drawable.adress, tint = DarkGray, size = 15.dp)
                DefText(
                    item.address,
                    size = 10,
                    spacing = 14.sp,
                    color = Gray,
                    modifier = Modifier.padding(start = 5.dp),
                    maxLength = 55
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefCardPreview() {
    DefCardPostType1(
        item = Post(
            id = 5725,
            title = "errem",
            description = "sapien",
            user_id = 3286,
            latitude = "has",
            longitude = "purus",
            status = "iaculis",
            address = "proin",
            rating = "4.1",
            created_at = "gubergren",
            updated_at = "nonumy",
            get_first_image = Image(
                id = 4344,
                image_path = "massa",
                post_id = 3936,
                type = "facilisis",
                created_at = "tacimates",
                updated_at = "semper"
            ),
            images = listOf(),
            master_comments = listOf(),
            rieltor_comments = listOf(),
            distance = 2.3,
            isFavorite = true
        ),
        onCardClicked = { },
        distance = "",
        onFavoriteIconUnfilledClicked = {},
        onFavoriteIconFilledClicked = {}

    )

}