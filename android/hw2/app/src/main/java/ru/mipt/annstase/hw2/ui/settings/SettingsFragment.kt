package ru.mipt.annstase.hw2.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentSettingsBinding
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import ru.mipt.annstase.hw2.viewmodel.SettingsViewModel
import ru.mipt.annstase.hw2.viewmodel.TasksViewModel

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI() {
        binding.cbFilters.isChecked     = settingsViewModel.filtersEnabled.value == true
        binding.cbSortUrgency.isChecked = settingsViewModel.sortByUrgency.value == true
        binding.cbAds.isChecked         = settingsViewModel.adsEnabled.value == false

        settingsViewModel.filtersEnabled.asLiveData().observe(viewLifecycleOwner) { enabled ->
            binding.cbFilters.isChecked = enabled
        }
        settingsViewModel.sortByUrgency.asLiveData().observe(viewLifecycleOwner) {
            binding.cbSortUrgency.isChecked = it
        }
        settingsViewModel.adsEnabled.asLiveData().observe(viewLifecycleOwner) {
            binding.cbAds.isChecked = !it
        }
    }

    override fun initListeners() {
        binding.cbFilters.setOnCheckedChangeListener { _, checked ->
            Log.d("CHECK FILTER", checked.toString())
            settingsViewModel.setFiltersEnabled(checked)
        }
        binding.cbSortUrgency.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setSortByUrgency(checked)
        }
        binding.cbAds.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setAdsEnabled(!checked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
