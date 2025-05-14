package ru.mipt.annstase.hw2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw2.data.local.AppDatabase
import ru.mipt.annstase.hw2.data.repository.TaskRepository
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.domain.usecase.DeleteTaskUseCase
import ru.mipt.annstase.hw2.domain.usecase.GetTaskByIdUseCase
import ru.mipt.annstase.hw2.domain.usecase.InsertOrUpdateTaskUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
        application: Application,
        savedStateHandle: SavedStateHandle
    ) : AndroidViewModel(application) {

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_URGENCY = "urgency"
        private const val KEY_DATE = "date"
        private const val KEY_TAGS = "tags"
    }

    private val state = savedStateHandle
    val title = state.getLiveData(KEY_TITLE, "")
    val description = state.getLiveData(KEY_DESCRIPTION, "")
    val urgency = state.getLiveData(KEY_URGENCY, 1)
    val deadlineMillis = state.getLiveData(KEY_DATE, System.currentTimeMillis())
    val tags = state.getLiveData(KEY_TAGS, emptyList<String>())
    private val db = AppDatabase.getInstance(application)
    private val repository       = TaskRepository.getInstance(db)
    private val getByIdUseCase   = GetTaskByIdUseCase(repository)
    private val saveUseCase      = InsertOrUpdateTaskUseCase(repository)
    private val deleteUseCase    = DeleteTaskUseCase(repository)

    private val _taskId = MutableLiveData<Long>()

    val task: LiveData<TaskModel?> = _taskId.switchMap { id ->
        getByIdUseCase.execute(id)
    }

    fun loadTask(id: Long) {
        _taskId.value = id
    }

    fun saveTask(model: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUseCase.execute(model)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCase.execute(id)
        }
    }

    fun onTitleChanged(new: String) {
        state[KEY_TITLE] = new
    }

    fun onDescriptionChanged(new: String) {
        state[KEY_DESCRIPTION] = new
    }

    fun onUrgencyChanged(new: Int) {
        state[KEY_URGENCY] = new
    }

    fun onDeadlineChanged(new: Long) {
        state[KEY_DATE] = new
    }

    fun onTagsChanged(new: List<String>) {
        state[KEY_TAGS] = new
    }
}
