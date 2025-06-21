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

    val currentUserData: Flow<User?> = currentUserFlow.flatMapLatest { username ->
        flow {
            emit(userPrefs.getUserList().first().find { it.username == username })
        }
    }

    // Fungsi register dengan username + password
    fun registerUser(
        username: String,
        password: String,
        accountNumber: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val existingUsers = userPrefs.getUserList().first()
            val userExists = existingUsers.any { it.username == username }

            if (userExists) {
                onResult(false)
            } else {
                val randomBalance = (1_000_000..5_000_000).random()
                val newUser = User(username, password, accountNumber, randomBalance)
                userPrefs.saveUser(newUser)
                onResult(true)
            }
        }
    }

    // Fungsi login
    fun loginUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val users = userPrefs.getUserList().first()
            val user = users.find { it.username == username && it.password == password }
            if (user != null) {
                userPrefs.setCurrentUser(username)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.clearCurrentUser()
        }
    }

}