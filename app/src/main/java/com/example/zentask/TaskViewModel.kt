package com.example.zentask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.zentask.data.TaskRepository
import com.example.zentask.model.Task

class TaskViewModel : ViewModel() {

    private val repo = TaskRepository()

    var tasks by mutableStateOf(repo.getTasks())
        private set

    var isAdding by mutableStateOf(false)
        private set

    var newTaskTitle by mutableStateOf("")

    fun onAddButtonClicked() {
        isAdding = true
    }

    fun updateTaskTitle(title: String) {
        newTaskTitle = title
    }

    fun submitTask() {
        if (newTaskTitle.isNotBlank()) {
            repo.addTask(newTaskTitle)
            tasks = repo.getTasks().toList()  // triggers recomposition
            newTaskTitle = ""
            isAdding = false  // hides TextField **after task is added**
        }
    }


    fun removeTask(task: Task) {
        repo.removeTask(task)
        tasks = repo.getTasks().toList()
    }
}
