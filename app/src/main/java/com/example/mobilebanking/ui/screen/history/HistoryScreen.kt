package com.example.mobilebanking.ui.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import com.example.mobilebanking.ui.navigation.BottomBar
import com.example.mobilebanking.ui.screen.auth.AuthViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun HistoryScreen(viewModel: AuthViewModel = viewModel()) {
    val transactions by viewModel.transactionFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Riwayat Transaksi",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada transaksi")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(transactions.reversed()) { transaction ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (transaction.status == "Dana Masuk")
                                Color(0xFFDFF5E1) else Color(0xFFFFEBEB)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(transaction.type, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tanggal: ${transaction.dateTime}")
                            Text(
                                text = "Penerima: " + when {
                                    transaction.type.contains("QRIS", ignoreCase = true) -> "QRIS Merchant"
                                    else -> transaction.recipient
                                }
                            )
                            Text("Jumlah: Rp ${transaction.amount}")
                            Text("Status: ${transaction.status}")
                        }
                    }
                }
            }
        }
    }
}
