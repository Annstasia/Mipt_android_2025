package ru.mipt.annstase.hw3.domain.usecase

import ru.mipt.annstase.hw3.domain.model.Chat
import ru.mipt.annstase.hw3.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): List<Chat> =
        chatRepository.getChats()
}
