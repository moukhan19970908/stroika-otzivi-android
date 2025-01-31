package com.contractors.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [FavoritePostDBO::class], version = 1, exportSchema = false)
abstract class ContractorsDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: ContractorsDatabase? = null

        fun getDatabase(context: Context): ContractorsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContractorsDatabase::class.java,
                    "contractors_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}