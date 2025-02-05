package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White

@Composable
fun GoogleBtn(
    modifier: Modifier = Modifier,
    color: Color = White,
    textColor: Color = VeryDarkGray,
    textWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .border(2.dp, Gray, RoundedCornerShape(5.dp))
            .background(color, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .width(300.dp)
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
    ) {
        DefImage(R.drawable.google, Modifier.size(30.dp).align(Alignment.CenterStart))
        DefText(
            text = stringResource(R.string.google),
            modifier = Modifier.padding(start = 20.dp).align(Alignment.Center),
            color = textColor,
            weight = textWeight,
            size = 18
        )
    }
}