package com.contractors.app.presentation.ui.screen.login.model

import com.contractors.app.R
import java.util.UUID

data class UserInfo(
    val id: String = UUID.randomUUID().toString(),
    val imageId: Int = R.drawable.commentimg,
    val name: String = "",
    val surname: String? = "",
    val lastname: String? = "",
    val experience: String = "",
    val specialist: String = "",
    val email: String = "",
    val password: String = "12345678",
    val number: String = "",
    val imageUrl: String = "",
    val role: Role = Role.Master,
    val userTypeId: Int = 0,
)