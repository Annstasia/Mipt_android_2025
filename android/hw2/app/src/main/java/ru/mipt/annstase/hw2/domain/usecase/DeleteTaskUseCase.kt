package ru.mipt.annstase.hw2.domain.usecase

import ru.mipt.annstase.hw2.data.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend fun execute(taskId: Long) = repository.delete(taskId)
}
