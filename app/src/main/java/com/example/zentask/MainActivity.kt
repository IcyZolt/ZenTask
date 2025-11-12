package com.example.zentask

import android.content.Context.MODE_PRIVATE
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
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //preference for first run
        val prefs: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            //launch SettingsActivity for first run
            val intent = Intent(this, StartupPage::class.java)
            startActivity(intent)

            //mark first run as complete
            prefs.edit().putBoolean("isFirstRun", false).apply()
        }

        enableEdgeToEdge()
        setContent {
            ZenTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val prefs = LocalContext.current.getSharedPreferences("UserPrefs", MODE_PRIVATE)
    val userName = prefs.getString("username", "Android")
    Text(text = "Hello $userName!", modifier = modifier)

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZenTaskTheme {
        Greeting()
    }
}