package ru.mipt.annstase.hw2.domain.usecase

import androidx.lifecycle.LiveData
import ru.mipt.annstase.hw2.data.repository.TaskRepository
import ru.mipt.annstase.hw2.domain.model.TaskModel

class GetTaskByIdUseCase(private val repository: TaskRepository) {
    fun execute(taskId: Long): LiveData<TaskModel?> = repository.getTaskById(taskId)
}
