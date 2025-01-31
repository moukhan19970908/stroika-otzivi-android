package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.contractors.app.data.network.Comment

@Composable
internal fun CommentColumnListShort(list: List<Comment>, openProfile: (String) -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(list.size, key = { list[it].id }) {
            val item = list[it]
            CommentCard(item = item, context = context, openProfile = openProfile)
        }
    }
}

