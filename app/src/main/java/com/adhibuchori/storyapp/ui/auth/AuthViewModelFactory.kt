package com.adhibuchori.storyapp.ui.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adhibuchori.storyapp.data.remote.repository.UserRepository
import com.adhibuchori.storyapp.di.Injection

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(Injection.provideUserRepository(dataStore))
        }.also { instance = it }
    }
}