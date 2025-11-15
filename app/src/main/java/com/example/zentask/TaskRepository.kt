package com.example.zentask.data

import com.example.zentask.model.Task

class TaskRepository {

    private val tasks = mutableListOf<Task>()

    fun getTasks(): List<Task> = tasks

    fun addTask(title: String) {
        val newTask = Task(id = tasks.size + 1, title = title)
        tasks.add(newTask)
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
    }
}

