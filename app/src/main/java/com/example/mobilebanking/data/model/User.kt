package com.example.mobilebanking.data.model

data class User(
    val username: String,
    val password: String,
    val accountNumber: String,
    val balance: Int
)