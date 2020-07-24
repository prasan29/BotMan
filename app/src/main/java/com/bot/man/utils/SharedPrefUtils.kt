package com.bot.man.utils

import android.content.Context
import android.content.SharedPreferences
import com.bot.man.model.Constants

class SharedPrefUtils private constructor(context: Context) {
    /**
     * Method  used to set the string value of SharedPreferences.
     *
     * @param key   Key of the string to be set.
     * @param value Value of the string to be set.
     */
    fun setStringValue(key: String?, value: String?) {
        val editor = sSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Method to get the string value from SharedPreferences.
     *
     * @param key Key of the string to be get.
     * @return Key of te string.
     */
    fun getStringValue(key: String?): String? {
        return sSharedPreferences
                .getString(key, Constants.DEFAULT_SHARED_STRING_VALUE)
    }

    /**
     * Method  used to set the b boolean value of SharedPreferences.
     *
     * @param key   Key of the string to be set.
     * @param value Value of the boolean to be set.
     */
    fun setBooleanValue(key: String?, value: Boolean) {
        val editor = sSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Method to get the boolean value from SharedPreferences.
     *
     * @param key Key of the string to be get.
     * @return value of te boolean.
     */
    fun getBooleanValue(key: String?): Boolean {
        return sSharedPreferences
                .getBoolean(key, Constants.DEFAULT_SHARED_BOOLEAN_VALUE)
    }

    companion object {
        lateinit var INSTANCE: SharedPrefUtils
            private set
        private lateinit var sSharedPreferences: SharedPreferences

    }

    init {
        sSharedPreferences = context.getSharedPreferences(
                Constants.APPLICATION_PREFERENCE_NAME,
                Context.MODE_PRIVATE)
    }
}