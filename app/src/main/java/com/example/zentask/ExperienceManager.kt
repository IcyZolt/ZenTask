package com.example.zentask

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class ExperienceManager(context: Context) {
    private val prefs = context.getSharedPreferences("ExperiencePrefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private var expSystem = Experience()

    init {
        expSystem = Experience().apply {
            val savedLevel = prefs.getInt("level", 0)
            val savedExp = prefs.getFloat("experience", 0f).toDouble()
            val savedStreak = prefs.getFloat("streak", 1f).toDouble()

            javaClass.getDeclaredField("level").apply { isAccessible = true }.setInt(this, savedLevel)
            javaClass.getDeclaredField("experience").apply { isAccessible = true }.setDouble(this, savedExp)
            javaClass.getDeclaredField("streak").apply { isAccessible = true }.setDouble(this, savedStreak)
        }
    }

    fun completeTask(dueDate: String) {
        val today = SimpleDateFormat("M/d/yyyy", Locale.getDefault()).format(Date())
        expSystem.completeTask(dueDate, today)
        saveProgress()
    }

    private fun saveProgress() {
        editor.putInt("level", expSystem.level)
        editor.putFloat("experience", expSystem.experience.toFloat())
        editor.putFloat("streak", expSystem.streak.toFloat())
        editor.apply()
    }

    fun getLevel() = expSystem.level
    fun getExperience() = expSystem.experience
    fun getRequiredExp() = expSystem.experienceToNextLevel
    fun getProgressPercent(): Float =
        (expSystem.experience / expSystem.experienceToNextLevel).toFloat().coerceIn(0f, 1f)
}
