package com.example.bookify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Splash Screen Installation
        Thread.sleep(3000);
        installSplashScreen();

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)


        // Check if user is logged in
        if (isLoggedIn()) {
            navigateToHomeScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, MainApplicationActivity::class.java))
        finish()
    }

    private fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}
