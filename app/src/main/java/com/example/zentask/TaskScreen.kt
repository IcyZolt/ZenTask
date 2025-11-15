package com.example.zentask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

data class Task(
    val id: Int,
    val title: String,
    var isDone: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskManagerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp() {

    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var isAdding by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAdding = true }
            ) {
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
                                    id = tasks.size + 1,
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

            // Tasks List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = {
                            tasks = tasks.map {
                                if (it.id == task.id) it.copy(isDone = !it.isDone) else it
                            }
                        },
                        onDelete = {
                            tasks = tasks.filter { it.id != task.id }
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
    onToggle: () -> Unit,
    onDelete: () -> Unit
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
                modifier = Modifier.clickable { onToggle() }
            )

            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
