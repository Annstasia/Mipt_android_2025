package ru.mipt.annstase.hw3.data.remote

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.mipt.annstase.hw3.data.remote.dto.ChatDtoWithMessages
import ru.mipt.annstase.hw3.data.remote.dto.ChatsRsDto


interface ApiService {
    @GET("chats")
    suspend fun getChats(): ChatsRsDto

    @GET("chat")
    suspend fun getChat(
        @Query("id") chatId: Int
    ): ChatDtoWithMessages

    @POST("msg")
    suspend fun postMessage(
        @Query("id") chatId: Int,
        @Query("text") text: String
    ): ChatDtoWithMessages

    @POST("create_chat")
    suspend fun createChat(
        @Query("name") name: String
    ): ChatsRsDto
}