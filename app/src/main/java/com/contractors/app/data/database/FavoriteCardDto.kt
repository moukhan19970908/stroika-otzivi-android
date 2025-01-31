package com.contractors.app.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.contractors.app.presentation.ui.model.Post


@Entity(tableName = "favorites")
data class FavoritePostDBO(
    @PrimaryKey val id: Int,
    @ColumnInfo("id_server") val idServer: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("rating") val rating: String,
    @ColumnInfo("address") val address: String,
    @ColumnInfo("distance") val distance: Double,
    @ColumnInfo("image") val image: String,
)

fun Post.toDBO(): FavoritePostDBO = FavoritePostDBO(
    id = id,
    idServer = this.id,
    title = title,
    address = address,
    rating = rating,
    distance = distance,
    image = this.get_first_image.image_path
)