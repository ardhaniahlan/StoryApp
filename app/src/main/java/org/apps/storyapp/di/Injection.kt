package org.apps.storyapp.di

import android.content.Context
import org.apps.storyapp.data.StoryRepository
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}