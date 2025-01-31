package com.contractors.app.presentation.ui.screen.sendCode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.contractors.app.data.repository.AppRepository
import com.contractors.app.data.network.RegistrationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class SendType{
    Reg,
    ReturnPass,
    Email,
    Number
}

data class SendParam(
    val type: SendType = SendType.Reg,
    val number: String = "",
    val email: String = "",
    val callback: (Boolean) -> Unit = {}
)

data class SendCodeState(
    val registrationInfo: RegistrationInfo = RegistrationInfo(),
    val param: SendParam = SendParam()
)

sealed interface SendCodeAction {
    data class SetParam(val sendParam: SendParam): SendCodeAction
    data class SetRegInfo(val registrationInfo: RegistrationInfo): SendCodeAction
}

@HiltViewModel
class SendCodeViewModel @Inject constructor(
    private val appRepository: AppRepository
): ViewModel() {
    var state by mutableStateOf(SendCodeState())

    fun onAction(action: SendCodeAction) {
        when (action) {
            is SendCodeAction.SetParam -> setParam(action.sendParam)
            is SendCodeAction.SetRegInfo -> setRegInfo(action.registrationInfo)
        }
    }

    private fun setRegInfo(registrationInfo: RegistrationInfo) {
        state = state.copy(registrationInfo = registrationInfo)
    }

    private fun setParam(param: SendParam) {
        state = state.copy(param = param)
    }

}
