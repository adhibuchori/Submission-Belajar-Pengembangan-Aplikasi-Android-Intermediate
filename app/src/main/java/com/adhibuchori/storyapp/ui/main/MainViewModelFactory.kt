package com.adhibuchori.storyapp.ui.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adhibuchori.storyapp.data.remote.repository.StoryRepository
import com.adhibuchori.storyapp.data.remote.repository.UserRepository
import com.adhibuchori.storyapp.di.Injection

class MainViewModelFactory(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): MainViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(
                    Injection.provideStoryRepository(
                        context,
                        dataStore
                    ), Injection.provideUserRepository(dataStore)
                )
            }.also { instance = it }
    }
}