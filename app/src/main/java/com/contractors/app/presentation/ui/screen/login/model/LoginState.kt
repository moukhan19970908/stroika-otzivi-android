package com.contractors.app.presentation.ui.screen.login.model

import com.yandex.mapkit.geometry.Point

data class LoginState(
    val token: String = "",
    val point: Point = Point(0.0,0.0),
    val userInfo: UserInfo = UserInfo()
)
