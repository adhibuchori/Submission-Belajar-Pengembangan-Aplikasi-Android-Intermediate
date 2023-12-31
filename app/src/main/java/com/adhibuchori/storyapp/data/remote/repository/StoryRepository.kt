package com.adhibuchori.storyapp.data.remote.repository

import android.util.Log
import com.adhibuchori.storyapp.data.remote.response.Story
import com.adhibuchori.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.adhibuchori.storyapp.data.Result
import com.adhibuchori.storyapp.data.local.preference.UserPreference
import com.adhibuchori.storyapp.data.local.room.StoryDao
import com.adhibuchori.storyapp.data.remote.utils.reduceFileImage
import okhttp3.RequestBody.Companion.asRequestBody

class StoryRepository private constructor(private val apiService: ApiService, private val userPreference: UserPreference, private val storyDao: StoryDao){

    fun getAllStories(): Flow<Result<List<Story>>> = flow {
        emit(Result.Loading)
        try {
            userPreference.getToken().collect{
                if(it != null){
                    val response = apiService.getAllStories(it)
                    storyDao.clearStory()
                    for (story in response.listStory) {
                        storyDao.addStory(story)
                    }
                    emit(Result.Success(response.listStory))
                }
            }
        }catch (e: Exception){
            Log.d("StoryRepository", "getAllStories: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailStory(id: String): Flow<Result<Story>> = flow {
        emit(Result.Loading)
        try {
            userPreference.getToken().collect{
                if(it != null){
                    val response = apiService.getDetailStory(it, id)
                    emit(Result.Success(response.story))
                }
            }
        }catch (e: Exception){
            Log.d("StoryRepository", "getDetailStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(description: String, file: File): Flow<Result<String>> = flow{
        emit(Result.Loading)
        val reducedFile = reduceFileImage(file)
        val desc = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
        try {
            userPreference.getToken().collect{
                if(it != null){
                    val response = apiService.addStory(it, imageMultipart, desc)
                    emit(Result.Success(response.message))
                }
            }
        }catch (e: Exception){
            Log.d("StoryRepository", "getDetailStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            authPreferences: UserPreference,
            storyDao: StoryDao,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, authPreferences, storyDao)
            }.also { instance = it }
    }
}