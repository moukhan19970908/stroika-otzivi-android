package com.contractors.app.presentation.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefIcon
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.Black
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.White

@Composable
fun SelectInput(
    title: String,
    label: String,
    text: String,
    errorText: String = "",
    list: List<String>,
    isSingle: Boolean = false,
    isError: Boolean = false,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var open by remember { mutableStateOf(false) }
    var currentList by remember { mutableStateOf(listOf<String>()) }

    PickerBottomSheetCheck(
        showBottomSheet = open,
        list = list,
        currentList = currentList,
        isSingle = isSingle,
        callback = {
            currentList = it
            onChange(it.joinToString(separator = ", "))
            open = false
        }
    )
    val interactionSource = remember { MutableInteractionSource() }
    Column {
        Box(
            modifier
                .fillMaxWidth()
                .background(White, shape = RoundedCornerShape(5.dp))
                .border(1.dp, if (isError) Red else Gray, shape = RoundedCornerShape(5.dp))
                .animateContentSize()
                .padding(start = 15.dp, top = 10.dp, bottom = 5.dp)
                .clickable(
                    onClick = {
                        open = true
                    },
                    indication = null,
                    interactionSource = interactionSource
                )
        ) {
            Column(
                Modifier.fillMaxWidth(.9f)
            ) {
                DefText(title, color = Black, size = 14)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp)
                        .padding(horizontal = 1.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefText(
                        if (currentList.isEmpty()) label else text,
                        size = 14,
                        color = if (currentList.isEmpty()) Gray else Black,
                        singleLine = false,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
            DefIcon(
                if (open) R.drawable.arrow_top else R.drawable.arrow_down,
                tint = Black,
                size = 24.dp,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        AnimatedVisibility (currentList.isNotEmpty() || isError) {
            DefText(
                text = errorText,
                color = if (isError) Red else Gray,
                size = 12,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 17.dp)
            )
        }
    }

}