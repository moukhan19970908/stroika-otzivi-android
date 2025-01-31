package com.contractors.app.data.database.di

import android.content.Context
import com.contractors.app.data.database.ContractorsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): ContractorsDatabase {
        return ContractorsDatabase.getDatabase(context)
    }
}
