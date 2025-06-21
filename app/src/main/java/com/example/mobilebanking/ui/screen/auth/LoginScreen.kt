package com.example.mobilebanking.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.loginUser(username, password) { success ->
                if (success) {
                    Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate("register")
        }) {
            Text("Belum punya akun? register")
        }
    }
}