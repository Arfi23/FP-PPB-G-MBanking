package com.example.mobilebanking.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.ui.screen.auth.*
import com.example.mobilebanking.ui.screen.home.HomeScreen
import com.example.mobilebanking.ui.screen.history.HistoryScreen
import com.example.mobilebanking.ui.screen.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // Bungkus semua halaman utama dengan BottomNavigation
        composable("main") {
            BottomNavWrapper(navController)
        }
    }
}

@Composable
fun BottomNavWrapper(navController: NavController) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = innerNavController) }
    ) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = innerNavController)
            }
            composable(BottomNavItem.History.route) {
                HistoryScreen(navController = innerNavController) // Buat placeholder
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(navController = innerNavController) // Buat placeholder
            }
        }
    }
}