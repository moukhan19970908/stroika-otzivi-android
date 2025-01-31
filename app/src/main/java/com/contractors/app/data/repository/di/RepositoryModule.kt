package com.contractors.app.data.repository.di

import com.contractors.app.data.database.ContractorsDatabase
import com.contractors.app.data.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAppRepository(
        apiClient: HttpClient,
        database: ContractorsDatabase,
    ): AppRepository = AppRepository(
        apiClient = apiClient,
        database = database
    )

}