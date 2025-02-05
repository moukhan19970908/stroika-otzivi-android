package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.contractors.app.data.network.Comment

@Composable
internal fun CommentRowListShort(list: List<Comment>, openProfile: (String) -> Unit) {
    val context = LocalContext.current
    LazyRow(
        Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(list.size, key = { list[it].id }) {
            val item = list[it]
            CommentCard(item = item, context = context, openProfile = openProfile, modifier = Modifier.width(312.dp))
        }
    }
}

