package com.example.mobilebanking.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.mobilebanking.R
import com.example.mobilebanking.ui.navigation.BottomBar
import com.example.mobilebanking.ui.screen.auth.AuthViewModel

@Composable
fun ProfileScreen(viewModel: AuthViewModel = viewModel(), navController: NavController) {
    val currentUser by viewModel.currentUserData.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.userprofile),
            contentDescription = "Foto Profil",
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        currentUser?.let { user ->
            Text("Username: ${user.username}")
            Text("Account Number: ${user.accountNumber}")
            Text("Saldo: Rp ${user.balance}")
        }

        Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke bawah

        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
