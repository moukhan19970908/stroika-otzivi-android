package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.Orange

@Composable
fun StarRow(fill: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefIcon(R.drawable.star, size = 16.dp, tint = if (fill >= 1) Orange else Gray,)
        DefIcon(R.drawable.star, size = 16.dp, tint = if (fill >= 2) Orange else Gray, modifier = Modifier.padding(start = 2.dp))
        DefIcon(R.drawable.star, size = 16.dp, tint = if (fill >= 3) Orange else Gray, modifier = Modifier.padding(start = 2.dp))
        DefIcon(R.drawable.star, size = 16.dp, tint = if (fill >= 4) Orange else Gray, modifier = Modifier.padding(start = 2.dp))
        DefIcon(R.drawable.star, size = 16.dp, tint = if (fill >= 5) Orange else Gray, modifier = Modifier.padding(start = 2.dp))
    }
}