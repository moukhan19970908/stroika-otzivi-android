package com.contractors.app.presentation.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SystemState(
    val openCard: String = ""
)

sealed interface SystemAction {
    data class OpenDeck(val deckInfo: String): SystemAction

}

@HiltViewModel
class SystemViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(SystemState())

    fun onAction(action: SystemAction) {
        when(action) {
            is SystemAction.OpenDeck -> TODO()
        }
    }
}