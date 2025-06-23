package com.example.mobilebanking.ui.screen.qris

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.R
import com.example.mobilebanking.data.model.Transaction
import com.example.mobilebanking.ui.screen.auth.AuthViewModel
import com.example.mobilebanking.utils.getCurrentDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QRISScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUserData.collectAsState(initial = null)
    val balance = currentUser?.balance ?: 0

    var showSuccess by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var showQR by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (showSuccess) {
        // Simulasi halaman sukses
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            confirmButton = {
                TextButton(onClick = {
                    showSuccess = false
                    navController.popBackStack()
                }) {
                    Text("OK")
                }
            },
            title = { Text("Transaksi Berhasil") },
            text = { Text("Pembayaran menggunakan QRIS berhasil.") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("QRIS", style = MaterialTheme.typography.headlineMedium)

        // Tombol Scan QR Merchant
        Button(onClick = {
            if (balance < 10_000) {
                Toast.makeText(context, "Saldo tidak mencukupi", Toast.LENGTH_SHORT).show()
            } else {
                isScanning = true
                coroutineScope.launch {
                    Toast.makeText(context, "Memindai QR Merchant...", Toast.LENGTH_SHORT).show()
                    delay(8000L)

                    // Kurangi saldo
                    viewModel.updateBalance(balance - 10_000)

                    // Simpan transaksi
                    val transaction = Transaction(
                        type = "Pembayaran QRIS",
                        amount = 10_000,
                        dateTime = getCurrentDateTime(),
                        recipient = "Pembayaran QRIS",
                        status = "Dana Keluar"
                    )
                    viewModel.addTransaction(transaction)

                    showSuccess = true
                }
            }
        }) {
            Text("Scan QR Merchant")
        }

        if (isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scanqr), // <-- tambahkan gambar ini
                    contentDescription = "Scanning QR",
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        // Tombol Tampilkan QR Saya
        Button(onClick = {
            if (balance < 10_000) {
                Toast.makeText(context, "Saldo tidak mencukupi", Toast.LENGTH_SHORT).show()
            } else {
                showQR = true
                coroutineScope.launch {
                    delay(8000L)

                    // Kurangi saldo
                    viewModel.updateBalance(balance - 10_000)

                    // Simpan transaksi
                    val transaction = Transaction(
                        type = "Pembayaran QRIS (Customer Show QR)",
                        amount = 10_000,
                        dateTime = getCurrentDateTime(),
                        recipient = "Pembayaran QRIS",
                        status = "Dana Keluar"
                    )
                    viewModel.addTransaction(transaction)

                    showQR = false
                    showSuccess = true
                }
            }
        }) {
            Text("Tampilkan QR Saya")
        }

        if (showQR) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.qrcode), // Gambar QR simulasi
                contentDescription = "QR Saya",
                modifier = Modifier.size(200.dp)
            )
            Text("Tunjukkan QR ini kepada merchant", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
