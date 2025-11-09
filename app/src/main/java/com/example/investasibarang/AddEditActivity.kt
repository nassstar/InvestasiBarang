package com.example.investasibarang

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.investasibarang.databinding.ActivityAddEditBinding
import com.example.investasibarang.entity.Barang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var firestore: FirebaseFirestore
    private var barangId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        barangId = intent.getStringExtra("barangId")

        // Jika mode edit
        if (barangId != null) {
            loadData()
        }

        binding.btnSimpan.setOnClickListener { simpanData() }
        binding.btnHapus.setOnClickListener { hapusData() }
    }

    private fun loadData() {
        firestore.collection("barang").document(barangId!!).get()
            .addOnSuccessListener { doc ->
                val barang = doc.toObject(Barang::class.java)
                barang?.let {
                    binding.etNamaBarang.setText(it.namaBarang)
                    binding.etMerkBarang.setText(it.merkBarang)
                    binding.etJumlah.setText(it.jumlah.toString())
                    binding.etHarga.setText(it.harga.toString())
                    binding.etTanggalBeli.setText(it.tanggalBeli)
                    binding.etStatus.setText(it.status)
                }
            }
    }

    private fun simpanData() {
        val nama = binding.etNamaBarang.text.toString().trim()
        val merk = binding.etMerkBarang.text.toString().trim()
        val jumlahText = binding.etJumlah.text.toString().trim()
        val hargaText = binding.etHarga.text.toString().trim()
        val tanggal = binding.etTanggalBeli.text.toString().trim()
        val status = binding.etStatus.text.toString().trim()

        // Validasi input
        if (nama.isEmpty() || merk.isEmpty() || jumlahText.isEmpty() || hargaText.isEmpty() || tanggal.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val barang = Barang(
            namaBarang = nama,
            merkBarang = merk,
            jumlah = jumlahText.toIntOrNull() ?: 0,
            harga = hargaText.toDoubleOrNull() ?: 0.0,
            tanggalBeli = tanggal,
            status = status,
            userId = uid
        )

        val docRef = if (barangId == null) {
            firestore.collection("barang").document()
        } else {
            firestore.collection("barang").document(barangId!!)
        }

        barang.id = docRef.id
        docRef.set(barang)
            .addOnSuccessListener {
                Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
                // Kembali ke halaman utama
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun hapusData() {
        barangId?.let {
            firestore.collection("barang").document(it).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show()
                    // Kembali ke halaman utama
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
}
