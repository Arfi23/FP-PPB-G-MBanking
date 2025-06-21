package com.example.mobilebanking.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Beranda", Icons.Default.Home)
    object History : BottomNavItem("history", "Riwayat", Icons.Default.History)
    object Profile : BottomNavItem("profile", "Profil", Icons.Default.Person)
}