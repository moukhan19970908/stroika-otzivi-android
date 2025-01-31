package com.contractors.app.presentation.ui.screen.blog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.contractors.app.data.network.Blog
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.formatDate
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.White

@Composable
internal fun BlogCard(
    onClick: (Blog) -> Unit,
    item: Blog,
    context: Context
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp, Gray, RoundedCornerShape(12.dp))
            .background(color = White, shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
            .clickable {
                onClick(item)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .crossfade(true)
                .data("$BASE_URL${item.image}")
                .diskCachePolicy(CachePolicy.DISABLED)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build(),
            contentDescription = Const.ImageDescription.name,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            DefText(text = item.title, weight = FontWeight.Bold, size = 16, maxLength = 20)
            DefText(
                text = item.body,
                maxLength = 160,
                size = 12,
                align = TextAlign.Start,
                modifier = Modifier.padding(top = 5.dp)
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DefText(
                    text = formatDate(item.created_at),
                    weight = FontWeight.Medium,
                    size = 12,
                    color = Gray,
                    modifier = Modifier
                        .padding(top = 10.dp),
                )
            }
        }
    }
}
