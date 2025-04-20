package com.cantikka.utsmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cantikka.utsmobile.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi email dan password!", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val user = userViewModel.login(email, password)

                    if (user != null) {
                        // Menyimpan email ke SharedPreferences, bukan ID
                        val sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
                        sharedPref.edit().putString("user_email", user.email).apply()

                        // Berpindah ke HomeActivity setelah login berhasil
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

