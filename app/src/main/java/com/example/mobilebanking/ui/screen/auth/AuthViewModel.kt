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
    val accountNumberFlow = userPrefs.getAccountNumber
    val balanceFlow = userPrefs.getBalance

    // Fungsi register dengan username + password
    fun registerUser(
        username: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val existingUsers = userPrefs.getUserList().first()
            val userExists = existingUsers.any { it.username == username }

            if (userExists) {
                onResult(false)
            } else {
                val accountNumber = generateAccountNumber()
                val initialBalance = 1_000_000

                val user = User(username, password)
                userPrefs.saveUser(user)
                userPrefs.setCurrentUser(username)
                userPrefs.saveAccountData(accountNumber, initialBalance)

                onResult(true)
            }
        }
    }

    // Fungsi login
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

    // Utilitas tambahan untuk pendaftaran manual
    fun saveUser(username: String, password: String) {
        viewModelScope.launch {
            val user = User(
                username = username,
                password = password
            )
            userPrefs.saveUser(user)
        }
    }

    fun saveAccountData(accountNumber: String, balance: Int) {
        viewModelScope.launch {
            userPrefs.saveAccountData(accountNumber, balance)
        }
    }

    // Fungsi membuat nomor rekening acak
    private fun generateAccountNumber(): String {
        val prefix = "987"
        val randomDigits = (1000000..9999999).random()
        return prefix + randomDigits
    }

}