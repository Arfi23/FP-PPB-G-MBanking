package com.example.mobilebanking.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.ui.screen.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val currentUser by viewModel.currentUserFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("EFPE Bank") })
        }
    ) { paddingValues -> // <-- ini 'it' parameter padding yang diberikan oleh Scaffold,
                         // diperlukan karena Scaffold memiliki parameter lambda content: @Composable (PaddingValues) -> Unit
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // <-- penting!
                .padding(16.dp),         // padding tambahan opsional
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            // Text("Welcome to Mobile Banking", modifier = androidx.compose.ui.Modifier.padding(16.dp))

            Text(
                text = "Welcome, ${currentUser ?: "Guest"}",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(onClick = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }

    }
}