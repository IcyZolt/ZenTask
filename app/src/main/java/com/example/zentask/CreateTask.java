package com.example.zentask;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
//import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateTask extends AppCompatActivity{
    Button add;
    AlertDialog dialog;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.create_task);

        add = findViewById(R.id.addTask);
        layout = findViewById(R.id.container);

        buildDialog();
        add.setOnClickListener(v -> dialog.show());
    }
    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        builder.setView(view);
        builder.setTitle("Lets get somethin done!")
        .setPositiveButton("I'M READY!", (dialog, which) -> addCard(name.getText().toString())).setNegativeButton("one sec...", (dialog, which) -> {
        });
        dialog = builder.create();
    }
    private void addCard(String name) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card, null);


        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);
        delete.setOnClickListener(v -> layout.removeView(view));
        layout.addView(view);
    }
}
