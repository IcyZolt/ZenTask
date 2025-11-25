package com.example.zentask.TaskLogic;

public class Task {
    public String name;
    public String date;

    public int time;
    public String description;
    public boolean isArchived;

    public Task(String name, String date, int time, String description) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.description = description;
        this.isArchived = false;
    }
}
