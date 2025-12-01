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

        int hour = task.getTime() /100;
        int minute = task.getTime() %100;

        nameBox.setText(task.getName());
        dateBox.setText(task.getDate());
        hourBox.setText(String.valueOf(hour));
        minuteBox.setText(String.valueOf(minute));

        descBox.setText(task.getDescription());

        builder.setView(view);
        builder.setTitle("Edit Task");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                new String[]{"AM", "PM"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AmPmBox.setAdapter(adapter);

        AmPmBox.setSelection(task.getAmpm() != null && task.getAmpm().equals("PM") ? 1 : 0);

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            String newName = nameBox.getText().toString();
            String newDate = dateBox.getText().toString();
            String newHour = hourBox.getText().toString();
            String newMinute = minuteBox.getText().toString();

            if(newHour.isEmpty() || newMinute.isEmpty()) {
                hourBox.setError("Invalid Time");
                minuteBox.setError("Invalid Time");
                return;
            }



            int hourNew = Integer.parseInt(newHour);
            int minNew = Integer.parseInt(newMinute);

            if(hourNew<1||hourNew>12) {
                hourBox.setError("Invalid Time");
                return;
            }

            if(minNew<0 || minNew>59){
                minuteBox.setError("Invalid Time");
                return;
            }

            int combinedTime = hourNew * 100 + minNew;

            String ampm = AmPmBox.getSelectedItem().toString();
            String newDesc = descBox.getText().toString();

            //normalize date
            String normalizedDate = DateNormalizeHelper.normalizeDate(newDate);

            if(normalizedDate.isEmpty()){
                dateBox.setError("Use MM/DD/YYYY");
                return;
            }

            callback.onEdited(
                    newName,
                    normalizedDate,
                    String.valueOf(combinedTime),
                    ampm,
                    newDesc
            );
            dialog.dismiss(); // close after valid
        });

        }
}