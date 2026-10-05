package com.getit.getit.yes.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemePrefs {
    private const val PREFS = "settings"
    private const val KEY_NIGHT_MODE = "night_mode"

    val modes = listOf(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
    )

    fun saved(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun apply(context: Context) = AppCompatDelegate.setDefaultNightMode(saved(context))

    fun set(context: Context, mode: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit { putInt(KEY_NIGHT_MODE, mode) }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
