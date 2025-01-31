package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.SecondaryLight
import com.contractors.app.presentation.ui.theme.White

@Composable
fun FooterItem(
    modifier: Modifier,
    isActive: Boolean,
    iconId: Int,
    text: String,
    onClick: () -> Unit
) {
    val color = if (isActive) White else SecondaryLight
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefIcon(
                iconId, size = 20.dp,
                tint = color,
                modifier = Modifier.padding(top = 10.dp)
            )
            DefText(text, color = color, size = 12)
        }

        if (isActive)
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(color, CircleShape)
            )
    }
}