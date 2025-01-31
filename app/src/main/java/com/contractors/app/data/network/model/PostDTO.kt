package com.contractors.app.data.network.model

import com.contractors.app.data.network.Image
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.RealtorComment
import com.contractors.app.presentation.ui.model.Post

data class PostDTO(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val user_id: Int = 0,
    val latitude: String = "0.0",
    val longitude: String = "0.0",
    val status: String = "",
    val address: String = "",
    val rating: String = "",
    val created_at: String = "0",
    val updated_at: String = "0",
    val get_first_image: Image = Image(),
    val images: List<Image> = listOf(),
    val master_comments: List<MasterComment> = emptyList(),
    val rieltor_comments: List<RealtorComment> = emptyList(),
    val distance: Double = -1.0,
)


fun Post.toPostDTO() = PostDTO(
    id = id,
    title = title,
    description = description,
    user_id = user_id,
    latitude = latitude,
    longitude = longitude,
    status = status,
    address = address,
    rating = rating,
    created_at = created_at,
    updated_at = updated_at,
    get_first_image = get_first_image,
    images = images,
    master_comments = master_comments,
    rieltor_comments = rieltor_comments,
    distance = distance
)

