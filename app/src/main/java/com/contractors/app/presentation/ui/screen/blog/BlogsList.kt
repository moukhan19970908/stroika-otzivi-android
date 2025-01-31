package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.contractors.app.data.network.Blog
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel

@Composable
internal fun BlogsList(onClick: (Blog) -> Unit) {
    val context = LocalContext.current
    val dataViewModel = LocalDataViewModel.current
    val list = dataViewModel.state.blogs.data

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.height(5.dp)) }
        items(list.size, key = { list[it].id }) {
            val item = list[it]
            BlogCard(onClick = onClick, item = item, context = context)

        }
        item {
            BlogCard(
                onClick = onClick, item = Blog(
                    id = 3255,
                    title = "constituto",
                    image = "primis",
                    body = "graecis",
                    created_at = "propriae",
                    comments = listOf()
                ), context = context
            )
        }
    }
}