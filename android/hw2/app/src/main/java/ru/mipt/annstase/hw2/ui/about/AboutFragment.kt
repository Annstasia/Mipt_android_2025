package ru.mipt.annstase.hw2.ui.about

import android.os.Bundle
import android.view.View
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentAboutBinding
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment(R.layout.fragment_about) {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAboutBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initListeners() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}