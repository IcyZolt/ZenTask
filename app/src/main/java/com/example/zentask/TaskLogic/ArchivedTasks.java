package com.example.zentask.TaskLogic;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zentask.R;

import java.util.List;
public class ArchivedTasks extends AppCompatActivity {

    LinearLayout archivedContainer;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.archived_tasks);

        archivedContainer = findViewById(R.id.archived_container);

        taskList = TaskStorage.loadTasks(this);

        for (Task t : taskList) {

            if (t.isArchived) {
                addArchivedCard(t);
            }
        }
    }

    private void addArchivedCard(Task task) {
        View v = getLayoutInflater().inflate(R.layout.archived_card, null);

        TextView taskName = v.findViewById(R.id.archived_task_name);
        taskName.setText(task.name);

        archivedContainer.addView(v);
    }

}
