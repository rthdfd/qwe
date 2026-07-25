package com.example.cozelike.util

import android.content.Context
import android.content.SharedPreferences

object Prefs {

    private const val NAME = "cozelike_settings"

    private fun sp(context: Context): SharedPreferences =
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun apiBaseUrl(context: Context): String =
        sp(context).getString("api_base_url", "https://api.openai.com/v1")
            ?: "https://api.openai.com/v1"

    fun apiKey(context: Context): String =
        sp(context).getString("api_key", "") ?: ""

    fun defaultModel(context: Context): String =
        sp(context).getString("default_model", "gpt-3.5-turbo") ?: "gpt-3.5-turbo"

    fun setApiBaseUrl(context: Context, value: String) =
        sp(context).edit().putString("api_base_url", value).apply()

    fun setApiKey(context: Context, value: String) =
        sp(context).edit().putString("api_key", value).apply()

    fun setDefaultModel(context: Context, value: String) =
        sp(context).edit().putString("default_model", value).apply()
}
