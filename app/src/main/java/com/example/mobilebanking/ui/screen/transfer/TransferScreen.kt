package com.example.mobilebanking.ui.screen.transfer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.data.model.Transaction
import com.example.mobilebanking.ui.screen.auth.AuthViewModel
import com.example.mobilebanking.utils.getCurrentDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current

    val bankList = listOf("BNI", "BRI", "Mandiri", "Muamalat", "BSI", "BCA")
    var selectedBank by remember { mutableStateOf(bankList.first()) }
    var expanded by remember { mutableStateOf(false) }

    var targetAccountNumber by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }

    val currentUser by viewModel.currentUserData.collectAsState(initial = null)
    val userList by viewModel.userListFlow.collectAsState(initial = emptyList())
    val balance = currentUser?.balance ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Transfer Antar Rekening", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(100.dp))

        // Dropdown Bank Tujuan
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedBank,
                onValueChange = {},
                readOnly = true,
                label = { Text("Bank Tujuan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                bankList.forEach { bank ->
                    DropdownMenuItem(
                        text = { Text(bank) },
                        onClick = {
                            selectedBank = bank
                            expanded = false
                        }
                    )
                }
            }
        }

        // Input Nomor Rekening Tujuan
        OutlinedTextField(
            value = targetAccountNumber,
            onValueChange = { targetAccountNumber = it },
            label = { Text("Nomor Rekening Tujuan") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Input Nominal Transfer
        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Nominal Transfer") },
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

        Button(
            onClick = {
                val amount = amountInput.toIntOrNull() ?: 0
                val recipientUser = userList.find { it.accountNumber == targetAccountNumber }
                val currentAccountNumber = currentUser?.accountNumber

                when {
                    targetAccountNumber.isBlank() || amountInput.isBlank() || pinInput.isBlank() -> {
                        Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                    targetAccountNumber == currentAccountNumber -> {
                        Toast.makeText(context, "Tidak bisa transfer ke rekening sendiri", Toast.LENGTH_SHORT).show()
                    }
                    recipientUser == null -> {
                        Toast.makeText(context, "Nomor rekening tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                    amount < 10_000 -> {
                        Toast.makeText(context, "Minimal transfer Rp 10.000", Toast.LENGTH_SHORT).show()
                    }
                    pinInput != currentUser?.password -> {
                        Toast.makeText(context, "PIN salah", Toast.LENGTH_SHORT).show()
                    }
                    amount > balance -> {
                        Toast.makeText(context, "Saldo tidak mencukupi", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Kurangi saldo pengirim
                        val newSenderBalance = balance - amount
                        viewModel.updateBalance(newSenderBalance)

                        // Tambah saldo penerima
                        viewModel.increaseBalance(recipientUser.username, recipientUser.balance + amount)

                        // Simpan transaksi pengirim
                        viewModel.addTransaction(
                            Transaction(
                                type = "Transfer ke ${targetAccountNumber} (${selectedBank})",
                                amount = amount,
                                dateTime = getCurrentDateTime(),
                                recipient = targetAccountNumber,
                                status = "Dana Keluar"
                            )
                        )

                        // Simpan transaksi penerima
                        viewModel.addTransactionForUser(
                            username = recipientUser.username,
                            transaction = Transaction(
                                type = "Transfer dari $currentAccountNumber (${selectedBank})",
                                amount = amount,
                                dateTime = getCurrentDateTime(),
                                recipient = targetAccountNumber,
                                status = "Dana Masuk"
                            )
                        )

                        Toast.makeText(context, "Transfer berhasil", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kirim Dana")
        }
    }
}
