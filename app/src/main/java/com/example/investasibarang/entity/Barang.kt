package com.example.investasibarang.entity

data class Barang(
    var id: String = "",
    var namaBarang: String = "",
    var merkBarang: String = "",
    var jumlah: Int = 0,
    var harga: Double = 0.0,
    var tanggalBeli: String = "",
    var status: String = "",
    var userId: String = ""
)
