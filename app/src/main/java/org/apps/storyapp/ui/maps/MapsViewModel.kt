package org.apps.storyapp.ui.maps

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.api.ApiConfig
import org.apps.storyapp.network.response.ListStoryItem
import org.apps.storyapp.network.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(application: Application): AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _mapsStories = MutableLiveData<List<ListStoryItem>>()
    val mapStories: LiveData<List<ListStoryItem>> = _mapsStories

    init {
        getMapsStories()
    }

    fun getMapsStories(){
        val token = UserPreference(context).getUser().token

        val client = token?.let {
            ApiConfig.getApiService().getStoriesLocation("Bearer $it")
        }

        client?.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.body()?.error == false) {
                    _mapsStories.value = response.body()?.listStory
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }
        })
    }
}