package com.example.mobilebanking.ui.screen.qris

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRISScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("QRIS Payment") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Ini halaman QRIS")
        }
    }
}