package com.example.hw1
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.addCallback
import java.text.SimpleDateFormat
import java.util.*

class StatsActivity : BaseActivity() {
    companion object {
        const val STATS_KEY = "stats_text"
        const val CLICK_TIMESTAMP_KEY = "click_timestamp"
    }
    private val prefs by lazy {
        getSharedPreferences("clicker_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        onBackPressedDispatcher.addCallback(this) {
            if (intent?.action == Intent.ACTION_VIEW) {
                finishAffinity()
            } else {
                finish()
            }
        }

        val statsTextView = findViewById<TextView>(R.id.stats_text)

        if (savedInstanceState != null) {
            statsTextView.text = savedInstanceState.getString(STATS_KEY, "")
        } else {
            val now = System.currentTimeMillis()
            val timestamps = prefs.getStringSet(CLICK_TIMESTAMP_KEY, setOf())?.map { it.toLong() } ?: emptyList()
            val stats = calculateStats(timestamps, now)
            statsTextView.text = stats
        }

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            if (intent?.action == Intent.ACTION_VIEW) {
                finishAffinity()
            } else {
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val statsText = findViewById<TextView>(R.id.stats_text).text.toString()
        outState.putString(STATS_KEY, statsText)
    }

    private fun calculateStats(times: List<Long>, now: Long): String {
        val dayMillis = 24 * 60 * 60 * 1000
        val weekMillis = 7 * dayMillis
        val monthMillis = 30L * dayMillis
        val yearMillis = 365L * dayMillis

        val dayClicks = times.count { now - it < dayMillis }
        val weekClicks = times.count { now - it < weekMillis }
        val monthClicks = times.count { now - it < monthMillis }
        val yearClicks = times.count { now - it < yearMillis }

        val dateFormat = SimpleDateFormat("LLLL", Locale.getDefault())
        val monthName = dateFormat.format(Date()).replaceFirstChar { it.uppercase() }

        return getString(
            R.string.stats_format, dayClicks, weekClicks, monthClicks, monthName, yearClicks
        )
    }
}
