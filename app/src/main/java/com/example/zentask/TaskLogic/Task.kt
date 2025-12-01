package com.example.zentask.TaskLogic

import java.util.UUID

data class Task(
    var name: String,
    var date: String,
    var time: Int,
    var ampm: String,
    var description: String,
    var isArchived: Boolean = false,
    val id: String = UUID.randomUUID().toString()
)