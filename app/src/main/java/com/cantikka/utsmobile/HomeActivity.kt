package com.cantikka.utsmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cantikka.utsmobile.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()

        binding.buttonEdit.setOnClickListener {
            val intent = Intent(this@HomeActivity, UpdateActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            clearUserData()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI() // Ambil ulang data saat user kembali dari UpdateActivity
    }

    private fun clearUserData() {
        val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
        preferences.edit().clear().apply()

        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUI() {
        val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val userEmail = preferences.getString("user_email", null)

        Log.d("HomeActivity", "SharedPref Email: $userEmail")

        if (userEmail != null) {
            fetchUserData(userEmail)
        } else {
            displayUserDataNotFound()
        }
    }

    private fun fetchUserData(email: String) {
        lifecycleScope.launch {
            val user = userViewModel.getUserByEmail(email)
            if (user != null) {
                Log.d("HomeActivity", "User ditemukan: ${user.nama}")
                // Tampilkan data user ke UI
                binding.dataNama.text = user.nama
                binding.dataEmail.text = user.email
                binding.dataNoHp.text = user.noHp
                binding.dataAlamat.text = user.alamat

                // Simpan kembali data terbaru ke SharedPreferences
                val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
                with(preferences.edit()) {
                    putInt("user_id", user.id)
                    putString("user_email", user.email)
                    apply()
                }
            } else {
                Log.d("HomeActivity", "User tidak ditemukan di database")
                displayUserDataNotFound()
            }
        }
    }

    private fun displayUserDataNotFound() {
        binding.dataNama.text = "Nama kosong"
        binding.dataEmail.text = "Email kosong"
        binding.dataNoHp.text = "NoHp kosong"
        binding.dataAlamat.text = "Alamat kosong"
    }
}
