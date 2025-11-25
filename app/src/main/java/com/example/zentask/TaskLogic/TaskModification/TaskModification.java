// Path: app/src/main/java/com/example/zentask/TaskLogic/TaskModification.java
package com.example.zentask.TaskLogic.TaskModification;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import com.example.zentask.R;
import com.example.zentask.TaskLogic.Task;
import com.example.zentask.TaskLogic.DateNormalizeHelper;

public class TaskModification {

    public static void showEditDialog(Context context, Task task, OnTaskEdited callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_edit, null);

        EditText nameBox = view.findViewById(R.id.editName);
        EditText dateBox = view.findViewById(R.id.editDate);
        EditText timeBox = view.findViewById(R.id.editTime);
        EditText descBox = view.findViewById(R.id.editDescription);

        nameBox.setText(task.name);
        dateBox.setText(task.date);
        timeBox.setInteger(task.time);
        descBox.setText(task.description);

        builder.setView(view);
        builder.setTitle("Edit Task");

        builder.setPositiveButton("Save", (d, w) -> {

            String newName = nameBox.getText().toString();
            String newDate = dateBox.getText().toString();
            String newDesc = descBox.getText().toString();

            String normalizedDate = DateNormalizeHelper.normalizeDate(newDate);

            callback.onEdited(
                    newName,
                    normalizedDate,
                    newDesc
            );
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}