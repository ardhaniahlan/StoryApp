package org.apps.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.api.ApiService
import org.apps.storyapp.network.response.ListStoryItem

class StoryRepository(private val pref: UserPreference, private val apiService: ApiService) {

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }

    fun getListStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(pref,apiService),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService)
            }
        ).liveData
    }
}