package com.example.zentask.TaskLogic;

public class Task {
    public String name;
    public String date;

    public int time;
    
    public String ampm;
    public String description;
    public boolean isArchived;

    public Task(String name, String date, int time, String ampm, String desc) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.ampm = ampm;
        this.description = desc;
        this.isArchived = false;
    }
}
