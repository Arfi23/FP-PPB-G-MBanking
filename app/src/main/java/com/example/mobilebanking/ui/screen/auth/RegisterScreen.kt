package com.example.mobilebanking.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = accountNumber,
            onValueChange = { accountNumber = it },
            label = { Text("Nomor Rekening") },
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

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (username.isBlank() || password.isBlank() || accountNumber.isBlank()) {
                Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.registerUser(username, password, accountNumber) { result ->
                    if (result) {
                        Toast.makeText(context, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }) {
            Text("Register")
        }

    }
}
