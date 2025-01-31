package com.contractors.app.data.network.model

data class OwnPostsDTO(
    val success: Boolean = false,
    val data: List<OwnPostDTO> = listOf()
)