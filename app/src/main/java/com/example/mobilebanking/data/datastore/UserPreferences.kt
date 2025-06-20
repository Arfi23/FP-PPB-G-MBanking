package com.example.mobilebanking.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            val currentList = getUserListOnce()
            val newList = currentList.toMutableList().apply {
                add(user)
            }
            preferences[userListKey] = gson.toJson(newList)
        }
    }

    private suspend fun getUserListOnce(): List<User> {
        val json = context.dataStore.data.map { it[userListKey] ?: "" }.first()
        if (json.isEmpty()) return emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getCurrentUser(): Flow<String?> {
        return context.dataStore.data.map { it[currentUserKey] }
    }

    suspend fun setCurrentUser(username: String) {
        context.dataStore.edit { prefs ->
            prefs[currentUserKey] = username
        }
    }

    suspend fun clearCurrentUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(currentUserKey)
        }
    }
}