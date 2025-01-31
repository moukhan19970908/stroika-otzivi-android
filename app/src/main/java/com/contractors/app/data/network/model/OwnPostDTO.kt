package com.contractors.app.data.network.model

import com.contractors.app.data.network.Image

data class OwnPostDTO(
    val id: Int = 1,
    val title: String = "",
    val description: String = "",
    val user_id: Int = 1,
    val latitude: String = "",
    val longitude: String = "",
    val status: String = "",
    val address: String = "",
    val rating: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val get_first_image: Image = Image(),
)
