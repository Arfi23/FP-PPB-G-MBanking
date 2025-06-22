package com.example.mobilebanking.data.model

data class Transaction(
    val type: String,         // Transfer, Top Up E-Wallet, QRIS
    val amount: Int,          // Nominal transaksi
    val dateTime: String,     // Format: "dd/MM/yyyy HH:mm"
    val recipient: String,    // No rekening / No telepon / "Pembayaran QRIS"
    val status: String        // "Dana Masuk" atau "Dana Keluar"
)
