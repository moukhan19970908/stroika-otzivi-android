package com.contractors.app.data.network.model

data class GetProfileDTO(
    val user: List<User>? = emptyList(),
)