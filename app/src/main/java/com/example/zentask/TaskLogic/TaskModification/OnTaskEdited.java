package com.example.zentask.TaskLogic.TaskModification;

public interface OnTaskEdited {
    void onEdited(String newName,
                  String newDate,
                  String newTime,
                  String newAmPm,
                  String newDesc);

}
