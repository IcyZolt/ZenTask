package com.example.zentask

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Define all screens
enum class Screen {
    Tasks, Archive, Leaderboards, Settings
}

// Drawer menu item
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
                    text = "Menu",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentScreen == item.screen,
                        onClick = {
                            scope.launch {
                                if (currentScreen != item.screen) {
                                    currentScreen = item.screen
                                }
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        when (currentScreen) {
            Screen.Tasks -> TasksScreen(drawerState)
            Screen.Archive -> ArchiveScreen(drawerState)
            Screen.Leaderboards -> LeaderboardScreen(drawerState)
            Screen.Settings -> SettingsScreen(drawerState)
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

/* ------------------- Placeholder Screens ------------------- */
@Composable
fun ArchiveScreen(drawerState: DrawerState) {
    Scaffold(topBar = { AppTopBar("Archive", drawerState) }) { padding ->
        Text("Archive Screen", Modifier.padding(padding))
    }
}

@Composable
fun LeaderboardScreen(drawerState: DrawerState) {
    Scaffold(topBar = { AppTopBar("Leaderboards", drawerState) }) { padding ->
        Text("Leaderboards Screen", Modifier.padding(padding))
    }
}

@Composable
fun SettingsScreen(drawerState: DrawerState) {
    Scaffold(topBar = { AppTopBar("Settings", drawerState) }) { padding ->
        Text("Settings Screen", Modifier.padding(padding))
    }
}
