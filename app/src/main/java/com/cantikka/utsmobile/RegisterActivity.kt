package com.cantikka.utsmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cantikka.utsmobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val nama = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val noHp = binding.editTextPhone.text.toString().trim()
            val alamat = binding.editTextAddress.text.toString().trim()

            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua kolom!", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(
                    nama = nama,
                    email = email,
                    password = password,
                    noHp = noHp,
                    alamat = alamat
                )

                userViewModel.register(user) {
                    runOnUiThread {
                        Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                    }

                    userViewModel.getAllUsers { userList ->
                        userList.forEach { user ->
                            Log.d(
                                "USER_LIST",
                                "User ID: ${user.id}, Nama: ${user.nama}, Email: ${user.email}"
                            )
                        }
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
