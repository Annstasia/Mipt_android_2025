package com.example.hw1
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.content.edit

class MainActivity : BaseActivity() {
    companion object {
        const val CLICK_COUNTER_KEY = "click_count"
        const val CLICK_TIMESTAMP_KEY = "click_timestamp"
        const val LEVEL_2 = 1000
        const val LEVEL_3 = 100000
    }
    private lateinit var clickImageView: ImageView
    private lateinit var clickCountText: TextView
    private lateinit var clicksToNextLevelText: TextView
    private var clickCount = 0

    private val prefs by lazy {
        getSharedPreferences("clicker_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickImageView = findViewById(R.id.click_image)
        clickCountText = findViewById(R.id.click_count_text)
        clicksToNextLevelText = findViewById(R.id.clicks_to_next_level_text)

        clickCount =
            savedInstanceState?.getInt(CLICK_COUNTER_KEY, 0) ?: prefs.getInt(CLICK_COUNTER_KEY, 0)

        updateUI()

        clickImageView.setOnClickListener {
            clickCount++
            prefs.edit { putInt(CLICK_COUNTER_KEY, clickCount) }
            saveClickTimestamp()
            updateUI()
        }

        findViewById<TextView>(R.id.stats_button).setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.help_button).setOnClickListener {
            val telegramIntent = Intent(Intent.ACTION_VIEW, getString(R.string.help_uri).toUri())
            startActivity(telegramIntent)
        }

        findViewById<ImageButton>(R.id.exit_button).setOnClickListener {
            finishAffinity()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLICK_COUNTER_KEY, clickCount)
    }

    private fun updateUI() {
        clickCountText.text = getString(R.string.total_clicks, clickCount)
        val (levelRes, toNext) = when {
            clickCount > LEVEL_3 -> Pair(R.drawable.cardio_3, 0)
            clickCount > LEVEL_2 -> Pair(R.drawable.cardio_2, LEVEL_3 - clickCount)
            else -> Pair(R.drawable.cardio_1, LEVEL_2 - clickCount)
        }
        clickImageView.setImageResource(levelRes)
        clicksToNextLevelText.text = getString(R.string.clicks_to_next_level, toNext)
    }

    private fun saveClickTimestamp() {
        val timestamps = prefs.getStringSet(CLICK_TIMESTAMP_KEY, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        timestamps.add(System.currentTimeMillis().toString())
        prefs.edit { putStringSet(CLICK_TIMESTAMP_KEY, timestamps) }
    }
}
