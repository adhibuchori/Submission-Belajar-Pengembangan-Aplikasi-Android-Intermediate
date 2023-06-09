package com.adhibuchori.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.adhibuchori.storyapp.data.local.preference.UserPreference
import com.adhibuchori.storyapp.data.local.room.StoryDatabase
import com.adhibuchori.storyapp.data.remote.repository.StoryRepository
import com.adhibuchori.storyapp.data.remote.repository.UserRepository
import com.adhibuchori.storyapp.data.remote.retrofit.ApiConfig

object Injection {

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val apiService = ApiConfig.getApiService()
        val authPreferences = UserPreference.getInstance(dataStore)
        return UserRepository.getInstance(apiService, authPreferences)
    }

    fun provideStoryRepository(context: Context, dataStore: DataStore<Preferences>): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(dataStore)
        val database = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(apiService, userPreference, database)
    }
}