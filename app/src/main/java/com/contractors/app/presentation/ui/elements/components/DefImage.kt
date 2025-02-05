package com.contractors.app.presentation.ui.elements.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.contractors.app.domain.utils.Const

@Composable
fun DefImage(
    @DrawableRes
    imageId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        painter = painterResource(id = imageId),
        contentDescription = Const.ImageDescription.name,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxSize()
    )
}