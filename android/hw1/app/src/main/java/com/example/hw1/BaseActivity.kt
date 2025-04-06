package com.example.hw1
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*
import androidx.core.content.edit

open class BaseActivity : AppCompatActivity() {
    companion object {
        const val PREFS_NAME = "clicker_prefs"
        const val KEY_APP_LANG = "app_lang"
        const val KEY_APP_THEME = "app_theme"
        const val RUSSIAN_LANG = "ru"
        const val ENGLISH_LANG = "en"
    }
    private fun updateLocaleAndRecreate(lang: String) {
        val newConfig = resources.configuration
        val newLocale = Locale(lang)
        Locale.setDefault(newLocale)
        newConfig.setLocale(newLocale)
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)

    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lang = prefs.getString(KEY_APP_LANG, null) ?: Resources.getSystem().configuration.locales[0].language
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = newBase.resources.configuration
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        val themeMode = prefs.getInt(KEY_APP_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)
        super.attachBaseContext(context)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return when (item.itemId) {
            R.id.theme_light -> {
                prefs.edit { putInt(KEY_APP_THEME, AppCompatDelegate.MODE_NIGHT_NO) }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            R.id.theme_dark -> {
                prefs.edit { putInt(KEY_APP_THEME, AppCompatDelegate.MODE_NIGHT_YES) }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            R.id.theme_system -> {
                prefs.edit { remove(KEY_APP_THEME) }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                true
            }
            R.id.lang_en -> {
                prefs.edit { putString(KEY_APP_LANG, ENGLISH_LANG) }
                updateLocaleAndRecreate(ENGLISH_LANG)
                true
            }
            R.id.lang_ru -> {
                prefs.edit { putString(KEY_APP_LANG, RUSSIAN_LANG) }
                updateLocaleAndRecreate(RUSSIAN_LANG)
                true
            }
            R.id.lang_system -> {
                prefs.edit { remove(KEY_APP_LANG) }
                val systemLocale = Resources.getSystem().configuration.locales[0].language
                updateLocaleAndRecreate(systemLocale)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
