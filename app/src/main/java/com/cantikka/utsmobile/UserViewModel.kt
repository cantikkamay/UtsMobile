package com.cantikka.utsmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun register(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            userRepository.register(user)
            onSuccess()
        }
    }

    suspend fun login(email: String, password: String): User? {
        return userRepository.login(email, password)
    }

    fun getAllUsers(onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = userRepository.getAllUsers()
            onResult(users)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun updateUser(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            onResult()
        }
    }
}

