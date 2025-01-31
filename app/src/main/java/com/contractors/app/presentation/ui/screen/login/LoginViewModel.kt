package com.contractors.app.presentation.ui.screen.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.contractors.app.data.datastore.PrefManager
import com.contractors.app.data.network.CodeResponse
import com.contractors.app.data.network.RegistrationInfo
import com.contractors.app.data.network.UpdateProfileInfo
import com.contractors.app.data.network.model.GetProfileDTO
import com.contractors.app.data.repository.AppRepository
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.Const
import com.contractors.app.domain.utils.errorParser
import com.contractors.app.domain.utils.jsonToMap
import com.contractors.app.presentation.ui.screen.login.model.LoginState
import com.contractors.app.presentation.ui.screen.login.model.Role
import com.contractors.app.presentation.ui.screen.login.model.UserInfo
import com.google.gson.Gson
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val ktorRepository: AppRepository,
    private val prefManager: PrefManager,
) : ViewModel() {
    var state by mutableStateOf(LoginState())

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.Registration -> registration(action.info, action.callback)
            is LoginAction.Logout -> logout()
            is LoginAction.IsAuth -> isAuth(action.callback)
            is LoginAction.Login -> login(action.number, action.pass, action.callback)
            is LoginAction.GetProfile -> getProfile()
            is LoginAction.UploadAvatar -> uploadAvatar(action.file)
            is LoginAction.UpdateUser -> updateUser(action.updateUser)
            is LoginAction.ChangePass -> changePass(action.old, action.new, action.callback)
            is LoginAction.SetPoint -> setPoint(action.point)
            is LoginAction.Verify -> verify(action.phone, action.code, action.callback)
        }
    }

    private fun verify(phone: String, code: String, callback: (Boolean, String) -> Unit) {
        ktorRepository.verify(phone, code) { code, result ->
            if (code == 200) {
                val gson = Gson()
                val resp = gson.fromJson(result, CodeResponse::class.java)

                resp.token.let {
                    state = state.copy(token = it)
                    prefManager.setString(Const.AuthToken.name, it)
                    callback(true, "")
                }
            } else {
                callback(false, errorParser(result))
            }
        }
    }

    private fun setPoint(point: Point) {
        state = state.copy(point = point)
    }

    private fun changePass(old: String, new: String, callback: (Boolean, String) -> Unit) {
        ktorRepository.changePassword(old, new, state.token) { code, result ->
            callback(code == 200, if (code != 200) errorParser(result) else "")
        }
    }

    private fun updateUser(updateUser: UpdateProfileInfo) {
        ktorRepository.updateProfile(updateUser, state.token) { code, result ->
            if (code == 200) {
                getProfile()
            }
        }
    }

    private fun uploadAvatar(file: File) {
        ktorRepository.uploadAvatar(file, state.token) { code, result ->
        }
    }

    private fun getProfile() {
        val token = prefManager.getString(Const.AuthToken.name) ?: ""
        ktorRepository.getProfileByToken(token) { code, result ->

            if (code == 200) {
                val gson = Gson()
                val userResp = gson.fromJson(result, GetProfileDTO::class.java)
                val user1 = userResp.user!!.getOrNull(0) ?: return@getProfileByToken
                state = state.copy(
                    userInfo = UserInfo(
                        id = user1.id.toString() ?: "0",
                        name = user1.name ?: "",
                        surname = user1.middle_name ?: "",
                        lastname = user1.surname ?: "",
                        number = user1.phone ?: "",
                        email = user1.email ?: "",
                        experience = user1.experience ?: "",
                        specialist = user1.specialist ?: "",
                        role = when (user1.user_type_id) {
                            1 -> Role.Master
                            2 -> Role.Realtor
                            3 -> Role.Customer
                            else -> Role.Master
                        },
                        userTypeId = user1.user_type_id ?: 1,
                        imageUrl = "$BASE_URL${user1.avatar}" ?: ""
                    )
                )
            }
        }
    }

    private fun login(number: String, pass: String, callback: (Boolean, String) -> Unit) {
        ktorRepository.auth(number, pass) { code, result ->
            if (code == 200) {
                val map: Map<String, String> = jsonToMap(result)
                map["token"].let {
                    if (it != null) {
                        state = state.copy(token = it)
                        prefManager.setString(Const.AuthToken.name, it)
                        callback(true, "")
                    } else {
                        callback(false, errorParser(result))
                    }
                }
            } else {
                callback(false, errorParser(result))
            }
        }
    }

    private fun isAuth(callback: (Boolean) -> Unit) {
        if (state.token.isNotEmpty()) {
            callback(true)
            return
        }
        val token = prefManager.getString(Const.AuthToken.name)
        if (token != null) {
            state = state.copy(token = token)
            callback(true)
        } else {
            callback(false)
        }
    }

    private fun logout() {
        prefManager.removeKey(Const.AuthToken.name)
        state = state.copy(token = "")
    }

    private fun registration(info: RegistrationInfo, callback: (Boolean, String) -> Unit) {

        println(" my register Llsakjdksjd = ${info.special}")
        println(" userType_id == ${info.user_type_id} ")

        ktorRepository.registration(info) { code, result ->
            if (code == 200) {
                println(" ok register SUCCESS !ldskjflkasdjflk ")
                callback(true, "Success")
            } else {
                       println(" ok register FUYUCJLJCICUCUUCUCUC  ldskjflkasdjflk ")
                callback(false, errorParser(result))
            }
        }
    }
}

sealed interface LoginAction {
    data class SetPoint(val point: Point) : LoginAction
    data class Verify(
        val phone: String,
        val code: String,
        val callback: (Boolean, String) -> Unit
    ) : LoginAction

    data class ChangePass(
        val old: String,
        val new: String,
        val callback: (Boolean, String) -> Unit
    ) : LoginAction

    data class UpdateUser(val updateUser: UpdateProfileInfo) : LoginAction
    data class UploadAvatar(val file: File) : LoginAction
    class GetProfile : LoginAction
    data class IsAuth(val callback: (Boolean) -> Unit) : LoginAction
    data class Login(
        val number: String,
        val pass: String,
        val callback: (Boolean, String) -> Unit
    ) : LoginAction

    data class Registration(val info: RegistrationInfo, val callback: (Boolean, String) -> Unit) :
        LoginAction

    data object Logout : LoginAction
}