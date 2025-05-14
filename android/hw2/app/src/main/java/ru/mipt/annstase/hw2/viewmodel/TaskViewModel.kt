package ru.mipt.annstase.hw2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.domain.model.TaskModel
import ru.mipt.annstase.hw2.domain.usecase.DeleteTaskUseCase
import ru.mipt.annstase.hw2.domain.usecase.GetTasksUseCase
import ru.mipt.annstase.hw2.domain.usecase.InsertOrUpdateTaskUseCase
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    application: Application,
    private val getTasksUseCase: GetTasksUseCase,
    private val insertOrUpdateUseCase: InsertOrUpdateTaskUseCase,
    private val deleteUseCase: DeleteTaskUseCase
) : AndroidViewModel(application) {

    val tasks: LiveData<List<TaskModel>> = getTasksUseCase.execute()

    private val _filtersEnabled  = MutableLiveData(true)
    private val _sortByUrgency   = MutableLiveData(false)
    private val _adsEnabled      = MutableLiveData(true)

    val filtersEnabled: LiveData<Boolean>   = _filtersEnabled
    val sortByUrgency: LiveData<Boolean>    = _sortByUrgency
    val adsEnabled: LiveData<Boolean>       = _adsEnabled

    private val _currentTagFilters = MutableLiveData<Set<String>>(emptySet())
    val currentTagFilters: LiveData<Set<String>> = _currentTagFilters

    val sections: LiveData<List<Pair<String, List<TaskModel>>>> =
        MediatorLiveData<List<Pair<String, List<TaskModel>>>>().apply {
            fun update() {
                val list = tasks.value.orEmpty()
                val filtered = if (_filtersEnabled.value == true) list.filter { !it.done } else list
                val sorted   = if (_sortByUrgency.value == true) filtered.sortedBy { -it.urgency } else filtered

                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val startToday    = cal.timeInMillis
                cal.add(Calendar.DAY_OF_MONTH, 1)
                val startTomorrow = cal.timeInMillis
                cal.add(Calendar.DAY_OF_MONTH, 1)
                val endTomorrow   = cal.timeInMillis

                val today    = sorted.filter { !it.done && it.deadline in startToday until startTomorrow }
                val tomorrow = sorted.filter { !it.done && it.deadline in startTomorrow until endTomorrow }

                fun List<TaskModel>.byTags() =
                    if (currentTagFilters.value.orEmpty().isEmpty()) this
                    else filter { it.tags.any { tag -> tag in currentTagFilters.value!! } }

                val result = mutableListOf<Pair<String, List<TaskModel>>>()
                if (today.byTags().isNotEmpty())
                    result += getResourceString(R.string.today_tasks)    to today.byTags()
                if (tomorrow.byTags().isNotEmpty())
                    result += getResourceString(R.string.tomorrow_tasks) to tomorrow.byTags()

                value = result
            }

            addSource(tasks)               { update() }
            addSource(_filtersEnabled)    { update() }
            addSource(_sortByUrgency)     { update() }
            addSource(_currentTagFilters) { update() }
        }

    fun setFiltersEnabled(enabled: Boolean) {
        _filtersEnabled.value = enabled
    }

    fun setSortByUrgency(enabled: Boolean) {
        _sortByUrgency.value = enabled
    }

    fun setAdsEnabled(enabled: Boolean) {
        _adsEnabled.value = enabled
    }

    fun setCurrentTagFilters(filters: Set<String>) {
        _currentTagFilters.value = filters
    }

    fun insertOrUpdate(task: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            insertOrUpdateUseCase.execute(task)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCase.execute(taskId)
        }
    }

    private fun getResourceString(resId: Int): String =
        getApplication<Application>().getString(resId)
}
