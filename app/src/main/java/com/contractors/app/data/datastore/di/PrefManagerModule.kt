package com.contractors.app.data.datastore.di

import android.content.Context
import com.contractors.app.data.datastore.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefManagerModule {
    @Singleton
    @Provides
    fun provideRefManager(
        @ApplicationContext app: Context
    ): PrefManager {
        return PrefManager(app)
    }
}
