package com.contractors.app.presentation.ui.elements.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.contractors.app.domain.utils.Const

@Composable
fun DefIcon(
    imageId: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    size: Dp = 30.dp,
    clip: Boolean = false
) {
    Box(
        modifier.clip(if (clip) CircleShape else RoundedCornerShape(0)).size(size)
    ) {
        Icon(
            painter = painterResource(id = imageId),
            contentDescription = Const.ImageDescription.name,
            tint = tint,
            modifier = Modifier.fillMaxSize()
        )
    }

}