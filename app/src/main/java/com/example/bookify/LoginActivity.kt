package com.example.bookify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var overlay: FrameLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var linkSignUpTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        usernameEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById<Button>(R.id.buttonLogin)
        linkSignUpTextView = findViewById(R.id.linkSignUp)

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        loginButton.setOnClickListener {
            login()
        }

        // Check if user is already logged in
        if (isLoggedIn()) {
            navigateToHomeScreen()
        }

        linkSignUpTextView.setOnClickListener {
            navigateToSignUpScreen()
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (validateInputs(username,password)) {
            showLoading(true)
            ApiService.login(username, password) { success ->
                runOnUiThread {
                    showLoading(false)
                    if (success) {
                        saveLoginInfo(username, true)
                        navigateToHomeScreen()
                    } else {
                        showToast("Invalid credentials. Please try again.")
                    }
                }
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Please fill in all fields")
            return false
        }

        return true
    }

    private fun saveLoginInfo(username: String, isLoggedIn: Boolean) {
        sharedPreferences.edit().apply {
            putString("username", username)
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun showLoading(isLoading: Boolean) {
        overlay = findViewById(R.id.overlay)
        overlay.visibility = if (isLoading) FrameLayout.VISIBLE else FrameLayout.GONE
        loginButton.isEnabled = !isLoading
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, MainApplicationActivity::class.java))
        finish()
    }

    private fun navigateToSignUpScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
