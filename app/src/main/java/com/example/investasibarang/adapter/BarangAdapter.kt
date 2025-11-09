package com.example.investasibarang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.investasibarang.databinding.ItemBarangBinding
import com.example.investasibarang.entity.Barang

class BarangAdapter(
    private val list: MutableList<Barang>,
    private val onClick: (Barang) -> Unit
) : RecyclerView.Adapter<BarangAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBarangBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barang = list[position]
        with(holder.binding) {
            tvNamaBarang.text = barang.namaBarang
            tvMerkBarang.text = "Merk: ${barang.merkBarang}"
            tvJumlahBarang.text = "Jumlah: ${barang.jumlah}"
            tvHargaBarang.text = "Harga: Rp ${barang.harga}"
            tvTanggalBeli.text = "Tanggal Beli: ${barang.tanggalBeli}"
            tvStatusBarang.text = "Status: ${barang.status}"

            root.setOnClickListener { onClick(barang) }
        }
    }
}
