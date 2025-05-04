package ru.mipt.annstase.hw2.domain.usecase

import androidx.lifecycle.LiveData
import ru.mipt.annstase.hw2.data.repository.TaskRepository
import ru.mipt.annstase.hw2.domain.model.TaskModel

class GetTasksUseCase(private val repository: TaskRepository) {
    fun execute(): LiveData<List<TaskModel>> = repository.getTasks()
}
