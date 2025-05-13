package ru.mipt.annstase.hw3.data.remote.dto

data class ChatDtoWithMessages(
    val id: Int,
    val messages: List<MessageDto>
)
