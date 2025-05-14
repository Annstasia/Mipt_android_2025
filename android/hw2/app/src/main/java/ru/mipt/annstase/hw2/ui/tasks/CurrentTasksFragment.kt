package ru.mipt.annstase.hw2.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentCurrentTasksBinding
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import ru.mipt.annstase.hw2.viewmodel.SettingsViewModel
import ru.mipt.annstase.hw2.viewmodel.TasksViewModel


@AndroidEntryPoint
class CurrentTasksFragment : BaseFragment(R.layout.fragment_current_tasks) {

    private var _binding: FragmentCurrentTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TasksViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private val filtersAdapter = FiltersAdapter { tagName, isSelected ->
        val current = viewModel.currentTagFilters.value.orEmpty().toMutableSet()
        if (isSelected) current.add(tagName.name) else current.remove(tagName.name)
        viewModel.setCurrentTagFilters(current)
    }

    private lateinit var sectionsAdapter: TasksSectionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCurrentTasksBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI() {
        binding.filters.apply {
            layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            adapter = filtersAdapter
        }

        sectionsAdapter = TasksSectionAdapter(
            context = requireContext(),
            onCheckedChange = { task, done ->
                viewModel.insertOrUpdate(task.copy(done = done))
            },
            onItemClick = { task ->
                requireActivity().findNavController(R.id.nav_host).navigate(
                    R.id.editTaskFragment,
                    bundleOf("taskId" to task.id)
                )
            }
        )

        binding.recyclerTasks.apply {
            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
            adapter = sectionsAdapter
        }

        binding.fabAddTask.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host).navigate(
                R.id.editTaskFragment,
                bundleOf("taskId" to 0L)
            )
        }

    }

    override fun initListeners() {
        settingsViewModel.filtersEnabled.asLiveData().observe(viewLifecycleOwner) {
            viewModel.setFiltersEnabled(it)
        }

        settingsViewModel.sortByUrgency.asLiveData().observe(viewLifecycleOwner) {
            viewModel.setSortByUrgency(it)
        }
        fun updateFilters() {
            val tasks = viewModel.tasks.value.orEmpty()
            val tags = tasks.flatMap { it.tags }.distinct()
            val selected = viewModel.currentTagFilters.value.orEmpty()
            filtersAdapter.submitList(
                tags.map { FiltersAdapter.FilterItem(it, selected.contains(it)) }
            )
        }

        viewModel.tasks.observe(viewLifecycleOwner)    { updateFilters() }
        viewModel.currentTagFilters.observe(viewLifecycleOwner) { updateFilters() }

        viewModel.filtersEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.filters.isVisible = enabled
            if (!enabled) filtersAdapter.submitList(emptyList())
        }

        viewModel.sections.observe(viewLifecycleOwner) { sections ->
            val todayTasks = sections
                .firstOrNull { it.first == getString(R.string.today_tasks) }
                ?.second.orEmpty()
            val tomorrowTasks = sections
                .firstOrNull { it.first == getString(R.string.tomorrow_tasks) }
                ?.second.orEmpty()
            sectionsAdapter.submitSections(todayTasks, tomorrowTasks)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
