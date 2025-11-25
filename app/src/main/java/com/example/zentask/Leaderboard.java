package com.example.zentask;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zentask.R;
import java.util.*;
public class Leaderboard extends AppCompatActivity {

    LinearLayout leaderboardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        leaderboardContainer = findViewById(R.id.leaderboard_container);

        List<Player> players = loadFakeLeaderboard();

        Collections.sort(players, (a, b) -> b.xp - a.xp);

        for (Player p : players) {
            addPlayerCard(p);
        }
    }

    private List<Player> loadFakeLeaderboard() {
        List<Player> list = new ArrayList<>();

        list.add(new Player("Ava", 5, 1250));
        list.add(new Player("Jordan", 7, 2100));
        list.add(new Player("Kai", 4, 900));

        String username = UserStorage.loadUsername(this);
        if (username == null || username.isEmpty()) {
            username = "You";
    }
        int userXP = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("xp", 300);
        int userLevel = userXP / 300;

        list.add(new Player(username, userLevel, userXP));

        return list;
    }

    private void addPlayerCard(Player player) {

        android.view.View v = getLayoutInflater().inflate(R.layout.leaderboard_card, null);

        TextView name = v.findViewById(R.id.player_name);
        TextView level = v.findViewById(R.id.player_level);
        TextView xp = v.findViewById(R.id.player_xp);

        name.setText(player.name);
        level.setText("Level: " + player.level);
        xp.setText("XP: " + player.xp);

        leaderboardContainer.addView(v);
    }

    public static class Player {

        String name;
        int level;
        int xp;

        Player(String name, int level, int xp) {
            this.name = name;
            this.level = level;
            this.xp = xp;

        }
    }
}

