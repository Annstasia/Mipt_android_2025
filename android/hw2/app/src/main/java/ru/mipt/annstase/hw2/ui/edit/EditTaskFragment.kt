package ru.mipt.annstase.hw2.ui.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.databinding.FragmentEditTaskBinding
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.ui.base.BaseFragment
import ru.mipt.annstase.hw2.viewmodel.EditTaskViewModel
import ru.mipt.annstase.hw2.viewmodel.SettingsViewModel
import ru.mipt.annstase.hw2.viewmodel.TasksViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditTaskFragment : BaseFragment(R.layout.fragment_edit_task) {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val editViewModel: EditTaskViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private val args: EditTaskFragmentArgs by navArgs()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEditTaskBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
    }

    override fun initUI() {
        settingsViewModel.filtersEnabled.asLiveData().observe(viewLifecycleOwner) { enabled ->
            binding.tilTags.isVisible = enabled
            binding.etTags.isEnabled = enabled
        }

        editViewModel.title.observe(viewLifecycleOwner) {
            if (binding.etTitle.text.toString() != it) binding.etTitle.setText(it)
        }
        editViewModel.description.observe(viewLifecycleOwner) {
            if (binding.etDescription.text.toString() != it) binding.etDescription.setText(it)
        }
        editViewModel.urgency.observe(viewLifecycleOwner) {
            val urgencyStr = it.toString()
            if (binding.etUrgency.text.toString() != urgencyStr) binding.etUrgency.setText(urgencyStr)
        }
        editViewModel.deadlineMillis.observe(viewLifecycleOwner) {
            binding.btnDeadline.text = dateFormat.format(Date(it))
        }
        editViewModel.tags.observe(viewLifecycleOwner) {
            val joined = it.joinToString(",")
            if (binding.etTags.text.toString() != joined) binding.etTags.setText(joined)
        }

        if (args.taskId != 0L) {
            editViewModel.loadTask(args.taskId)
            editViewModel.task.observe(viewLifecycleOwner) { task ->
                task?.let {
                    editViewModel.onTitleChanged(it.title)
                    editViewModel.onDescriptionChanged(it.description)
                    editViewModel.onUrgencyChanged(it.urgency)
                    editViewModel.onDeadlineChanged(it.deadline)
                    editViewModel.onTagsChanged(it.tags)
                }
                binding.btnDelete.isVisible = true
            }
        } else {
            binding.btnDelete.isVisible = false
        }
    }

    override fun initListeners() {
        binding.etTitle.doAfterTextChanged {
            editViewModel.onTitleChanged(it.toString())
        }

        binding.etDescription.doAfterTextChanged {
            editViewModel.onDescriptionChanged(it.toString())
        }

        binding.etUrgency.doAfterTextChanged {
            val txt = it.toString().trim()
            val urgency = txt.toIntOrNull().takeIf { txt.isDigitsOnly() } ?: 0
            editViewModel.onUrgencyChanged(urgency)
        }

        binding.etTags.doAfterTextChanged {
            val tags = it.toString().split(',').map(String::trim).filter { it.isNotEmpty() }
            editViewModel.onTagsChanged(tags)
        }

        binding.btnDeadline.setOnClickListener {
            val cal = Calendar.getInstance().apply {
                timeInMillis = editViewModel.deadlineMillis.value ?: System.currentTimeMillis()
            }

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    cal.set(year, month, day, 0, 0, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    editViewModel.onDeadlineChanged(cal.timeInMillis)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSave.setOnClickListener {
            val title = editViewModel.title.value?.trim().orEmpty()
            if (title.isEmpty()) {
                binding.etTitle.error = getString(R.string.error_empty_title)
                return@setOnClickListener
            }

            val task = TaskModel(
                id          = args.taskId,
                title       = title,
                description = editViewModel.description.value.orEmpty(),
                deadline    = editViewModel.deadlineMillis.value ?: System.currentTimeMillis(),
                urgency     = editViewModel.urgency.value ?: 0,
                tags        = if (settingsViewModel.filtersEnabled.value == true)
                    editViewModel.tags.value.orEmpty() else emptyList(),
                done        = editViewModel.task.value?.done ?: false
            )
            editViewModel.saveTask(task)
            findNavController().navigateUp()
        }

        binding.btnDelete.setOnClickListener {
            if (args.taskId != 0L) {
                editViewModel.deleteTask(args.taskId)
            }
            findNavController().navigateUp()
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
