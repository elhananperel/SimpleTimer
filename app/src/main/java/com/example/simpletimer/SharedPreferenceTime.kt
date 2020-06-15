package com.example.simpletimer


import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log

class SharedPreferenceTime(var _context: Context) {
    // Shared Preferences
    var pref: SharedPreferences
    var editor: Editor

    // Shared pref mode
    var PRIVATE_MODE = 0

    fun setTime(time: Long) {
        editor.putLong(KEY_TIME, time)

        // commit changes
        editor.commit()
        Log.d(
            TAG,
            "time update ! : $time"
        )
    }
    fun setLength(length: Long) {
        editor.putLong(KEY_LENGTH, length)

        // commit changes
        editor.commit()
        Log.d(
            TAG,
            "time update ! : $length"
        )
    }

    val getTime: Long
        get() = pref.getLong(KEY_TIME, -1L)

    val getlength: Long
        get() = pref.getLong(KEY_LENGTH, 345600000)


    companion object {
        // LogCat tag
        private const val TAG = "timer_i"

        // Shared preferences file name
        private const val PREF_NAME = "AndroidHiveLogin"
        private const val KEY_TIME = "time"
        private const val KEY_LENGTH = "length"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}