package com.example.zentask

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zentask.ui.theme.ZenTaskTheme
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

<<<<<<< Updated upstream
=======
    //C++ function (stringFromJNI) bridge to Kotlin
    companion object {
        init {
            System.loadLibrary("zentask")
        }
    }

    //native link JNI bridge
    external fun stringFromJNI(): String


>>>>>>> Stashed changes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //preference for first run
        val prefs: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usernameFromFile = UserStorage.loadUsername(this)

        if (usernameFromFile.isNullOrEmpty()) {
            //launch SettingsActivity for first run
            Log.d("MainActivity", "No saved username found, opening setting")
            startActivity(Intent(this, SettingsPage::class.java))
            finish()//stop mainactivity still user setup finished
            return // stop executing until user comes back
        } else {
            Log.d("MainActivity", "Welcome back")
        }

        enableEdgeToEdge()
        setContent {
            ZenTaskTheme {
<<<<<<< Updated upstream
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
=======

                val cppMessage = stringFromJNI()
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                //android.util.Log.d("PrefsCheck", "Username = ${prefs.getString("username", "none")}")

                val username = usernameFromFile ?: "User"
                val showGreeting = prefs.getBoolean("showGreetingOnce", true)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        if (showGreeting) {
                            Text(
                                text = "Hello $username",
                                style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            prefs.edit().putBoolean("showGreetingOnce", false).apply()
                        }
                        Text(
                            text= "$cppMessage",
                            style=androidx.compose.material3.MaterialTheme.typography.bodyLarge
                        )
                    }

>>>>>>> Stashed changes
                }
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier, name: String) {
        val prefs = LocalContext.current.getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userName = prefs.getString("username", name)
        Text(text = "Hello $userName!", modifier = modifier)

    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ZenTaskTheme {
            Greeting(name = "")
        }
    }
}