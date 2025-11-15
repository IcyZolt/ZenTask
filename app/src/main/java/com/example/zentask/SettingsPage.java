package com.example.zentask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsPage extends AppCompatActivity {

    EditText usernameInputStart;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch notificationSwitchStart;
    Button saveButtonStart;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        //connect layout elements
        usernameInputStart = findViewById(R.id.username_input);
        notificationSwitchStart = findViewById(R.id.notification_switch);
        saveButtonStart = findViewById(R.id.save_button);

        //get shared preferences
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        //load saved data
        usernameInputStart.setText(prefs.getString("username", ""));
        notificationSwitchStart.setChecked(prefs.getBoolean("notifications", true));

        //save button action
        saveButtonStart.setOnClickListener(v -> {
            String username = usernameInputStart.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", username);
            editor.putBoolean("notifications", notificationSwitchStart.isChecked());
            editor.putBoolean("isFirstRun", false);
            editor.putBoolean("showGreetingOnce", true); //show the greeting on next entry to Mainactivity
            editor.apply();

            UserStorage.saveUsername(this, username);
            android.util.Log.d("UserStorage", "Saved username file at: " + getFilesDir() + "/user_info.txt");

            Intent intent = new Intent(SettingsPage.this, MainActivity.class);
            startActivity(intent);

            finish();
        });

    }




}
