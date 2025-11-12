package com.example.zentask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.font.FontVariation;

public class StartupPage extends AppCompatActivity {

    EditText usernameInputStart;
    Switch notificationSwitchStart;
    Button saveButtonStart;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_page);

        //connect layout elements
        usernameInputStart = findViewById(R.id.username_input_start);
        notificationSwitchStart = findViewById(R.id.notification_switch_start);
        saveButtonStart = findViewById(R.id.save_button_start);

        //get shared preferences
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        //load saved data
        usernameInputStart.setText(prefs.getString("username", ""));
        notificationSwitchStart.setChecked(prefs.getBoolean("notifications", true));

        //save button action
        saveButtonStart.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", usernameInputStart.getText().toString());
            editor.putBoolean("notifications", notificationSwitchStart.isChecked());
            editor.putBoolean("isFirstRun", false);
            editor.apply();

            Intent intent = new Intent(StartupPage.this, MainActivity.class);
            startActivity(intent);

            finish();
        });
    }


}
