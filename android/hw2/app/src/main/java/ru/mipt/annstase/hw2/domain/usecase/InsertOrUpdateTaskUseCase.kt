package ru.mipt.annstase.hw2.domain.usecase

import ru.mipt.annstase.hw2.data.repository.TaskRepository
import ru.mipt.annstase.hw2.domain.model.TaskModel

class InsertOrUpdateTaskUseCase(private val repository: TaskRepository) {
    suspend fun execute(task: TaskModel) = repository.insertOrUpdate(task)
}
