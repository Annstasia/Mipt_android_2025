package ru.mipt.annstase.hw2.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutResId: Int) : Fragment(layoutResId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
    }

    protected open fun initUI() {}
    protected open fun initListeners() {}
}