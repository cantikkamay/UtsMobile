package com.cantikka.utsmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.cantikka.utsmobile.databinding.ActivityUpdateBinding
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var userViewModel: UserViewModel
    private var currentUser: User? = null
    private var userId: Int = -1
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
        userId = preferences.getInt("user_id", -1)
        userEmail = preferences.getString("user_email", null)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userEmail?.let { email ->
            lifecycleScope.launch {
                val user = userViewModel.getUserByEmail(email)
                user?.let {
                    currentUser = it
                    binding.dataNama.setText(it.nama)
                    binding.dataEmail.setText(it.email)
                    binding.dataPassword.setText(it.password)
                    binding.dataNoHp.setText(it.noHp)
                    binding.dataAlamat.setText(it.alamat)
                }
            }
        }

        // Tombol update ditekan
        binding.button.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val nama = binding.dataNama.text.toString().trim()
        val email = binding.dataEmail.text.toString().trim()
        val password = binding.dataPassword.text.toString().trim()
        val noHp = binding.dataNoHp.text.toString().trim()
        val alamat = binding.dataAlamat.text.toString().trim()

        // Validasi input
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek user baru yang sudah diubah
        val updatedUser = currentUser?.copy(
            nama = nama,
            email = email,
            password = password,
            noHp = noHp,
            alamat = alamat
        )

        // Simpan ke database
        updatedUser?.let {
            userViewModel.updateUser(it) {
                // Perbarui SharedPreferences dengan data baru
                val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
                with(preferences.edit()) {
                    putInt("user_id", it.id)
                    putString("user_email", it.email)
                    apply()
                }

                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()

                // Kembali ke HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
