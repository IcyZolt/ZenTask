package com.example.zentask

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

import com.example.zentask.TaskLogic.CreateTask
import com.example.zentask.TaskLogic.Task
import com.example.zentask.TaskLogic.TaskModification.OnTaskEdited
import com.example.zentask.TaskLogic.TaskModification.TaskModification
import com.example.zentask.TaskLogic.TaskStorage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksComposeScreen(drawerState: DrawerState) {
    val context = LocalContext.current
    var tasks by remember { mutableStateOf(loadActiveTasks(context)) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppTopBar("Tasks", drawerState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Greeting
            val username = remember { UserStorage.loadUsername(context) ?: "User" }
            val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val showGreeting = remember { prefs.getBoolean("showGreetingOnce", false) }

            if (showGreeting) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Hello $username! 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                LaunchedEffect(Unit) {
                    prefs.edit().putBoolean("showGreetingOnce", false).apply()
                }
            }

            // Task List
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.TaskAlt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tasks yet!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap + to create your first task",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tasks, key = { it.name }) { task ->
                        TaskCardCompose(
                            task = task,
                            onComplete = {
                                task.isArchived = true
                                TaskStorage.saveTasks(context, tasks)
                                tasks = loadActiveTasks(context)
                            },
                            onDelete = {
                                tasks = tasks.filter { it.name != task.name }
                                TaskStorage.saveTasks(context, tasks)
                            },
                            onModify = {
                                TaskModification.showEditDialog(
                                    context,
                                    task,
                                    object : OnTaskEdited{
                                    override fun onEdited(
                                        newName: String,
                                        newDate: String,
                                        newTime: String,
                                        newAmPm: String,
                                        newDesc: String) {

                                        // Mutate the existing task
                                        task.name = newName
                                        task.date = newDate
                                        task.time = newTime.toInt()
                                        task.ampm = newAmPm
                                        task.description = newDesc

                                        // Reassign tasks list to trigger Compose recomposition
                                        tasks = tasks.toList()

                                        TaskStorage.saveTasks(context, tasks)
                                    }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { taskName ->
                val newTask = Task(taskName, "", 0, "", "")
                tasks = tasks + newTask
                TaskStorage.saveTasks(context, tasks)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TaskCardCompose(
    task: Task,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    onModify: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (task.date.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = task.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (task.time > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatTime(task),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row {
                    IconButton(onClick = onComplete) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Complete Task",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Modify")},
                                onClick = {
                                    onModify()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    onDelete()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                },

                            )

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var taskName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Let's get something done!") },
        text = {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = { Text("Task name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (taskName.isNotBlank()) {
                        onAdd(taskName.trim())
                    }
                }
            ) {
                Text("I'M READY!")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("One sec...")
            }
        }
    )
}

private fun loadActiveTasks(context: Context): List<Task> {
    return TaskStorage.loadTasks(context).filter { !it.isArchived }
}

private fun formatTime(task: Task): String {
    if (task.time <= 0 || task.ampm.isNullOrEmpty()) {
        return ""
    }
    val hour = task.time / 100
    val minute = task.time % 100
    return String.format("%02d:%02d %s", hour, minute, task.ampm)
}