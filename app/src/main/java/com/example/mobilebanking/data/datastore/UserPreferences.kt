package com.example.mobilebanking.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mobilebanking.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    private val userListKey = stringPreferencesKey("user_list")
    private val currentUserKey = stringPreferencesKey("current_user")
    private val gson = Gson()

    // Ambil seluruh daftar user
    fun getUserList(): Flow<List<User>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[userListKey]
            if (json.isNullOrEmpty()) emptyList()
            else {
                val type = object : TypeToken<List<User>>() {}.type
                gson.fromJson(json, type)
            }
        }
    }

    // Ambil satu kali daftar user (untuk keperluan simpan/update)
    private suspend fun getUserListOnce(): List<User> {
        val json = context.dataStore.data.map { it[userListKey] ?: "" }.first()
        if (json.isEmpty()) return emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    // Simpan user baru
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            val currentList = getUserListOnce()
            val newList = currentList.toMutableList().apply {
                add(user)
            }
            preferences[userListKey] = gson.toJson(newList)
        }
    }

    // Ambil username yang sedang login
    fun getCurrentUser(): Flow<String?> {
        return context.dataStore.data.map { it[currentUserKey] }
    }

    // Simpan username yang sedang login
    suspend fun setCurrentUser(username: String) {
        context.dataStore.edit { prefs ->
            prefs[currentUserKey] = username
        }
    }

    // Hapus current user
    suspend fun clearCurrentUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(currentUserKey)
        }
    }

    // Ambil objek User dari username yang sedang login
    suspend fun getCurrentUserObject(): User? {
        val currentUsername = getCurrentUser().first()
        val userList = getUserListOnce()
        return userList.find { it.username == currentUsername }
    }

    // Tambahan (opsional): Update saldo user
    suspend fun updateUserBalance(username: String, newBalance: Int) {
        context.dataStore.edit { prefs ->
            val userList = getUserListOnce().toMutableList()
            val index = userList.indexOfFirst { it.username == username }
            if (index != -1) {
                val updatedUser = userList[index].copy(balance = newBalance)
                userList[index] = updatedUser
                prefs[userListKey] = gson.toJson(userList)
            }
        }
    }

}