package com.example.mobilebanking.utils

fun getCurrentDateTime(): String {
    val formatter = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return formatter.format(java.util.Date())
}
