package com.contractors.app.presentation.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contractors.app.data.network.model.PostDTO
import com.contractors.app.presentation.ui.elements.list.GridPostType1List
import com.contractors.app.presentation.ui.model.Post

@Composable
fun MainLists(
    list: List<Post>,
    topList: List<Post>,
    nearestList: List<Post>,
    clickItem: (Post) -> Unit,
    showAll: (ListType) -> Unit
) {
    Column(
        Modifier
            .padding(bottom = 70.dp)
            .fillMaxSize()
    ) {
        GridPostType1List(
            title = "",
            list = list,
            clickItem = {},
            onFavoriteIconClicked = {},
        )

    }

}


