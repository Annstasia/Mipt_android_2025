package ru.mipt.annstase.hw3.data.remote.dto

data class MessageDto(
    val id: Int,
    val text: String,
    val sender: String,
    val timestamp: String
)