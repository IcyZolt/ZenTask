package com.example.zentask

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.zentask.TaskLogic.CreateTask
import com.example.zentask.TaskLogic.ArchivedTasks

enum class Screen {
    Tasks, Archive, Leaderboards, Settings
}

data class DrawerMenuItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationManager() {
    var currentScreen by remember { mutableStateOf(Screen.Tasks) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val menuItems = listOf(
        DrawerMenuItem("Tasks", Icons.Default.Task, Screen.Tasks),
        DrawerMenuItem("Archive", Icons.Default.Archive, Screen.Archive),
        DrawerMenuItem("Leaderboards", Icons.Default.Leaderboard, Screen.Leaderboards),
        DrawerMenuItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "ZenTask",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentScreen == item.screen,
                        onClick = {
                            scope.launch {
                                when (item.screen) {
                                    Screen.Tasks -> {
                                        // Launch Java Activity for Tasks
                                        currentScreen = Screen.Tasks
                                        drawerState.close()
                                        return@launch
                                    }
                                    Screen.Archive -> {
                                        // Launch Java Activity for Archive
                                        context.startActivity(Intent(context, ArchivedTasks::class.java))
                                        drawerState.close()
                                    }
                                    else -> {
                                        // For other screens, navigate within Compose
                                        if (currentScreen != item.screen) {
                                            currentScreen = item.screen
                                        }
                                        drawerState.close()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        when (currentScreen) {
            Screen.Tasks -> TasksComposeScreen(drawerState)
            Screen.Archive -> ArchiveLauncherScreen(drawerState)
            Screen.Leaderboards -> LeaderboardScreen(drawerState)
            Screen.Settings -> SettingsComposeScreen(drawerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

// Launcher screen that opens the Java Activity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksLauncherScreen(drawerState: DrawerState) {
    val context = LocalContext.current
    /*
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, CreateTask::class.java))
    }*/


    Scaffold(topBar = { AppTopBar("Tasks", drawerState) }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveLauncherScreen(drawerState: DrawerState) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, ArchivedTasks::class.java))
    }

    Scaffold(topBar = { AppTopBar("Archive", drawerState) }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(drawerState: DrawerState) {
    val context = LocalContext.current
    val expManager = remember { ExperienceManager(context) }
    val username = remember { UserStorage.loadUsername(context) ?: "User" }

    Scaffold(topBar = { AppTopBar("Leaderboards", drawerState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Your Progress", style = MaterialTheme.typography.headlineSmall)

            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Player: $username", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Level: ${expManager.getLevel()}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Experience: ${expManager.getExperience().toInt()} / ${expManager.getRequiredExp()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(progress = expManager.getProgressPercent(), modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Coming Soon: Global Leaderboards", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComposeScreen(drawerState: DrawerState) {
    val context = LocalContext.current

    Scaffold(topBar = { AppTopBar("Settings", drawerState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, SettingsPage::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Full Settings")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Quick Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "User: ${UserStorage.loadUsername(context) ?: "Not set"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
