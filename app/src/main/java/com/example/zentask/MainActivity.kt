package com.example.zentask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zentask.ui.theme.ZenTaskTheme

class MainActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("zentask")
        }
    }

    external fun stringFromJNI(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for first run - redirect to settings if needed
        val usernameFromFile = UserStorage.loadUsername(this)

        if (usernameFromFile.isNullOrEmpty()) {
            Log.d("MainActivity", "No saved username found, opening settings")
            startActivity(Intent(this, SettingsPage::class.java))
            finish()
            return
        } else {
            Log.d("MainActivity", "Welcome back, $usernameFromFile")
        }

        enableEdgeToEdge()

        // Launch the Compose navigation system
        setContent {
            ZenTaskTheme {
                NavigationManager()
            }
        }
    }
}