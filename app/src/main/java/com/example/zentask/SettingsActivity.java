package com.example.zentask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    EditText usernameInput;
    Switch notificationSwitch;
    Button saveButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //connect layout elements
        usernameInput = findViewById(R.id.username_input);
        notificationSwitch = findViewById(R.id.notification_switch);
        saveButton = findViewById(R.id.save_button);

        //get shared preferences
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        //load saved data
        usernameInput.setText(prefs.getString("username", ""));
        notificationSwitch.setChecked(prefs.getBoolean("notifications", true));

        //save button action
        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", usernameInput.getText().toString());
            editor.putBoolean("notifications", notificationSwitch.isChecked());
            editor.apply();
            finish();
        });
    }


}
