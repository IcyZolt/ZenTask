package com.example.zentask.TaskLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Intent;

import com.example.zentask.R;
import com.example.zentask.SettingsPage;
import com.example.zentask.TaskLogic.TaskModification.TaskModification;
import com.example.zentask.UserStorage;
import com.example.zentask.Experience;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateTask extends AppCompatActivity {

    private Button add;
    private AlertDialog dialog;
    private LinearLayout layout;

    private List<Task> taskList;
    private Experience userExperience; // single experience instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        // Initialize experience
        userExperience = new Experience();

        // Load tasks
        taskList = TaskStorage.loadTasks(this);
        System.out.println("Loaded tasks: " + taskList.size());

        // Greeting
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean showGreeting = pref.getBoolean("showGreetingOnce", true);
        String username = UserStorage.loadUsername(this);
        if (username == null) username = "User";

        TextView greetingView = findViewById(R.id.greetingText);
        if (showGreeting) {
            greetingView.setText("Hello " + username);
            pref.edit().putBoolean("showGreetingOnce", false).apply();
        } else {
            greetingView.setVisibility(View.GONE);
        }

        add = findViewById(R.id.addTask);
        layout = findViewById(R.id.container);

        buildDialog();
        add.setOnClickListener(v -> dialog.show());

        // Build task list UI
        for (Task t : taskList) {
            addCard(t, false);
        }

        // Settings button
        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateTask.this, SettingsPage.class);
            startActivity(intent);
        });
    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        builder.setView(view);
        builder.setTitle("Let's get something done!")
                .setPositiveButton("I'M READY!", (dialog, which) -> {
                    String n = name.getText().toString().trim();
                    if (!n.isEmpty()) {
                        Task newTask = new Task(n, "", 0, "", "", false, java.util.UUID.randomUUID().toString());
                        addCard(newTask, true);
                    }
                })
                .setNegativeButton("One sec...", (dialog, which) -> {
                });

        dialog = builder.create();
    }

    @SuppressLint("InflateParams")
    private void addCard(Task task, Boolean isNewTask) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView dateView = view.findViewById(R.id.date);
        TextView timeView = view.findViewById(R.id.time);

        Button delete = view.findViewById(R.id.delete);
        Button complete = view.findViewById(R.id.complete); // new button

        view.setTag(task.getId());

        Task finalTask;


        if (isNewTask) {
            finalTask = task;
            taskList.add(finalTask);
            TaskStorage.saveTasks(this, taskList);
        } else {
            // find the existing saved task by ID
            finalTask = taskList.stream()
                    .filter(x -> x.getId().equals(task.getId()))
                    .findFirst()
                    .orElse(task); // fallback
        }

        nameView.setText(finalTask.getName());
        dateView.setText(finalTask.getDate());
        timeView.setText(formatTime(finalTask));


        view.setOnLongClickListener(v -> {
            TaskModification.showEditDialog(this, finalTask, (newName, newDate, newTime, ampm, newDescription) -> {
                String oldName = finalTask.getName();

                finalTask.setName(newName);
                finalTask.setDate(newDate);
                finalTask.setTime(Integer.parseInt(newTime));
                finalTask.setAmpm(ampm);
                finalTask.setDescription(newDescription);

                TaskStorage.saveTasks(this, taskList);
                refresh(oldName, finalTask);
            });
            return true;
        });

        // Delete task
        delete.setOnClickListener(v -> {
            layout.removeView(view);
            taskList.remove(finalTask);
            TaskStorage.saveTasks(this, taskList);
        });

        // Complete task and grant experience
        complete.setOnClickListener(v -> {
            finalTask.setArchived(true);
            TaskStorage.saveTasks(this, taskList);

            String completionDate = new SimpleDateFormat("M/d/yyyy").format(new Date());
            userExperience.completeTask(finalTask.getDate(), completionDate);

            Toast.makeText(this, "Task completed! Level: " + userExperience.getLevel(), Toast.LENGTH_SHORT).show();

            // Remove card from UI
            layout.removeView(view);
        });

        layout.addView(view, layout.getChildCount() - 1);
    }

    protected void onStop() {
        super.onStop();
        TaskStorage.saveTasks(this, taskList);
    }

    private void refresh(String oldName, Task task) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View card = layout.getChildAt(i);

            TextView nameView = card.findViewById(R.id.name);
            TextView dateView = card.findViewById(R.id.date);
            TextView timeView = card.findViewById(R.id.time);

            if (nameView.getText().toString().equals(oldName)) {

                nameView.setText(task.getName());
                dateView.setText(task.getDate());
                timeView.setText(formatTime(task));

                break;
            }
        }
    }
    private String formatTime(Task task) {
        if (task.getTime() <= 0 || task.getAmpm() == null || task.getAmpm().isEmpty()) {
            return "";
        }

        int hour = task.getTime() / 100;
        int minute = task.getTime() % 100;

        return String.format("%02d:%02d %s", hour, minute, task.getAmpm());
    }
}


