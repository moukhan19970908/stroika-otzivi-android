package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Brand
import com.contractors.app.presentation.ui.theme.White

@Composable
fun HeaderText(title: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Brand)
            .padding(10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        DefText(
            title,
            size = 22,
            color = White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}