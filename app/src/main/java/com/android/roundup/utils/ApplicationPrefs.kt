package com.android.roundup.utils

import android.content.SharedPreferences
import com.android.roundup.App

class ApplicationPrefs {

    companion object{
        private val mPreferences: SharedPreferences = App.self().getSharedPreferences(
            Constants.APPLICATION_PREFS, 0
        )

        fun isNotFirstTime(): Boolean {
            return mPreferences.getBoolean(Constants.IS_FIRST_TIME, false)
        }

        fun setNotFirstTime(b: Boolean) {
            val mEditor = mPreferences.edit()
            mEditor.putBoolean(Constants.IS_FIRST_TIME, b).apply()
        }

        fun isLoggedIn(): Boolean{
            return mPreferences.getBoolean(Constants.IS_LOGGED_IN, false)
        }

        fun setLoggedIn(b: Boolean){
            val mEditor = mPreferences.edit()
            mEditor.putBoolean(Constants.IS_LOGGED_IN, b).apply()
        }
    }
}
