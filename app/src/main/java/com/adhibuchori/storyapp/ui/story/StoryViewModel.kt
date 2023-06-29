package com.adhibuchori.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adhibuchori.storyapp.data.remote.repository.StoryRepository
import com.adhibuchori.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch
import com.adhibuchori.storyapp.data.Result
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<Result<Story>>()
    val story: LiveData<Result<Story>>
        get() = _story

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            storyRepository.getDetailStory(id).collect {
                _story.value = it
            }
        }
    }

    fun addStory(description: String, file: File) =
        storyRepository.addStory(description, file).asLiveData()
}