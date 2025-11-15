package com.example.zentask.TaskLogic;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
//import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zentask.R;
import com.example.zentask.UserStorage;

public class CreateTask extends AppCompatActivity{
    Button add;
    AlertDialog dialog;

    List<Task> taskList;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.create_task);

        //load tasks
        taskList = TaskStorage.loadTasks(this);
        System.out.println("Loaded tasks: "+taskList.size());

        //greeting
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean showGreeting = pref.getBoolean("showGreetingOnce", true);
        String username = UserStorage.loadUsername(this);

        if(username == null){
            username = "User";
        }
        TextView greetingView = findViewById(R.id.greetingText);
        if(showGreeting){
            greetingView.setText("Hello "+ username);
            pref.edit().putBoolean("showGreetingOnce", false).apply();
        } else{
            greetingView.setVisibility(View.GONE);
        }
        add = findViewById(R.id.addTask);
        layout = findViewById(R.id.container);

        buildDialog();

        add.setOnClickListener(v -> dialog.show());

        //build list without re adding data
        for(Task t: taskList){
            addCard(t.name, false);
        }
    }
    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        builder.setView(view);
        builder.setTitle("Lets get something done!")
        .setPositiveButton("I'M READY!", (dialog, which) -> addCard(name.getText().toString(),true)).setNegativeButton("one sec...", (dialog, which) -> {
        });
        dialog = builder.create();
    }
    private void addCard(String name, Boolean isNewTask) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);

        // if this is a brand new task, save it
        Task t;
        if (isNewTask) {
            t = new Task(name, "", "");
            taskList.add(t);
            TaskStorage.saveTasks(this, taskList);
        } else{
            t = taskList.stream().filter(x->x.name.equals(name)).findFirst().orElse(null);
        }

        Task finalTask = t;

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            taskList.remove(finalTask);
            TaskStorage.saveTasks(this, taskList);
        });
        layout.addView(view);
    }
    protected void onStop(){
        super.onStop();
        TaskStorage.saveTasks(this,taskList);
    }
}
