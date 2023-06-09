package com.adhibuchori.storyapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adhibuchori.storyapp.data.remote.repository.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun isLogin() = userRepository.isLogin().asLiveData()

    fun login(email: String, password: String) = userRepository.login(email, password).asLiveData()

    fun register(name: String, email: String, password: String) = userRepository.register(name, email, password).asLiveData()
}