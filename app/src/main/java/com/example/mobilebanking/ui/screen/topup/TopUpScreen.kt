package com.example.mobilebanking.ui.screen.topup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.ui.screen.auth.AuthViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebanking.data.model.Transaction
import com.example.mobilebanking.utils.getCurrentDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {

    val context = LocalContext.current

    val currentUser by viewModel.currentUserFlow.collectAsState(initial = null)
    val userList by viewModel.userListFlow.collectAsState(initial = emptyList())
    val currentUserObj = userList.find { it.username == currentUser }
    val balance = currentUserObj?.balance ?: 0

    var selectedEWallet by remember { mutableStateOf("Gopay") }
    val eWalletOptions = listOf("Gopay", "ShopeePay", "OVO")
    var expanded by remember { mutableStateOf(false) }

    var phoneNumber by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Top Up E-Wallet", style = MaterialTheme.typography.headlineMedium)

        // Dropdown E-Wallet
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedEWallet,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih E-Wallet") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                eWalletOptions.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedEWallet = label
                            expanded = false
                        }
                    )
                }
            }
        }

        // Input nomor telepon
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Nomor Telepon") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        // Input nominal
        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Nominal Top Up") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Input PIN
        OutlinedTextField(
            value = pinInput,
            onValueChange = { pinInput = it },
            label = { Text("PIN (Password)") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        // Tombol Submit
        Button(
            onClick = {
                val amount = amountInput.toIntOrNull() ?: 0

                when {
                    phoneNumber.isBlank() || amountInput.isBlank() || pinInput.isBlank() -> {
                        Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                    amount < 5000 -> {
                        Toast.makeText(context, "Minimal top up adalah Rp 5.000", Toast.LENGTH_SHORT).show()
                    }
                    pinInput != currentUserObj?.password -> {
                        Toast.makeText(context, "PIN salah", Toast.LENGTH_SHORT).show()
                    }
                    amount > balance -> {
                        Toast.makeText(context, "Saldo tidak mencukupi", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Update saldo
                        val newBalance = balance - amount
                        viewModel.updateBalance(newBalance)

                        // Simpan riwayat transaksi
                        val transaction = Transaction(
                            type = "Top Up E-Wallet (${selectedEWallet})",
                            amount = amount,
                            dateTime = getCurrentDateTime(),
                            recipient = phoneNumber,
                            status = "Dana Keluar"
                        )
                        viewModel.addTransaction(transaction)

                        Toast.makeText(context, "Top up berhasil", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // kembali ke halaman sebelumnya
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Top Up Sekarang")
        }
    }
}