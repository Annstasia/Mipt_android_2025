package ru.mipt.annstase.hw3.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw3.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ChatSelectionListener {

    companion object {
        private const val TAG_MASTER = "ALL_CHATS"
        private const val TAG_DETAIL = "CHAT_DETAIL"
        private const val KEY_CHAT_ID = "currentChatId"
    }

    private var currentChatId: Int? = null
    private var lastOrientation: Int = Configuration.ORIENTATION_UNDEFINED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentChatId = savedInstanceState?.getInt(KEY_CHAT_ID)
        lastOrientation = resources.configuration.orientation
        if (supportFragmentManager.findFragmentByTag(TAG_MASTER) == null) {
            setupFragments()
        }
        onBackPressedDispatcher.addCallback(this) {
            if (lastOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                finish()
            } else if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation != lastOrientation) {
            lastOrientation = newConfig.orientation
            setContentView(R.layout.activity_main)
            setupFragments()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentChatId?.let { outState.putInt(KEY_CHAT_ID, it) }
    }

    override fun onChatSelected(chatId: Int) {
        currentChatId = chatId
        val fm = supportFragmentManager

        if (lastOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            fm.commit {
                replace(
                    R.id.detail_container,
                    ChatDetailFragment.newInstance(chatId),
                    TAG_DETAIL
                )
            }
        } else {
            fm.commit {
                replace(
                    R.id.container,
                    ChatDetailFragment.newInstance(chatId),
                    TAG_DETAIL
                )
                addToBackStack(null)
            }
        }
    }

    private fun setupFragments() {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        if (lastOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.findFragmentById(R.id.container)?.let {
                fragmentManager.commitNow { remove(it) }
            }
            if (fragmentManager.findFragmentByTag(TAG_MASTER) == null) {
                fragmentManager.commitNow {
                    replace(R.id.master_container, AllChatsFragment(), TAG_MASTER)
                }
            }
            if (currentChatId != null) {
                fragmentManager.commitNow {
                    replace(
                        R.id.detail_container,
                        ChatDetailFragment.newInstance(currentChatId!!),
                        TAG_DETAIL
                    )
                }
            } else {
                fragmentManager.findFragmentByTag(TAG_DETAIL)?.let {
                    fragmentManager.commitNow { remove(it) }
                }
            }

        } else {
            fragmentManager.findFragmentById(R.id.master_container)?.let {
                fragmentManager.commitNow { remove(it) }
            }
            fragmentManager.findFragmentById(R.id.detail_container)?.let {
                fragmentManager.commitNow { remove(it) }
            }
            fragmentManager.commitNow {
                replace(R.id.container, AllChatsFragment(), TAG_MASTER)
            }
            if (currentChatId != null) {
                fragmentManager.commit {
                    replace(
                        R.id.container,
                        ChatDetailFragment.newInstance(currentChatId!!),
                        TAG_DETAIL
                    )
                    addToBackStack(null)
                }
            }
        }
    }
}
