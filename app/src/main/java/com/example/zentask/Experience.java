package com.example.zentask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Experience {

    private int level = 0;
    private double experience = 0;
    private double streak = 1.0;
    private double levelGainModifier = 1;
    private double experienceGainModifier = 1;

    private static final int BASE_LEVEL_THRESHOLD = 50;

    public interface ExperienceListener {
        void onExperienceUpdated(int level, double experience, int xpToNextLevel);
        void onLevelUp(int newLevel);
    }

    private ExperienceListener listener;

    public void setExperienceListener(ExperienceListener listener) {
        this.listener = listener;
    }

    public Experience() {
        this.level = 0;
        this.experience = 0;
        this.streak = 1.0;
        this.levelGainModifier = 1;
        this.experienceGainModifier = 1;
    }

    public void completeTask(String taskDueDate, String completionDate) {
        boolean onTime = isDueOnTime(taskDueDate, completionDate);

        double gainedExp;
        if (onTime) {
            gainedExp = experienceGain(levelGainModifier);
            streak += 0.1;
        } else {
            gainedExp = experienceGain(levelGainModifier) * 0.5;
            streak = 1.0;
        }

        addExperience(gainedExp);
    }

    private double experienceGain(double levelGainModifier) {
        return 15 * levelGainModifier * streak; // Increased for faster leveling
    }

    private int requiredExperience() {
        return (int) (BASE_LEVEL_THRESHOLD * Math.pow(1.18, level) * experienceGainModifier);
    }

    private void addExperience(double amount) {
        this.experience += amount;

        if (listener != null) {
            listener.onExperienceUpdated(level, experience, getExperienceToNextLevel());
        }

        while (this.experience >= requiredExperience()) {
            this.experience -= requiredExperience();
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        System.out.println("Congratulations! You reached level " + level);

        if (listener != null) {
            listener.onLevelUp(level);
        }
    }

    public boolean isDueOnTime(String taskDueDate, String completionDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
            Date due = sdf.parse(taskDueDate);
            Date completed = sdf.parse(completionDate);
            return !completed.after(due);
        } catch (ParseException e) {
            System.err.println("Date parsing failed: " + e.getMessage());
            return false;
        }
    }

    public int getLevel() { return level; }
    public double getExperience() { return experience; }
    public double getStreak() { return streak; }
    public int getExperienceToNextLevel() { return requiredExperience() - (int) experience; }
}
