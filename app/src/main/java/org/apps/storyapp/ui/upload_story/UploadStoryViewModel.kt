package org.apps.storyapp.ui.upload_story

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.api.ApiConfig
import org.apps.storyapp.network.response.FileUploadResponse
import org.apps.storyapp.reduceFileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadStoryViewModel(application: Application): AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _isFailed = MutableLiveData<Boolean>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(imageFile: File, desc: String, lat: Float, lon: Float) {
        val file = reduceFileImage(imageFile as File)

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val token = UserPreference(context).getUser().token

        val client = token?.let {
            ApiConfig.getApiService().uploadImage(
                "Bearer $it",
                imageMultipart,
                description,
                lat,
                lon
            )
        }

        _isLoading.value = true
        client?.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isFailed.value = response.body()?.error != false
                _isLoading.value = false
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isFailed.value = true
            }
        })
    }


}