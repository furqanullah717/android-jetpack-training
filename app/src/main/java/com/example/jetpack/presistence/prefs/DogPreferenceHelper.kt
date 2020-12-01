package com.example.jetpack.presistence.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class DogPreferenceHelper {

    companion object {
        private lateinit var prefs: SharedPreferences
        private const val TIME_KEY = "pref_time"

        @Volatile
        private var INSTANCE: DogPreferenceHelper? = null
        private var LOCK = Any()
        operator fun invoke(ctx: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: BuildPref(ctx).also {
                INSTANCE = it
            }
        }

        private fun BuildPref(ctx: Context): DogPreferenceHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
            return DogPreferenceHelper()
        }
    }

    fun storeTime(time: Long) {
        prefs.edit(commit = true) {
            putLong(TIME_KEY, time)
        }
    }

    fun getTime(): Long {
        return prefs?.getLong(TIME_KEY, 0L)
    }
}