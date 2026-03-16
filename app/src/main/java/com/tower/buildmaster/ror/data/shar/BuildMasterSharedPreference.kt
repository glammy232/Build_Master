package com.tower.buildmaster.ror.data.shar

import android.content.Context
import androidx.core.content.edit

class BuildMasterSharedPreference(context: Context) {
    private val chickenPrefs = context.getSharedPreferences("farmSharedPrefsAb", Context.MODE_PRIVATE)

    var chickenSavedUrl: String
        get() = chickenPrefs.getString(FARM_SAVED_URL, "") ?: ""
        set(value) = chickenPrefs.edit { putString(FARM_SAVED_URL, value) }

    var chickenExpired : Long
        get() = chickenPrefs.getLong(FARM_EXPIRED, 0L)
        set(value) = chickenPrefs.edit { putLong(FARM_EXPIRED, value) }

    var chickenAppState: Int
        get() = chickenPrefs.getInt(FARM_APPLICATION_STATE, 0)
        set(value) = chickenPrefs.edit { putInt(FARM_APPLICATION_STATE, value) }

    var chickenNotificationRequest: Long
        get() = chickenPrefs.getLong(FARM_NOTIFICAITON_REQUEST, 0L)
        set(value) = chickenPrefs.edit { putLong(FARM_NOTIFICAITON_REQUEST, value) }

    var chickenNotificationRequestedBefore: Boolean
        get() = chickenPrefs.getBoolean(FARM_NOTIFICATION_REQUEST_BEFORE, false)
        set(value) = chickenPrefs.edit { putBoolean(
            FARM_NOTIFICATION_REQUEST_BEFORE, value) }

    companion object {
        private const val FARM_SAVED_URL = "farmSavedUrl"
        private const val FARM_EXPIRED = "farmExpired"
        private const val FARM_APPLICATION_STATE = "farmApplicationState"
        private const val FARM_NOTIFICAITON_REQUEST = "farmNotificationRequest"
        private const val FARM_NOTIFICATION_REQUEST_BEFORE = "farmNotificationRequestedBefore"
    }
}