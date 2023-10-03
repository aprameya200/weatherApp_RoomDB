package com.example.api_to_database

import android.content.Context
import android.util.LruCache

class CacheMemory(private val context: Context) {
    private val lruCache = LruCache<String, Any>(cacheSize)

    // Define the maximum size of the in-memory cache
    companion object {
        private const val cacheSize = 1024 * 1024 // 1MB
    }

    // Store data in both the LruCache and SharedPreferences
    fun put(key: String, value: Any) {
        lruCache.put(key, value)

        // Also store data in SharedPreferences for persistence
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }

    // Retrieve data from the LruCache if available, otherwise, from SharedPreferences
    fun get(key: String): Any? {
        val cachedValue = lruCache.get(key)
        if (cachedValue != null) {
            return cachedValue
        }

        // If not found in the LruCache, attempt to retrieve from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }
}