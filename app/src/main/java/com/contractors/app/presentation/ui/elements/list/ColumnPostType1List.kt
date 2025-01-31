package com.contractors.app.presentation.ui.elements.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefCardPostType1
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.domain.utils.calculateDistanceYandex

@Composable
fun ColumnPostType1List(
    title: String,
    list: List<Post>,
    clickItem: (Post) -> Unit,
    onFavoriteIconUnfilledClicked: (idPostServer: Int) -> Unit,
    onFavoriteIconFilledClicked: (idPostServer: Int) -> Unit,
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
            DefCardPostType1(
                item = item,
                distance = distance,
                onCardClicked = {
                    clickItem(item)
                },
                onFavoriteIconUnfilledClicked = {
                    onFavoriteIconUnfilledClicked(item.id)
                },
                onFavoriteIconFilledClicked = {
                    onFavoriteIconFilledClicked(item.id)
                }
            )
            println(item.isFavorite)
        }

        item { Spacer(Modifier.height(100.dp)) }
    }
}