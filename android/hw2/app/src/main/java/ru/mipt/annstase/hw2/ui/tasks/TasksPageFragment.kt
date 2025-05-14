package ru.mipt.annstase.hw2.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentTasksPageBinding
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import ru.mipt.annstase.hw2.viewmodel.SettingsViewModel
import ru.mipt.annstase.hw2.viewmodel.TasksViewModel
import kotlin.random.Random

private const val ARG_DONE_FLAG       = "doneFlag"

@AndroidEntryPoint
class TasksPageFragment : BaseFragment(R.layout.fragment_tasks_page) {

    private var _binding: FragmentTasksPageBinding? = null
    private val binding get() = _binding!!

    private val tasksViewModel: TasksViewModel       by activityViewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val doneFlag: Boolean
        get() = requireArguments().getBoolean(ARG_DONE_FLAG, false)

    private lateinit var adapter: MixedTaskAdapter
    private var adJob: Job? = null

    private var lastFiltered: List<TaskModel> = emptyList()
    private var adPosition: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTasksPageBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
    }

    override fun initUI() {
        adapter = MixedTaskAdapter(
            onCheckedChange = { task, isChecked ->
                tasksViewModel.insertOrUpdate(task.copy(done = isChecked))
            },
            onTaskClick = { task ->
                requireActivity().findNavController(R.id.nav_host).navigate(
                    R.id.editTaskFragment,
                    bundleOf(TASK_ID_BUNDLE_NAME to task.id)
                )
            },
            onAdClose = { removeAd() }
        )

        binding.recyclerPage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TasksPageFragment.adapter
        }

        applyCurrentFilters()
    }

    override fun initListeners() {
        tasksViewModel.tasks.observe(viewLifecycleOwner)    { applyCurrentFilters() }
        tasksViewModel.currentTagFilters.observe(viewLifecycleOwner) { applyCurrentFilters() }
        tasksViewModel.sortByUrgency.observe(viewLifecycleOwner)      { applyCurrentFilters() }

        adJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                if (settingsViewModel.adsEnabled.asLiveData().value == true) {
                    insertAd()
                    delay(resources.getInteger(R.integer.ad_duration_millis).toLong())
                    removeAd()
                }
                delay(resources.getInteger(R.integer.ad_interval_millis).toLong())
            }
        }
    }

    private fun applyCurrentFilters() {
        val all = tasksViewModel.tasks.value.orEmpty()
        val tags = tasksViewModel.currentTagFilters.value.orEmpty()
        lastFiltered = all
            .filter { task -> tags.isEmpty() || task.tags.any { it in tags } }
            .filter { it.done == doneFlag }

        adPosition = null

        rebuildList(lastFiltered)
    }

    private fun rebuildList(list: List<TaskModel>) {
        val sorted = if (tasksViewModel.sortByUrgency.value == true)
            list.sortedByDescending { it.urgency }
        else
            list

        val items = when {
            sorted.isEmpty() -> listOf(
                MixedTaskAdapter.ListItem.EmptyItem(getString(R.string.freedom))
            )
            adPosition != null && adPosition!! in 0..sorted.size -> {
                val head = sorted.take(adPosition!!)
                val tail = sorted.drop(adPosition!!)
                head.map { MixedTaskAdapter.ListItem.TaskItem(it) } +
                        listOf(MixedTaskAdapter.ListItem.AdItem) +
                        tail.map { MixedTaskAdapter.ListItem.TaskItem(it) }
            }
            else -> sorted.map { MixedTaskAdapter.ListItem.TaskItem(it) }
        }

        adapter.submitList(items)
    }

    private fun insertAd() {
        val count = lastFiltered.size
        if (count > 0) {
            adPosition = Random.nextInt(count)
            rebuildList(lastFiltered)
        }
    }

    private fun removeAd() {
        adPosition = null
        rebuildList(lastFiltered)
    }

    override fun onDestroyView() {
        adJob?.cancel()
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(done: Boolean): TasksPageFragment =
            TasksPageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_DONE_FLAG, done)
                }
            }
    }
}
