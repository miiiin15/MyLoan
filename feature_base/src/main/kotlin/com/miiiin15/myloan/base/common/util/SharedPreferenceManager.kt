package com.miiiin15.myloan.base.common.util

class SharedPreferenceManager {
    private val preferences = mutableMapOf<String, Any>()

    fun putString(key: String, value: String) {
        preferences[key] = value
    }

    fun getString(key: String): String? {
        return preferences[key] as? String
    }

    fun clear() {
        preferences.clear()
    }
}