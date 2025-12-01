package com.example.zentask

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.zentask.TaskLogic.Task
import com.example.zentask.TaskLogic.TaskModification.OnTaskEdited
import com.example.zentask.TaskLogic.TaskModification.TaskModification
import com.example.zentask.TaskLogic.TaskStorage
import androidx.compose.foundation.combinedClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksComposeScreen(drawerState: DrawerState) {
    val context = LocalContext.current
    var tasks by remember { mutableStateOf(loadActiveTasks(context)) }
    var showAddDialog by remember { mutableStateOf(false) }

    val expManager = remember { ExperienceManager(context) }

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
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No tasks yet!", style = MaterialTheme.typography.titleLarge)
                        Text("Tap + to create your first task", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tasks, key = { it.id }) { task ->
                        TaskCardCompose(
                            task = task,
                            onComplete = {
                                // Complete task: award experience and archive
                                expManager.completeTask(task.date)

                                val updatedTasks = tasks.map {
                                    if (it.id == task.id) it.copy(isArchived = true) else it
                                }

                                TaskStorage.saveTasks(context, updatedTasks)
                                tasks = updatedTasks.filter { !it.isArchived }
                            },
                            onDelete = {
                                val updatedTasks = tasks.filter { it.id != task.id }
                                TaskStorage.saveTasks(context, updatedTasks)
                                tasks = updatedTasks
                            },
                            onModify = {
                                TaskModification.showEditDialog(
                                    context,
                                    task,
                                    object : OnTaskEdited {
                                        override fun onEdited(
                                            newName: String,
                                            newDate: String,
                                            newTime: String,
                                            newAmPm: String,
                                            newDesc: String
                                        ) {
                                            val updated = task.copy(
                                                name = newName,
                                                date = newDate,
                                                time = newTime.toInt(),
                                                ampm = newAmPm,
                                                description = newDesc
                                            )
                                            val updatedTasks = tasks.map { if (it.id == task.id) updated else it }
                                            TaskStorage.saveTasks(context, updatedTasks)
                                            tasks = updatedTasks.filter { !it.isArchived }
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

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { taskName ->
                val newTask = Task(taskName, "", 0, "", "")
                val updatedTasks = tasks + newTask
                TaskStorage.saveTasks(context, updatedTasks)
                tasks = updatedTasks
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
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { showMenu = true }
            ),
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
                    Text(task.name, style = MaterialTheme.typography.titleMedium)
                    if (task.date.isNotEmpty()) {
                        Text(task.date, style = MaterialTheme.typography.bodySmall)
                    }
                    if (task.time > 0 && task.ampm.isNotEmpty()) {
                        Text(formatTime(task), style = MaterialTheme.typography.bodySmall)
                    }
                }

                Row {
                    IconButton(onClick = onComplete) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Complete Task")
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Modify") },
                                onClick = { onModify(); showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = { onDelete(); showMenu = false }
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

private fun loadActiveTasks(context: Context): List<Task> =
    TaskStorage.loadTasks(context).filter { !it.isArchived }

private fun formatTime(task: Task): String {
    val hour = task.time / 100
    val minute = task.time % 100
    return "%02d:%02d %s".format(hour, minute, task.ampm)
}
