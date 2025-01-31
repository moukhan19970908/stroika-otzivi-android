package com.contractors.app.presentation.ui.screen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.OwnComment
import com.contractors.app.data.network.RealtorComment
import com.contractors.app.presentation.ui.theme.White

@Composable
internal fun CommentList(
    commentList: List<OwnComment>,
    readMore: (OwnComment) -> Unit,
    openProfile: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 10.dp)
    ) {
        item {
            Spacer(Modifier.width(10.dp))
        }
        items(commentList.size) {
            val item = commentList[it]
            Box(
                Modifier
                    .background(White, shape = RoundedCornerShape(10.dp))
                    .width(260.dp)
            ) {
                when (item) {
                    is MasterComment -> MasterCommentView(item, readMore, openProfile)
                    is RealtorComment -> RealtorCommentView(item, readMore, openProfile)
                }
            }
        }
        item {
            Spacer(Modifier.width(if (commentList.size == 1) 260.dp else 5.dp))
        }
    }
}