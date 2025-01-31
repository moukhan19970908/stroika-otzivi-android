package com.contractors.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getListFavoritesPostDBO(): List<FavoritePostDBO>

    @Query("SELECT * FROM favorites LIMIT 4")
    fun getFourPostFavoritesDBO(): List<FavoritePostDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePost(vararg users: FavoritePostDBO)

    @Query("DELETE FROM favorites WHERE id_server = :idServer")
    fun deleteFavoritePostById(idServer: Int)
}