package com.cantikka.utsmobile

import com.cantikka.utsmobile.User
import com.cantikka.utsmobile.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun register(user: User) {
        userDao.insert(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}