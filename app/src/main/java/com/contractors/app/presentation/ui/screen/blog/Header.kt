package com.contractors.app.presentation.ui.screen.blog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Brand
import com.contractors.app.presentation.ui.theme.White

@Composable
fun Header(
    textId: Int,
    back: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Brand)
            .padding(20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        DefIcon(
            R.drawable.right_row,
            tint = White,
            size = 15.dp,
            modifier = Modifier
                .padding(bottom = 6.dp)
                .align(Alignment.BottomStart)
                .clickable {
                    back()
                }
        )
        DefText(
            stringResource(textId),
            color = White,
            size = 22,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    back()
                }
        )
    }
}
