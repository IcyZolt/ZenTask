// Path: app/src/main/java/com/example/zentask/TaskLogic/TaskModification.java
package com.example.zentask.TaskLogic.TaskModification;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import com.example.zentask.R;
import com.example.zentask.TaskLogic.Task;

public class TaskModification {

    public static void showEditDialog(Context context, Task task, OnTaskEdited callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_edit, null);

        EditText nameBox = view.findViewById(R.id.editName);
        EditText dateBox = view.findViewById(R.id.editDate);
        EditText descBox = view.findViewById(R.id.editDescription);

        nameBox.setText(task.name);
        dateBox.setText(task.date);
        descBox.setText(task.description);

        builder.setView(view);
        builder.setTitle("Edit Task");

        builder.setPositiveButton("Save", (d, w) -> {
            callback.onEdited(
                    nameBox.getText().toString(),
                    dateBox.getText().toString(),
                    descBox.getText().toString()
            );
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}