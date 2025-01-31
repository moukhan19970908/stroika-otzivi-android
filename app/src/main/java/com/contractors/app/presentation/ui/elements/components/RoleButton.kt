package com.contractors.app.presentation.ui.elements.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.extentions.backgroundImage
import com.contractors.app.presentation.ui.theme.White

@Composable
fun RoleButton(
    @DrawableRes
    imageId: Int,
    @DrawableRes
    backgroundImageId: Int,
    textTitle: String,
    textColor: Color = White,
    textWeight: FontWeight = FontWeight.Medium,
    textDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isRoleButtonClicked by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(if (isRoleButtonClicked) 4.dp else 0.dp)
            .heightIn(min = 238.dp)
            .width(380.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .backgroundImage(
                painter = painterResource(id = backgroundImageId),
                contentScale = ContentScale.FillBounds,
            )
            .padding(all = 16.dp)
            .clickable(
                onClick = {
                    isRoleButtonClicked = true
                    onClick()
                },
                indication = null,
                interactionSource = interactionSource
            )
            .animateContentSize()


        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
            ) {
            DefImage(
                imageId = imageId,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(102.dp)
                    .height(96.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            DefText(
                text = textTitle,
                color = textColor,
                weight = textWeight,
                size = 36
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        DefText(
            text = textDescription,
            color = textColor,
            weight = textWeight,
            size = 12
        )
    }
}