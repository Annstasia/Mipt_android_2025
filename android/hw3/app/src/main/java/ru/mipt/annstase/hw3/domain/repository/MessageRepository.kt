package ru.mipt.annstase.hw3.domain.repository


import ru.mipt.annstase.hw3.data.remote.ApiService
import ru.mipt.annstase.hw3.domain.model.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMessages(chatId: Int): List<Message> {
        return apiService.getChat(chatId).messages.map { dto ->
            Message(
                id = dto.id,
                name = dto.text
            )
        }
    }


    suspend fun postMessage(chatId: Int, text: String): List<Message> {
        return apiService.postMessage(chatId, text).messages.map { dto ->
            Message(
                id = dto.id,
                name = dto.text
            )
        }
    }
}