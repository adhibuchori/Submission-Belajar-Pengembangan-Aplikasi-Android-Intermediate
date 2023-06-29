package com.adhibuchori.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adhibuchori.storyapp.data.remote.repository.StoryRepository
import com.adhibuchori.storyapp.data.remote.repository.UserRepository
import com.adhibuchori.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch
import com.adhibuchori.storyapp.data.Result

class MainViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _listStory = MutableLiveData<Result<List<Story>>>()
    val listStory : LiveData<Result<List<Story>>>
        get() = _listStory

    init {
        getAllStories()
    }

    fun getAllStories(){
        viewModelScope.launch {
            storyRepository.getAllStories().collect{
                _listStory.value = it
            }
        }
    }

    fun isLogin() = userRepository.isLogin().asLiveData()

    fun logout() = userRepository.logout().asLiveData()

}