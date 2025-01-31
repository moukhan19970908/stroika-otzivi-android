package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.DarkGray
import com.contractors.app.presentation.ui.theme.Gray
import com.contractors.app.presentation.ui.theme.LightBlue
import com.contractors.app.presentation.ui.theme.LightGray
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerBottomSheetCheck(
    showBottomSheet: Boolean,
    list: List<String>,
    currentList: List<String>,
    isSingle: Boolean = false,
    callback: (List<String>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(false)
    var selectedList by remember { mutableStateOf(mutableListOf<String>()) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                callback(selectedList)
            },
            containerColor = White,
            sheetState = sheetState
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(list.size) {
                    val item = list[it]
                    var check by remember { mutableStateOf(currentList.contains(item)) }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val interactionSource = remember { MutableInteractionSource()}
                        DefText(
                            text = item,
                            size = 14,
                            color = VeryDarkGray,
                            modifier = Modifier.fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        check = !check
                                        if (check) {
                                            if (isSingle) selectedList = mutableListOf(item)
                                            else selectedList.add(item)
                                        } else {
                                            selectedList.remove(item)
                                        }
                                        if (isSingle){
                                            callback(listOf(item))
                                        }
                                    },
                                    indication = null,
                                    interactionSource = interactionSource
                                )
                        )
                        if (isSingle) {
                            RadioButton(
                                selected = check,
                                colors = RadioButtonColors(
                                    selectedColor = LightBlue,
                                    unselectedColor = DarkGray,
                                    disabledSelectedColor = LightGray,
                                    disabledUnselectedColor = LightGray

                                ),
                                onClick = {
                                    callback(listOf(item))
                                }

                            )
                        } else {
                            Checkbox(
                                check,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LightBlue,
                                    uncheckedColor = DarkGray,
                                    checkmarkColor = White
                                ),
                                onCheckedChange = { it ->
                                    check = it
                                    if (it) {
                                        if (isSingle) selectedList = mutableListOf(item)
                                        else selectedList.add(item)
                                    } else {
                                        selectedList.remove(item)
                                    }
                                    if (isSingle){
                                        callback(listOf(item))
                                    }
                                }
                            )

                        }
                    }
                    Spacer(modifier = Modifier.background(Gray).fillMaxWidth().height(2.dp).padding(top = 5.dp))
                }
                item {
                    if (!isSingle) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            DefButton(stringResource(R.string.choice)) { callback(selectedList) }
                        }
                    }
                }
                item { 
                    Spacer(modifier = Modifier.padding(bottom = 50.dp))
                }
            }
        }
    }
}