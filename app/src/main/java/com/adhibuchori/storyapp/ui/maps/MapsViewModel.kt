package com.adhibuchori.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adhibuchori.storyapp.data.remote.repository.StoryRepository
import com.adhibuchori.storyapp.data.remote.response.Story
import com.adhibuchori.storyapp.data.remote.utils.story.Result
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<Result<List<Story>>>()
    val story: LiveData<Result<List<Story>>>
        get() = _story

    init {
        getAllStoriesWithLocation()
    }

    private fun getAllStoriesWithLocation() {
        viewModelScope.launch {
            storyRepository.getAllStoriesWithLocation().collect {
                _story.value = it
            }
        }
    }
}