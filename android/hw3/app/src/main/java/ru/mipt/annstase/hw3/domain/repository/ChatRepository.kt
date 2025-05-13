package ru.mipt.annstase.hw3.domain.repository


import ru.mipt.annstase.hw3.data.remote.ApiService
import ru.mipt.annstase.hw3.domain.model.Chat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getChats(): List<Chat> {
        val map = apiService.getChats().chats.map { dto ->
            Chat(
                id = dto.id,
                name = dto.name
            )
        }
        return map
    }


    suspend fun createChat(name: String): List<Chat> {

        return apiService.createChat(name).chats.map { dto ->
            Chat(
                id = dto.id,
                name = dto.name
            )
        }
    }
}