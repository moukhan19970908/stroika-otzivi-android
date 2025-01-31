package com.contractors.app.presentation.ui.model

import com.contractors.app.data.database.FavoritePostDBO
import com.contractors.app.data.network.Image
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.model.PostDTO
import com.contractors.app.data.network.RealtorComment
import com.contractors.app.data.network.model.OwnPostDTO

data class Post(
//    the same as id object from server
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
    val isFavorite: Boolean,
)


fun PostDTO.toPost(isFavorite: Boolean) = Post(
    id = id,
    isFavorite = isFavorite,
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
    images = images ?: emptyList(),
    master_comments = master_comments ?: emptyList(),
    rieltor_comments = rieltor_comments ?: emptyList(),
    distance = distance,
    )

fun OwnPostDTO.toPost(id: Int, isFavorite: Boolean?): Post = Post(
    id = this.id,
    isFavorite = isFavorite ?: false,
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
)

fun FavoritePostDBO.toPost(): Post = Post(
    id = this.idServer,
    isFavorite = true,
    title = title,
    address = address,
    get_first_image = Image(
        image_path = this.image,
    ),
    rating = rating,
    distance = distance,
)

