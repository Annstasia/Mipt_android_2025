package ru.mipt.annstase.hw3.domain.usecase


import ru.mipt.annstase.hw3.domain.model.Message
import ru.mipt.annstase.hw3.domain.repository.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(chatId: Int): List<Message> =
        repository.getMessages(chatId)
}
