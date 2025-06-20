package com.example.mobilebanking.ui.screen.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilebanking.data.datastore.UserPreferences
import com.example.mobilebanking.data.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferences(application)
    val userListFlow: Flow<List<User>> = userPrefs.getUserList()
    val currentUserFlow: Flow<String?> = userPrefs.getCurrentUser()

    fun registerUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingUsers = userPrefs.getUserList().first()
            val userExists = existingUsers.any { it.username == username }

            if (userExists) {
                onResult(false)
            } else {
                userPrefs.saveUser(User(username, password))
                onResult(true)
            }
        }
    }

    fun loginUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingUsers = userPrefs.getUserList().first()
            val isValid = existingUsers.any { it.username == username && it.password == password }
            if (isValid) {
                userPrefs.setCurrentUser(username) // <-- Simpan current login
            }
            onResult(isValid)
        }
    }

    fun setLoggedInUser(username: String) {
        viewModelScope.launch {
            userPrefs.setCurrentUser(username)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.clearCurrentUser()
        }
    }
}