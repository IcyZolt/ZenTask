// Path: app/src/main/java/com/example/zentask/TaskLogic/TaskModification.java
package com.example.zentask.TaskLogic.TaskModification;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

        EditText hourBox = view.findViewById(R.id.editHour);
        EditText minuteBox = view.findViewById(R.id.editMinute);

        Spinner AmPmBox = view.findViewById(R.id.editAmPm);

        EditText descBox = view.findViewById(R.id.editDescription);

        int hour = task.time /100;
        int minute = task.time %100;

        nameBox.setText(task.name);
        dateBox.setText(task.date);
        hourBox.setText(String.valueOf(hour));
        minuteBox.setText(String.valueOf(minute));

        descBox.setText(task.description);

        builder.setView(view);
        builder.setTitle("Edit Task");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                new String[]{"AM", "PM"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AmPmBox.setAdapter(adapter);

        AmPmBox.setSelection(task.ampm != null && task.ampm.equals("PM") ? 1 : 0);


        builder.setPositiveButton("Save", (d, w) -> {

            String newName = nameBox.getText().toString();
            String newDate = dateBox.getText().toString();

            String newHour = hourBox.getText().toString();
            String newMinute = minuteBox.getText().toString();

            int hourNew = Integer.parseInt(newHour);
            int minNew = Integer.parseInt(newMinute);
            int combinedTime = hourNew * 100 + minNew;

            String ampm = AmPmBox.getSelectedItem().toString();
            String newDesc = descBox.getText().toString();

            //normalize date
            String normalizedDate = DateNormalizeHelper.normalizeDate(newDate);

            callback.onEdited(
                    newName,
                    normalizedDate,
                    String.valueOf(combinedTime),
                    ampm,
                    newDesc
            );
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}