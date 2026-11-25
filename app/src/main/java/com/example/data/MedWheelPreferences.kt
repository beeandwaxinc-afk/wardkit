package com.example.data

import android.content.Context
import android.content.SharedPreferences

class MedWheelPreferences(context: Context) {
    private val appContext = context.applicationContext

    // Lazily opened so initialization never performs disk I/O on the main thread
    private val prefs: SharedPreferences by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun loadThemeMode(): ThemeMode =
        ThemeMode.fromKey(prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.key))

    fun saveThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.key).apply()
    }

    fun loadPinnedIds(): List<String> {
        val raw = prefs.getString(KEY_PINNED_IDS, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").filter { it.isNotBlank() }.distinct()
    }

    fun savePinnedIds(ids: List<String>) {
        prefs.edit().putString(KEY_PINNED_IDS, ids.distinct().joinToString(",")).apply()
    }

    fun loadUsageCounts(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        for ((key, value) in prefs.all) {
            if (key.startsWith(PREFIX_USAGE) && value is Int && value > 0) {
                map[key.removePrefix(PREFIX_USAGE)] = value
            }
        }
        return map
    }

    fun saveUsageCount(calculatorId: String, count: Int) {
        val key = "$PREFIX_USAGE$calculatorId"
        prefs.edit().putInt(key, count).apply()
    }

    fun incrementUsageCount(calculatorId: String): Map<String, Int> {
        val key = "$PREFIX_USAGE$calculatorId"
        val next = prefs.getInt(key, 0) + 1
        prefs.edit().putInt(key, next).apply()
        return loadUsageCounts()
    }

    fun loadPinnedKitIds(): List<String> {
        val raw = prefs.getString(KEY_PINNED_KIT_IDS, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").filter { it.isNotBlank() }.distinct()
    }

    fun savePinnedKitIds(ids: List<String>) {
        prefs.edit().putString(KEY_PINNED_KIT_IDS, ids.distinct().joinToString(",")).apply()
    }

    fun loadLabUnitSystem(): com.example.data.kit.LabUnitSystem =
        com.example.data.kit.LabUnitSystem.fromKey(
            prefs.getString(KEY_LAB_UNIT_SYSTEM, com.example.data.kit.LabUnitSystem.CONV.key)
        )

    fun saveLabUnitSystem(system: com.example.data.kit.LabUnitSystem) {
        prefs.edit().putString(KEY_LAB_UNIT_SYSTEM, system.key).apply()
    }

    companion object {
        private const val PREFS_NAME = "medwheel_offline_prefs"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_PINNED_IDS = "pinned_calculator_ids"
        private const val PREFIX_USAGE = "usage_count_"
        private const val KEY_PINNED_KIT_IDS = "wardkit_pinned_kit_ids"
        private const val KEY_LAB_UNIT_SYSTEM = "wardkit_lab_unit_system"
    }
}
