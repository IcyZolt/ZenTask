package com.example.zentask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Task data class
data class Task(
    val id: Int,
    val title: String,
    var isDone: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(drawerState: DrawerState) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var isAdding by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar("Tasks", drawerState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { isAdding = true }) {
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
            // Add Task Field
            if (isAdding) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("New Task") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            if (text.isNotBlank()) {
                                tasks = tasks + Task(
                                    id = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
                                    title = text
                                )
                                text = ""
                                isAdding = false
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Task List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { t ->
                            tasks = tasks.map { if (it.id == t.id) it.copy(isDone = !it.isDone) else it }
                        },
                        onDelete = { t ->
                            tasks = tasks.filter { it.id != t.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onToggle: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.title,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isDone) Color.Gray else Color.Unspecified,
                modifier = Modifier.clickable { onToggle(task) }
            )

            IconButton(onClick = { onDelete(task) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
