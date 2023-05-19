package org.apps.storyapp.ui.story

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import androidx.paging.*
import org.apps.storyapp.data.StoryRepository
import org.apps.storyapp.network.api.ApiConfig
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.response.ListStoryItem
import org.apps.storyapp.network.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel (storyRepository: StoryRepository): ViewModel() {
    val getListStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getListStories().cachedIn(viewModelScope)
}

