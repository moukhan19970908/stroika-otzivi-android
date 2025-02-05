package com.contractors.app.presentation.ui.screen.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.theme.White

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .then(modifier)
    ) {
        DefImage(
            imageId = R.drawable.logo,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

    }
}