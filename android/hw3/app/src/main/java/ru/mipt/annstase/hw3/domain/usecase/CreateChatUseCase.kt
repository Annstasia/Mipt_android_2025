package ru.mipt.annstase.hw3.domain.usecase

import ru.mipt.annstase.hw3.domain.model.Chat
import ru.mipt.annstase.hw3.domain.repository.ChatRepository


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(name: String): List<Chat> =
        repository.createChat(name)
}
