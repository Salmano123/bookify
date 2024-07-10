package com.example.bookify

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var overlay: FrameLayout
    private lateinit var linkSignInTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var imageNameTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var base64Image: String? = null
    private val CAMERA_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        linkSignInTextView = findViewById(R.id.linkSignIn)
        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        emailEditText = findViewById(R.id.editTextEmail)
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber)
        imageNameTextView = findViewById(R.id.textViewImageName)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        val openCameraButton = findViewById<Button>(R.id.buttonOpenCamera)

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        registerButton.setOnClickListener {
            register()
        }

        openCameraButton.setOnClickListener {
            openCamera()
        }

        linkSignInTextView.setOnClickListener {
            navigateToSignInScreen()
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            showToast("Camera permission denied")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            base64Image = convertToBase64(imageBitmap)
            imageNameTextView.text = "Image Selected"
        }
    }

    private fun convertToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun register() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        if (validateInputs(username, password, confirmPassword, email, phoneNumber)) {
            showLoading(true)
            ApiService.register(username, password, email, phoneNumber, base64Image) { success ->
                showLoading(false)
                if (success) {
                    saveLoginInfo(username, true)
                    showToast("Registration successful")
                    startActivity(Intent(this, MainApplicationActivity::class.java))
                    finish()
                } else {
                    showToast("Registration failed")
                }
            }
        }
    }

    private fun validateInputs(username: String, password: String, confirmPassword: String, email: String, phoneNumber: String): Boolean {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            showToast("Please fill in all fields")
            return false
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        overlay = findViewById(R.id.overlay)
        overlay.visibility = if (isLoading) FrameLayout.VISIBLE else FrameLayout.GONE
    }

    private fun navigateToSignInScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}