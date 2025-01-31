package com.contractors.app.presentation.ui.screen.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.RealtorComment
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalDataViewModel
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.screen.blog.Header
import com.contractors.app.presentation.ui.screen.item.MasterCommentView
import com.contractors.app.presentation.ui.screen.item.RealtorCommentView
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.main.DataAction
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Blue
import com.contractors.app.presentation.ui.theme.FadedLightBlue
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.Orange
import java.time.Instant

@Composable
fun CommentListView() {

    val navController = LocalNavController.current
    val dataViewModel = LocalDataViewModel.current
    val loginViewModel = LocalLoginViewModel.current

    val item = dataViewModel.state.selectedPost
    val allList =
        dataViewModel.state.commentList.sortedBy { Instant.parse(it.created_at).toEpochMilli() }
            .reversed()

    var select by remember { mutableStateOf(0) }

    val realtorList = allList.filter { it::class == RealtorComment::class }
    val masterList = allList.filter { it::class == MasterComment::class }

    val list = when (select) {
        1 -> masterList
        2 -> realtorList
        else -> allList
    }
    Box {
        Column {
            Header(R.string.allFeedback) {
                navController.popBackStack()
            }
            Column(
                Modifier.padding(horizontal = 10.dp),
            ) {
                Row {
                    Selector("Все", select == 0) {
                        select = 0
                    }
                    Selector("От мастеров", select == 1) {
                        select = 1
                    }
                    Selector("От риелторов", select == 2) {
                        select = 2
                    }
                }
                LazyColumn(
                    Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(list.size, key = { list[it].id }) {
                        val item = list[it]
                        when (item) {
                            is MasterComment -> MasterCommentView(item,
                                readMore = {
                                    dataViewModel.onAction(DataAction.SetComment(it))
                                    navController.navigate(Screen.Comment.name)
                                }, openProfile = {
                                    dataViewModel.onAction(DataAction.SetProfileById(it))
                                }
                            )
                            is RealtorComment -> RealtorCommentView(item,
                                readMore = {
                                    dataViewModel.onAction(DataAction.SetComment(it))
                                    navController.navigate(Screen.Comment.name)
                                }, openProfile = {
                                    dataViewModel.onAction(DataAction.SetProfileById(it))
                                })
                        }
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 26.dp)
        ) {
            DefButton(
                text = "Добавить отзыв",
                onClick = { navController.navigate(Screen.NewComment.name) }
            )
        }

    }
}

@Composable
private fun Selector(text: String, isCheck: Boolean = false, check: () -> Unit) {

    Row(
        Modifier
            .padding(end = 10.dp)
            .padding(top = 8.dp)
            .padding(bottom = 16.dp)
            .clickable { check() }
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, if (isCheck) Color.LightGray else Gray, RoundedCornerShape(10.dp))
            .background(if (isCheck) Color.LightGray else Color.White, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(isCheck) {
            DefIcon(R.drawable.little_check, tint = Blue, size = 18.dp)
        }
        DefText(text, modifier = Modifier.padding(5.dp))
    }
}