package com.contractors.app.presentation.ui.elements.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contractors.app.R

import com.contractors.app.presentation.ui.elements.components.DefCardPostType1
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.domain.utils.calculateDistanceYandex

@Composable
internal fun GridPostType1List(
    title: String,
    list: List<Post>,
    clickItem: (Post) -> Unit,
    onFavoriteIconFilledClicked: (postItem: Post) -> Unit,
    onFavoriteIconUnfilledClicked: (postItem: Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        if (title.isNotEmpty()) {
            DefText(
                if (list.isNotEmpty()) title else stringResource(R.string.notFound),
                weight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = modifier.weight(1f)
        ) {
            items(list.size,
                key = { list[it].id }
            ) {
                val item = list[it]
                val distance by remember { mutableStateOf(calculateDistanceYandex(item.distance)) }
                DefCardPostType1(
                    item = item,
                    distance = distance,
                    onFavoriteIconUnfilledClicked = { onFavoriteIconUnfilledClicked.invoke(item) },
                    onFavoriteIconFilledClicked = { onFavoriteIconFilledClicked.invoke(item) },

                    onCardClicked = { post ->
                        clickItem(post)
                    }
                )
            }

        }

    }
}
