package com.example.investasibarang

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investasibarang.adapter.BarangAdapter
import com.example.investasibarang.databinding.ActivityMainBinding
import com.example.investasibarang.entity.Barang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: BarangAdapter
    private val listBarang = mutableListOf<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Jika belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        loadData()

        binding.btnTambah.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = BarangAdapter(listBarang) { barang ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("barangId", barang.id)
            startActivity(intent)
        }
        binding.rvBarang.layoutManager = LinearLayoutManager(this)
        binding.rvBarang.adapter = adapter
    }

    private fun loadData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("barang")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                listBarang.clear()
                snapshot?.forEach {
                    val barang = it.toObject(Barang::class.java)
                    listBarang.add(barang)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
