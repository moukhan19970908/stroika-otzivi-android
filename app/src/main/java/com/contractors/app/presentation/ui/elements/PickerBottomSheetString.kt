package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.theme.VeryDarkGray
import com.contractors.app.presentation.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerBottomSheetString(
    showBottomSheet: Boolean,
    list: List<String>,
    callback: (String) -> Unit,
    closeEvent: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(false)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                closeEvent()
            },
            containerColor = White,
            sheetState = sheetState
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(list.size) {
                    val item = list[it]
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .clickable {
                                callback(item)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DefText(
                            text = item,
                            size = 14,
                            color = VeryDarkGray,
                            align = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item { 
                    Spacer(modifier = Modifier.padding(bottom = 50.dp))
                }
            }
        }
    }
}