package uz.apphub.chopar2.data.local

import android.content.SharedPreferences
import javax.inject.Inject

/**Created By Begzod Shokirov on 09/04/2024 **/

class AppSharedPref @Inject constructor(private val sharedPref: SharedPreferences) {
    fun getBoolean(key: String, defaultValue: Boolean) = sharedPref.getBoolean(key, defaultValue)
    fun setBoolean(key: String, defaultValue: Boolean) {
        sharedPref.edit().putBoolean(key, defaultValue).apply()
    }

    fun getString(key: String, defaultValue: String) =
        sharedPref.getString(key, defaultValue) ?: defaultValue

    fun setString(key: String, defaultValue: String) {
        sharedPref.edit().putString(key, defaultValue).apply()
    }

    fun getInt(key: String, defaultValue: Int) = sharedPref.getInt(key, defaultValue)
    fun setInt(key: String, defaultValue: Int) {
        sharedPref.edit().putInt(key, defaultValue).apply()
    }

    fun clear() = sharedPref.edit().clear().apply()

    fun isFirstLaunch(): Boolean = sharedPref.getBoolean("first_launch", true)
    fun setFirstLaunch() = sharedPref.edit().putBoolean("first_launch", true).apply()
}