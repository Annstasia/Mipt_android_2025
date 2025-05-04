package ru.mipt.annstase.hw2.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentAllTasksBinding
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import ru.mipt.annstase.hw2.viewmodel.SettingsViewModel
import ru.mipt.annstase.hw2.viewmodel.TasksViewModel

const val TASK_ID_BUNDLE_NAME = "taskId"

@AndroidEntryPoint
class AllTasksFragment : BaseFragment(R.layout.fragment_all_tasks) {
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private val filtersAdapter = FiltersAdapter { tagName, isSelected ->
        val current = viewModel.currentTagFilters.value.orEmpty().toMutableSet()
        if (isSelected) current.add(tagName.name) else current.remove(tagName.name)
        viewModel.setCurrentTagFilters(current)
    }

    private lateinit var pagerAdapter: TasksPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAllTasksBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI() {
        pagerAdapter = TasksPagerAdapter(this)
        binding.tasksPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.tasksPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> getString(R.string.planned)
                else -> getString(R.string.done)
            }
        }.attach()

        binding.filters.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filtersAdapter
        }

        binding.fabAdd.setOnClickListener {
            requireActivity()
                .findNavController(R.id.nav_host)
                .navigate(
                    R.id.editTaskFragment,
                    bundleOf(TASK_ID_BUNDLE_NAME to 0L)
                )
        }
    }

    override fun initListeners() {
        fun updateFilters(tasks: List<TaskModel>) {
            val tags = tasks.flatMap { it.tags }.distinct()
            val selected = viewModel.currentTagFilters.value.orEmpty()
            filtersAdapter.submitList(tags.map { FiltersAdapter.FilterItem(it, selected.contains(it)) })
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            updateFilters(tasks)
        }

        viewModel.filtersEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.filters.isVisible = enabled
            if (!enabled) {
                filtersAdapter.submitList(emptyList())
            }
        }

        viewModel.currentTagFilters.observe(viewLifecycleOwner) {
            viewModel.tasks.value?.let { tasks ->
                updateFilters(tasks)
            }
        }

        settingsViewModel.adsEnabled.asLiveData().observe(viewLifecycleOwner) {
            viewModel.setAdsEnabled(it)
        }

        settingsViewModel.filtersEnabled.asLiveData().observe(viewLifecycleOwner) {
            viewModel.setFiltersEnabled(it)
        }

        settingsViewModel.sortByUrgency.asLiveData().observe(viewLifecycleOwner) {
            viewModel.setSortByUrgency(it)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
